package top.niandui.common.uitls.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liangj
 * @version 1.0
 * @Title DelFileUtil.java
 * @description 删除文件
 * @time 2019-3-27 上午10:34:46
 */
public class DelFileUtil {
    /**
     * @param folderPath
     * @return void
     * @Title delFolder
     * @Description 删除文件夹
     * @author liangj
     * @date 2019-3-27 上午10:35:32
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path
     * @return boolean
     * @Title delAllFile
     * @Description 删除指定文件夹下的所有文件
     * @author liangj
     * @date 2019-3-27 上午10:35:57
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /*
     * @title deleteFileDateNum
     * @description 删除指定天数前的文件
     * @param [path, dateNum]
     * @return void
     * @author huangwx
     * @date 2020/5/26 17:15
     */
    public static void deleteFileDateNum(String path, int dateNum) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String Days = sdf.format(date.getTime() - dateNum * 24 * 60 * 60 * 1000);//获取n天前日期
        int aa = Integer.parseInt(Days);

        if (file.isDirectory()) {
            File[] fileLists = file.listFiles(); // 如果是目录，获取该目录下的内容集合

            for (int i = 0; i < fileLists.length; i++) { // 循环遍历这个集合内容
                String lastTime = sdf.format(fileLists[i].lastModified());   //获取文件最后修日期
                int ff = Integer.parseInt(lastTime);
                if (ff - aa < 0 || ff - aa == 0) {
                    deleteDir(fileLists[i]);
                }
            }
        }
    }

    /**
     * 递归算法，删除非空目录
     *
     * @param file
     */
    public static void deleteDir(File file) {
        if (file.isFile()) {//表示该文件不是文件夹
            file.delete();
        } else {
            //首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + "/" + childFilePath);
                deleteDir(childFile);
            }
            file.delete();
        }
    }
}
