package top.niandui.uri_and_url;

import org.springframework.util.Base64Utils;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Base64;

/**
 * uri_and_url 测试
 */
public class URITest {
    public static void main(String[] args) throws IllegalAccessException {
        URI uri = URI.create("http://www.baidu.com/s?ie=%41你好");
        System.out.println(uri);
        String uriQuery = uri.getQuery();
        System.out.println(uriQuery);
        String rawQuery = uri.getRawQuery();
        System.out.println(rawQuery);
        //是否绝对URI
        System.out.println(uri.isAbsolute());

        System.out.println(uri.toString());
        System.out.println(uri.toASCIIString());



        System.exit(0);
        Class<URI> uriClass = URI.class;
        Field[] fields = uriClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.printf("%s : %s\n", field.getName(), field.get(uri));
        }
    }
}
