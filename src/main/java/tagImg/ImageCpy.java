package tagImg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class ImageCpy {
    public int file_num = 0;
    int[] indexs;
    int currIndex = 0;
    String letters = getNameRoot(8);

    //后缀
    public static String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
            return "";
        } else {
            return filename.substring(dix + 1);
        }
    }

    public static String getNameRoot(int l) {
        String str = "";
        for (int i = 0; i < l; i++) {
            str = str + (char) (Math.random() * 26 + 'a');
        }
        return str;
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

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
//        System.out.println(oldPath + "-->" + newPath);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public void copyRandImage(List<TagedFile> filelist, String toDir, int num) {
//        List<File> filelist = SubDirImages.getFileList(new ArrayList<File>(), fromDir);
        file_num = filelist.size();
        if (num > file_num) num = file_num;
        genRandomList();
        TagedFile f;
        String fromPath = "";
        String toPath = "";


        String numbers = "";
        String suf = "";
//        System.out.println(num);
        for (int i = 0; i < num; i++) {
            f = filelist.get(indexs[i]);
            fromPath = f.getAbsolutePath();

            numbers = "" + currIndex;
            currIndex++;
            while (numbers.length() < 6) {
                numbers = "0" + numbers;
            }
            suf = getSuffix(f.getName());

            toPath = toDir + "/" + letters + numbers + "." + suf;
            copyFile(fromPath, toPath);
        }

    }
}
