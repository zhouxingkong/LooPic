package tagImg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TagedFile {
    public static final String DIV = "-";
    File file;
    List<String> tags;

    public TagedFile(File f) {
        file = f;
        tags = new ArrayList<String>();
        getTags();
    }

    public String getName() {
        return file.getName();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public void getTags() {
        String name = file.getName();
        if (name == null || name.length() < 2) return;
        /**/
        String[] nameSplitDot = name.split("\\.");
        if (nameSplitDot.length < 1) return;
        String suffix = nameSplitDot[nameSplitDot.length - 1];    //文件後綴

        String[] nameSplitLine = nameSplitDot[0].split(DIV);
        if (nameSplitLine.length < 1) return;

        for (String s : nameSplitLine) {
            tags.add(s);
        }
    }


}
