package top.niandui.common.uitls.file;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * csv工具
 *
 * @author liyongda
 * @version 1.0
 * @time 2020/3/6 14:47
 */
public class CSVUtil {
    // 每隔BATCH_COUNT条存储数据库，然后清理list ，方便内存回收
    private static final int BATCH_COUNT = 2000;

    /**
     * 根据标题读取CSV
     *
     * @param is          文件输入流
     * @param headerMap   头部字段映射：中文映射字段名
     * @param callback    回调方法，用于读取数据的保存
     * @param charsetName 文件编码格式
     */
    public static void read(InputStream is, Map<String, String> headerMap, Consumer<List<Map<String, String>>> callback, String charsetName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName))) {
            // 获取头部信息
            String line = br.readLine();
            String[] header = line.split(",");
            for (int i = 0; i < header.length; i++) {
                header[i] = headerMap.get(header[i]);
            }
            // 获取数据信息
            List<Map<String, String>> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                int start = 0, end = line.length();
                if (line.startsWith("\"")) {
                    start = 1;
                }
                if (line.endsWith("\"")) {
                    end--;
                }
                String[] record = line.substring(start, end).split("\",\"");
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
            throw new RuntimeException("读取文件失败", e);
        }
    }

    /**
     * 读取CSV所有信息
     *
     * @param filepath    文件路径
     * @param charsetName 文件编码格式
     * @return 读取的所有信息，一行为一条
     */
    public static List<String> readAllLine(String filepath, String charsetName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), charsetName))) {
            // 获取数据信息
            String line;
            List<String> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if ("".equals(line)) {
                    continue;
                }
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败", e);
        }
    }
}
