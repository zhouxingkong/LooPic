import org.junit.Test;
import tagImg.PicNameFilter;
import tagImg.TagedFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestSomething {
    @Test
    public void test() {
//        key = key.substring(0,key.lastIndexOf("-"));
        String key = "advc";
        System.out.println(key.lastIndexOf("-"));
    }

    @Test
    public void testRadix() {
//        key = key.substring(0,key.lastIndexOf("-"));
        String key = "正则-表达式-好用";
//        System.out.println(key.matches(".*表达|^((?!正则).)*$"));
        System.out.println(key.matches("((?!表达式).)*"));
    }

    @Test
    public void testRanking() {
        List<TagedFile> totalList = new ArrayList<TagedFile>();
        totalList.add(new TagedFile(new File("F:/601/unicloud/data/xxxxxxxx/pic01-good.jpg")));
        totalList.add(new TagedFile(new File("F:/601/unicloud/data/xxxxxxxx/pic02-good.jpg")));
        totalList.add(new TagedFile(new File("F:/601/unicloud/data/xxxxxxxx/pic03-good.jpg")));
        totalList.add(new TagedFile(new File("F:/601/unicloud/data/xxxxxxxx/pic04-good.jpg")));
        totalList.add(new TagedFile(new File("F:/601/unicloud/data/xxxxxxxx/pic05-good.jpg")));

        PicNameFilter picFilter = new PicNameFilter();
        List<TagedFile> out = picFilter.filter(totalList, new String[]{"good"}, 5);
        for (TagedFile tf : out) {
//            System.out.println("name="+tf.file.getName()+"   rank="+tf.mp);
            System.out.println("name=" + tf.file.getName());
        }

    }

}
