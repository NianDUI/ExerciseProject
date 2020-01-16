package top.niandui.utils.file;

//import com.synqnc.exception.ReFormatException;
import org.springframework.web.multipart.MultipartFile;
import top.niandui.utils.UtilDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopyUtil {
    /**
     *
     * @Title saveFile
     * @Description 多媒体流保存成文件
     * @param Mfile
     * @param path
     * @param addTime 文件名是否加时间,true加,false不加
     * @throws Exception
     * @return String
     * @author huangwx
     * @date 2018-9-14 上午9:46:49
     */
    public static String saveFile(MultipartFile Mfile, String path, boolean addTime) throws Exception {
        String fileName = Mfile.getOriginalFilename();
        String Suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            // 获取本地存储路径
            File file = new File(path);
            // 创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
            if (!file.exists())
                file.mkdirs();
            // 新建一个文件
            File file1;
            if (addTime) {
                file1 = new File(path + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "_"
                        + UtilDate.DateToStryyyyMMddHHmmssSSS() + "." + Suffix);
            } else {
                file1 = new File(path + "/" + fileName);
            }
            // 将上传的文件写入新建的文件中
            Mfile.transferTo(file1);
            return file1.getPath();
        } catch (Exception e) {
            throw new RuntimeException("文件异常");
        }
    }
    /**
	 * 
	 * @Title
	 * @Description 复制并重命名文件
	 * @param path
	 * @param realPath
	 * @return boolean
	 * @author huangwx
	 * @date 2017-9-18 下午6:44:59
	 */
	public static boolean createNewFile(String oldPath, String newPath) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		boolean bool = false;
		try {
			File file = new File(newPath);
			// 如果文件不存在，则创建新的文件
			if (!file.exists()) {
				file.createNewFile();
			}
			fi = new FileInputStream(oldPath);
			fo = new FileOutputStream(newPath);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			bool = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			    if (in != null) {
			        in.close();
                }
			    if (out != null) {
			        out.close();
                }
			    if (fi != null) {
			        fi.close();
			    }
			    if (fo != null) {
			        fo.close();
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return bool;
	}
	
	
	//复制指定目标目录到指定目录下
	@SuppressWarnings("static-access")
    public static boolean copyTargetDir(String oldPath, String newPath,String dirNmae) throws IOException{
		boolean flag = false;
		File file = new File(oldPath);
        String[] fileName = file.list();//获取当前目录下所有文件名及目录名
        
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        
        for (int i = 0; i < fileName.length; i++) {
            if ((new File(oldPath + file.separator + fileName[i])).isDirectory()) {
            	if((fileName[i]).equals(dirNmae)){//判断是否为目标目录文件，是则复制后，并删除当前目录
            		copyDir(oldPath  + file.separator  + fileName[i], newPath  + file.separator + fileName[i]);
            	}
            }            
        }
        flag = true;
		return flag;
	}
	
	
	
	/**
	 * 
	 * @Title
	 * @Description 复制目录中所有文件及目录到另一个目录
	 * @param oldPath
	 * @param newPath
	 * @return boolean
	 * @author meichao
	 * @date 2018-4-13  上午9:47:29
	 */
	@SuppressWarnings("static-access")
    public static boolean copyDir(String oldPath, String newPath) throws IOException{
		boolean flag = false;
		File file = new File(oldPath);
        String[] filePath = file.list();
        
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
            }
            
            if (new File(oldPath  + file.separator + filePath[i]).isFile()) {
                copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
            
        }
        flag = true;
		return flag;
	}
	
	
	//复制文件
	public static void copyFile(String oldPath, String newPath) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			File oldFile = new File(oldPath);
	        File file = new File(newPath);
	        in = new FileInputStream(oldFile);
	        out = new FileOutputStream(file);;

	        byte[] buffer=new byte[2097152];
	        
	        while((in.read(buffer)) != -1){
	            out.write(buffer);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}    
	}
	/*public static void main(String[] args) throws IOException {
		String oldPath="D://正则表达式";
		String newPath="E://";
		copyDir(oldPath, newPath);
	}*/

}
