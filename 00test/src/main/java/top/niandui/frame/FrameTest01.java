package top.niandui.frame;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import top.niandui.frame.utils.FileUtils;
import top.niandui.frame.utils.WebClientUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @Title: FrameTest01.java
 */
public class FrameTest01 {
    @Test
    public void test1() {
        new Frame01();
    }

    public static void main(String[] args) {
        new Frame01();
    }

    public static class Frame01 extends JFrame {
        private JTextField jtf; //文本框
        private JButton jbt; //按钮

        public Frame01() {
            this.setTitle("chaugnti");
            this.setSize(600, 450);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLayout(new FlowLayout());//创建面板布局
            this.setLocationRelativeTo(null);//窗口居中

            jtf = new JTextField(40);//文本框的初始化
            jtf.setText("https://www.veer.com/topic/604/");
            jbt = new JButton("按钮");//按钮的初始化
            jbt.addActionListener(this::myActionListener);
            add(jtf);
            add(jbt);
            setLayout(new FlowLayout());
            setVisible(true);//窗口可见
        }

        private void myActionListener(ActionEvent e) {
            new Thread(() -> {
                try {
                    String href = jtf.getText().trim();
                    List<DomAttr> imgSrcs = getInfo(href);
                    int top = 30;
                    for (DomAttr src : imgSrcs) {
                        URL url = new URL(src.getValue());
                        try {
                            BufferedImage bimg = ImageIO.read(url);
//                            System.out.println(bimg);
                            getGraphics().drawImage(bimg, (getWidth() - bimg.getWidth()) / 2,
                                    top, bimg.getWidth(), bimg.getHeight(), null);
                            top += bimg.getHeight();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }).start();
        }

        private <T> List<T> getInfo(String href) throws Exception {
            WebClient webClient = WebClientUtils.getWebClient();
//            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = webClient.getPage(href);
            Thread.sleep(1000 * 3);
            FileUtils.write(new File(ClassLoader.getSystemResource("").getPath()).getParentFile().getAbsolutePath() + "/" + page.getTitleText() + ".html", page.asXml());
            List<T> list = page.getByXPath("//a[@class='search_result_asset_link']/img/@src");
            System.out.println(list);
            return list;
        }
    }

}
