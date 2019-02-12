package multiDir;

import java.io.File;
import java.util.List;

/*图片标签管理*/
public class TagManager {
    public static final String DIV = "-";
    public int img_index = 0;
    public int file_num = 0;
    String dir;
    String[] files;
    List imglist;
    int[] indexs;

    public void getFileList(String base) {
        dir = base/*+lists[file_index]+"/"*/ + "/";
        File file = new File(dir);
        files = file.list();
        file_num = files.length;
        imglist = java.util.Arrays.asList(files);
    }

    public String addTagToName(String name, String tag) {
        if (name == null || name.length() < 2) return "";
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return "";
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return "";

        for (String s : nameSplitLine) {
            if (tag.equals(s)) {
                return nameSplitDot[0] + "." + suffix;
            }
//            System.out.println(s+":"+tag);
        }
        return nameSplitDot[0] + DIV + tag + "." + suffix;

    }

    public String removeTagFromName(String name, String tag) {
        if (name == null || name.length() < 2) return "";
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return "";
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return "";

        String outputName = nameSplitLine[0];
        for (int i = 1; i < nameSplitLine.length; i++) {
            if (!tag.equals(nameSplitLine[i])) {
                outputName = outputName + DIV + nameSplitLine[i];
            }
//            System.out.println(s+":"+tag);
        }
        return outputName + "." + suffix;

    }

    public void renameFile(String fromPath, String toPath) {
        File file = new File(fromPath);
        boolean ret = file.renameTo(new File(toPath));
        if (!ret) {
            System.out.println("Error!!" + fromPath + "   ->    " + toPath);
        }
    }

    public void doAddTag(String dir, String tag) {
        img_index = 0;
        for (int i = 0; i < file_num; i++) {

            String oldName = "" + imglist.get(img_index);
            String newName = addTagToName(oldName, tag);

            //重命名文件
            renameFile(dir + oldName, dir + newName);

            img_index++;
        }
    }

    public void doRemoveTag(String dir, String tag) {
        img_index = 0;
        for (int i = 0; i < file_num; i++) {

            String oldName = "" + imglist.get(img_index);
            String newName = removeTagFromName(oldName, tag);

            //重命名文件
            renameFile(dir + oldName, dir + newName);

            img_index++;
        }
    }

    public void addTag(String dir, String tag) {
        getFileList(dir);
        doAddTag(dir + "/", tag);
    }

    public void removeTag(String dir, String tag) {
        getFileList(dir);
        doRemoveTag(dir + "/", tag);
    }
}
