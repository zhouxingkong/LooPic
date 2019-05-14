package tagImg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*管理配置文件*/
public class ConfigFileManager {
    public static final String DIV = "-";
    public PicNameFilter picFilter;
    public ImageCpy imageCpy;
    public FilterResultStorage resultStorage;
    public String targetDir;
    public String exceptDir;

    List<TagedFile> totalList;
    List<String> excludeTags;

    public ConfigFileManager() {
        picFilter = new PicNameFilter();
        imageCpy = new ImageCpy();
        resultStorage = new FilterResultStorage();
    }

    public int[] genRandomList(int file_num) {
        if (file_num < 2) {
            return new int[]{0};
        }
        int[] indexs = new int[file_num];
        for (int j = 0; j < file_num; j++) {
            indexs[j] = j;    //给索引数组赋值
        }
        Random random = new Random();
        for (int j = 0; j < file_num; j++) {
            int temp = indexs[j];
            int newindx = random.nextInt(file_num - 1);
            indexs[j] = indexs[newindx];  //交换位置
            indexs[newindx] = temp;
        }

        return indexs;
    }

    public String makeKey(String[] tags) {
        String out = tags[0];
        for (int i = 1; i < tags.length; i++) {
            out = out + DIV + tags[i];
        }
        return out;
    }

    /**
     * 读取排除标签的配置文件
     *
     * @param dir
     * @throws IOException
     */
    public void loadExceptTags(String dir) throws IOException {
        excludeTags = new ArrayList<String>();
        FileReader fr = new FileReader(dir);
        BufferedReader bf = new BufferedReader(fr);
        String str;
        /*读取源文件路径*/
        while ((str = bf.readLine()) != null) {
            excludeTags.add(str);
        }

//        System.out.println("排除配置个数"+exceptTags.size()+"第一个"+exceptTags.get(0));

    }

    /*解析一行配置文件*/
    public void parseOneLine(String line) {
        String[] nameSplitSpace = line.split("\\ ");
        if (nameSplitSpace.length < 2) return;
        int picNum = Integer.parseInt(nameSplitSpace[1]);   //总共图片数量


        String key = nameSplitSpace[0];     //storage的键
        String[] tags = key.split(DIV);
        if (tags.length < 1) return;

        List<TagedFile> filteredList = null;
        int[] indexs;
        FilterResult fr = null;
        int storagedNum = 0;
        while (filteredList == null || filteredList.size() < picNum) {
            storagedNum = resultStorage.searchResultNum(key);   //搜索缓存
            if (storagedNum >= picNum) {
                fr = resultStorage.get(key);
                break;
            } else {
                /**/
                filteredList = picFilter.filter(totalList, tags, excludeTags, picNum);    //过滤
                /**/
                int fileNum = filteredList.size();
                if (fileNum < picNum && tags.length > 1) {
                    key = key.substring(0, key.lastIndexOf(DIV));
                    tags = key.split(DIV);
                } else {
                    indexs = genRandomList(fileNum);
                    fr = new FilterResult(filteredList, indexs);
                    resultStorage.put(key, fr);  //缓存结果
                    break;
                }

            }

            //todo ：shuffle

        }

        /*拷贝文件*/
        imageCpy.copyRandImage(fr, targetDir, picNum);
        System.out.println("tag=" + key + "resultNum = " + fr.getFilteredList().size());
    }

    public void parseConfigFile(String dir) {
        String name = dir + "/" + "config.txt";
//        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            /*读取源文件路径*/
            if ((str = bf.readLine()) != null) {
                List<TagedFile> fileList = new ArrayList<TagedFile>();
                totalList = PicNameFilter.getFileList(fileList, str);  //获取总共的文件列表
            }
            /*目标文件路径*/
            if ((str = bf.readLine()) != null) {
                targetDir = str;
            }

            /*读取排除文件路径*/
            if ((str = bf.readLine()) != null) {
                exceptDir = str;
                loadExceptTags(exceptDir);
            }

            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                parseOneLine(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LooFromConfigFile(String dir) {

        parseConfigFile(dir);
    }

}
