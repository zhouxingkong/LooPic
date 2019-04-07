package tagImg;

import tagImg.match.MatchBase;
import tagImg.match.MatchFloorProperSubnorm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PicNameFilter {

    /**
     * 引入策略模式，将不同标签匹配策略封装到子类中
     */
    MatchBase matchBase = new MatchFloorProperSubnorm();

    public static List<TagedFile> getFileList(List<TagedFile> filelist, String strPath) {

        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
//                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(filelist, files[i].getAbsolutePath()); // 获取文件绝对路径
                } else {
//                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + strFileName);
                    filelist.add(new TagedFile(files[i]));
                }
            }

        }
        return filelist;
    }

    List<TagedFile> filter(List<TagedFile> inputList, String[] tags, int num) {
        List<TagedFile> result = null;
        List<String> inputTag = new ArrayList<String>(Arrays.asList(tags));
        while (result == null) {
            result = inputList.stream()
                    .filter((TagedFile f) -> judgeCandidate(f, inputTag))    //step1: 找出所有全部包含目标标签组的文件集合
                    .peek(tagedFile -> matchBase.computeMp(tagedFile, inputTag)) //step2:使用标签匹配算法来排序文件的优先级
                    .sorted(Comparator.comparing(o -> o.mp))    //step3:将结果按照匹配分排序
                    .collect(Collectors.toList());
            //result.sort(Comparator.comparing(o -> o.mp));
            /*打印结果*/
//            printResult(result, inputTag);
        }

        return result;
    }


    public void printResult(List<TagedFile> result, List<String> inputTag) {
        System.out.println("------------------------");
        System.out.print("目标标签:");
        for (String t : inputTag) {
            System.out.print("-" + t);
        }
        System.out.println(" ");
        for (TagedFile t : result) {
            for (String s : t.tags) {
                System.out.print("-" + s);
            }
            System.out.println(" ;mp=" + t.mp);
        }
    }

    public boolean judgeCandidate(TagedFile f, List<String> inputTag) {
        for (String t : inputTag) {
            if (!f.tags.contains(t)) {
                return false;
            }
        }
        return true;
    }

}
