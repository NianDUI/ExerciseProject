package top.niandui.img_download;

import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import top.niandui.img_download.utils.FileUtils;
import top.niandui.img_download.utils.WebClientUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 图片下载
 */
public class Test1 {
    static String path;
    static {
        URL url = ClassLoader.getSystemResource("");
        path = url.getFile();
        path = path.substring(1, path.indexOf("target") + 7);
        System.out.println(path);
    }

    @Test
    public void test1() throws Exception {

        WebClient webClient = WebClientUtils.getWebClient();
        HtmlPage page = webClient.getPage("https://*****.com/tupian/list-****.html");
//        FileUtils.write(path + "baidu.html", page.asXml());
        System.out.println(page.getUrl());
        path += "list/";
        List<HtmlAnchor> aList = page.getByXPath("//div[@id='tpl-img-content']/li/a");
        for (HtmlAnchor a : aList) {
//            new Thread(() -> {
//                try {
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
            String title = ((DomAttr) a.getFirstByXPath("@title")).getValue();
            HtmlPage htmlPage = a.click();


            HtmlImage img = htmlPage.getFirstByXPath("//div[@class='content']/img");
//                HtmlImage img = webClient.getPage(src);
            img.setAttribute("src", "https://****.com/***/**/1235/01.jpg");
            img.doOnLoad();
            ImageReader imageReader = img.getImageReader();
            BufferedImage read = imageReader.read(0);
            ImageIO.write(read, "jpg", new File("2.jpg"));

//            List<DomAttr> imgSrcList = htmlPage.getByXPath("//div[@class='content']/img/@data-original");
//            for (DomAttr domAttr : imgSrcList) {
//                String src = domAttr.getValue();
////                FileUtils.write(src,path , title , src.substring(src.lastIndexOf('/')));
//                HtmlImage img = htmlPage.getFirstByXPath("//div[@class='content']/img");
////                HtmlImage img = webClient.getPage(src);
//                img.setAttribute("src", src);
//                img.doOnLoad();
//                ImageReader imageReader = img.getImageReader();
//                ImageIO.write((RenderedImage) imageReader, "jpg", new File("2.jpg"));
//                break;
//            }

            break;
        }
    }
}
