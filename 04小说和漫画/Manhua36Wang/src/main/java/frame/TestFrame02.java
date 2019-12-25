package frame;

import utils.Manhua36Wang;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFrame02 extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TestFrame02();
	}

	public TestFrame02() {
		this.setTitle("chaugnti");
		this.setSize(600, 450);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());//创建面板布局
        this.setLocationRelativeTo(null);//窗口居中

	    final JTextField jtf;//文本框
	    JButton jbt;//按钮
        jtf = new JTextField(40);//文本框的初始化
        jbt = new JButton("按钮");//按钮的初始化
		jbt.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

					new Thread() {
						@Override
						public void run() {
							try {
								String href = jtf.getText().trim();
								List<String> imgSrcs = Manhua36Wang.getChapterImgSrcs(href);
								int top = 30;
								for (String src : imgSrcs) {
									URL url = new URL(src);
									try {
										BufferedImage bimg = ImageIO.read(url);
										System.out.println(bimg);
										getGraphics().drawImage(bimg, (getWidth() - bimg.getWidth()) / 2,
												top, bimg.getWidth(), bimg.getHeight(), null);
										top += bimg.getHeight();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							} catch (Exception exx) {
								exx.printStackTrace();
							}
						}
					}.start();
			}
		});
        add(jtf);
        add(jbt);
        setLayout(new FlowLayout());

        setVisible(true);//窗口可见
	}
}
