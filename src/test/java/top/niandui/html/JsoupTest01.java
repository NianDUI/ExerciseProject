package top.niandui.html;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.niandui.common.uitls.HttpUtil;

/**
 * 应用启动自动加载
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 23:22
 */
public class JsoupTest01 {
    public static void main(String[] args) throws Exception {
        String url = "https://www.147xs.org/book/150820/226230160.html";

        String s = HttpUtil.get(url, null, String.class);
        System.out.println("s = " + s);

        Connection connect = Jsoup.connect(url)
//                .ignoreHttpErrors(true)
//                .ignoreContentType(true)

                .method(Connection.Method.GET)
                .header("authority", "www.147xs.org")
                .header("method", "GET")
                .header("path", "/book/150820/226230160.html")
                .header("scheme", "https")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "h-CN,zh;q=0.9,en;q=0.8")
                .cookie("cf_clearance", "bzxxScHO0vWcLiigtDt53T7MfuAaz5goJVCw0hVQA00-1673446115-0-160")
                .header("if-modified-since", "Wed, 11 Jan 2023 03:52:29 GMT")
                .header("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "Windows")
                .header("sec-fetch-dest", "document")
                .header("sec-fetch-mode", "navigate")
                .header("sec-fetch-site", "same-origin")
                .header("sec-fetch-user", "?1")
                .header("upgrade-insecure-connects", "1")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

        Document document = connect.get();
        System.out.println("document.text() = " + document.text());


    }
}
