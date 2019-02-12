package tagImg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*管理配置文件*/
public class ConfigFileManager {
    public static final String DIV = "-";
    public PicNameFilter picFilter;
    public ImageCpy imageCpy;
    public String targetDir;

    List<TagedFile> totalList;

    public ConfigFileManager() {
        picFilter = new PicNameFilter();
        imageCpy = new ImageCpy();
    }

    /*解析一行配置文件*/
    public void parseOneLine(String line) {
        String[] nameSplitSpace = line.split("\\ ");
        if (nameSplitSpace.length < 2) return;
        int picNum = Integer.parseInt(nameSplitSpace[1]);   //总共图片数量

        String[] tags = nameSplitSpace[0].split(DIV);
        if (tags.length < 1) return;

        List<TagedFile> filteredList = null;
        filteredList = picFilter.filter(totalList, tags, picNum);    //过滤
        while (filteredList == null || filteredList.size() < picNum) {
            tags = Arrays.copyOf(tags, tags.length - 1);
            filteredList = picFilter.filter(totalList, tags, picNum);    //过滤

        }


        imageCpy.copyRandImage(filteredList, targetDir, picNum);
        System.out.println("tag=" + tags[0] + "resultNum = " + filteredList.size());
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
