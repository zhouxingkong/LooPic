package singleDir;

import java.io.File;
import java.util.List;
import java.util.Random;

public class SingleDirRename {
    public int img_index = 0;
    public int file_num = 0;
    String dir;
    String[] files;
    List imglist;

    int[] indexs;


    public String getNameRoot(int l) {
        String str = "";
        for (int i = 0; i < l; i++) {
            str = str + (char) (Math.random() * 26 + 'a');
        }
        return str;
    }

    public void getFileList(String base) {
        dir = base/*+lists[file_index]+"/"*/ + "/";
        File file = new File(dir);
        files = file.list();
        file_num = files.length;
        imglist = java.util.Arrays.asList(files);
    }

    public void genLinearList() {
        indexs = new int[file_num];
        for (int j = 0; j < file_num; j++) {
            indexs[j] = j;    //给索引数组赋值
        }
    }

    public void genRandomList() {
        genLinearList();
        Random random = new Random();
        for (int j = 0; j < file_num; j++) {
            int temp = indexs[j];
            int newindx = random.nextInt(file_num - 1);
            indexs[j] = indexs[newindx];  //交换位置
            indexs[newindx] = temp;
        }
    }

    public void doRename(String pre, int offset) {
        for (int i = 0; i < file_num; i++) {

            String path = dir + imglist.get(img_index);

            String[] split_by_dir = path.split("/");
            String last_part = "";
            last_part = split_by_dir[split_by_dir.length - 1];

            String[] split_by_dot = last_part.split("\\.");

            String suffix = split_by_dot[split_by_dot.length - 1];
            String numStr = "" + (indexs[i] + offset);
            for (int j = 0; j < 6; j++) {
                if (numStr.length() < 6) {
                    numStr = "0" + numStr;
                }
            }
            String outputDir = "";
            for (int j = 0; j < split_by_dir.length - 1; j++) {
                outputDir += split_by_dir[j];
            }
            outputDir += "/";
            outputDir += pre;
            outputDir += numStr;
            outputDir += ".";
            outputDir += suffix;
//            System.out.println(outputDir);
            //重命名文件
            File file = new File(path);
            boolean ret = file.renameTo(new File(outputDir));
            if (!ret) {
                System.out.println("Error!!" + path + "   ->    " + outputDir);
            }

            img_index++;
        }
    }

    public void renameLinear(String pre, int offset) {
        genLinearList();
        doRename(pre, offset);

    }

    public void renameRand(String pre, int offset) {
        genRandomList();
        doRename(pre, offset);

    }

}
