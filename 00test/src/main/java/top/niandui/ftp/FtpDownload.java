package top.niandui.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Title: FtpDownload.java
 * @description: ftp文件下载
 * @time: 2020/3/6 9:53
 * @author: liyongda
 * @version: 1.0
 */
public class FtpDownload {

    public static void main(String[] args) {
        FTPClient ftp = new FTPClient();
        try {
            // 设置ftp字符集
            ftp.setControlEncoding("utf-8");
            // 连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect("127.0.0.1", 21);
            // 输入账号和密码进行登录:匿名用户anonymous
            ftp.login("anonymous","");
            // 接受状态码(如果成功，返回230，如果失败返回503)
            int replyCode = ftp.getReplyCode();
            // 设置文件传输模式为二进制，可以保证传输的内容不会被改变
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println("replyCode = " + replyCode);
            // 根据状态码检测ftp的连接，调用isPositiveCompletion(reply)：成功true，否则false
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("failed");
                return;
            }
            for (FTPFile ftpFile : ftp.listFiles()) {
                System.out.println(ftpFile);
            }
            InputStream is = ftp.retrieveFileStream("/org/2020-02-20-org-01.csv");
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("组织ID", "f_orgid");
            headerMap.put("组织编码", "f_orgcode");
            headerMap.put("组织类型", "f_orgtype");
            headerMap.put("组织名称", "f_orgname");
            headerMap.put("父组织ID", "f_parentorgid");
            headerMap.put("父组织编码", "f_parenntorgcode");
            headerMap.put("省分编码", "f_procode");
            headerMap.put("组织级别", "f_orglevel");
            headerMap.put("描述", "f_description");
            headerMap.put("专业", "f_professional");
            headerMap.put("地址", "f_address");
            headerMap.put("区域编码", "f_areacode");
            headerMap.put("备注", "f_remark");
            headerMap.put("是否叶子节点", "f_leaf");
            read(is, headerMap, FtpDownload::print);
            // 退出ftp
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void print(List list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    private static final int BATCH_COUNT = 3000;

    public static void read(InputStream is, Map<String, String> headerMap, Consumer<List<Map<String, String>>> callback) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "GBK"));
            String line = br.readLine();
            String[] header = line.split(",");
            for (int i = 0; i < header.length; i++) {
                header[i] = headerMap.get(header[i]);
            }
            List<Map<String, String>> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] record = line.substring(1, line.length() - 1).split("\",\"");
                Map<String, String> data = new HashMap<>(header.length, 1);
                for (int i = 0; i < record.length; i++) {
                    data.put(header[i], record[i]);
                }
                list.add(data);
                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易BOOM
                if (list.size() >= BATCH_COUNT) {
                    callback.accept(list);
                    // 存储完成清理 list
                    list.clear();
                }
            }
            if (list.size() > 0) {
                // 回调方法
                callback.accept(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {}
            }
        }
    }
}
