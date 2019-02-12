package tagImg;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class PicNameFilter {

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
        while (result == null) {
            result = inputList.stream()
                    .filter((TagedFile f) -> {
                        String name = f.getName();
                        for (String t : tags) {
                            if (!f.tags.contains(t)) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return result;
    }
}
