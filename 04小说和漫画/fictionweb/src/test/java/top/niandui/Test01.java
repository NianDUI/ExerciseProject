package top.niandui;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;
import top.niandui.common.uitls.file.FTPUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Objects;

/**
 * @Title: Test01.java
 * @description: Test
 * @time: 2020/9/14 9:55
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class Test01 {
    // json处理对象
    public final static ObjectMapper json = new ObjectMapper();

    @Test
    public void md5Test() throws Exception {
        String str = "H4sIAAAAAAAAAAEoANf/6L+Z5piv5LiA5Liq5rWL6K+V5a2X56ym5LiyYWFhYmJiY2NjZGVmemcshV4oAAAA";
        System.out.println(Objects.hash(str));

        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex("".getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex("".getBytes()));
    }

    @Test
    public void jsTest() throws Exception {
        ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("Nashorn");
        String a = "1 + 2 * 3 + 4 / (1 + 1)";
        Object eval = nashorn.eval(a);
        System.out.println(eval);
    }

    @Test
    public void ftpTest() throws Exception {
//        String key = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDQzNjU3NTksInN1YiI6IntcImlwXCI6XCIyMjIuMTI4LjYuMTk0XCIsXCJhY2NvdW50TmFtZVwiOlwiYWRtaW5cIixcImFwcHVzZXJpZFwiOlwiMTVcIixcImFwcFwiOlwiU3lucW5jXCIsXCJleHBpcmVzX2luXCI6XCIxNjA0MzY5MzU5ODE5dFwifSIsImlzcyI6Ijc2ODU4Nzc4MDAiLCJhdWQiOiIzNDM1MzAwIiwiZXhwIjoxNjA0MzY5MzU5fQ.aF2dytHynEAOQsWnHte4RxiuWz5_CqV8blZHuaBRp7I";
//        System.out.println(AESUtil.aesDecrypt("wDv5TSG9dlNs5r9RrMZXyQ==", key.substring(key.length() - 16)));

        FTPClient ftp = FTPUtil.getFTPClient("192.168.1.15", 21, "test", "SynQnc@2020er");
        String f_pathbase = "/home/test/batchcmd_result/1896c405-8889-4efb-b2ef-aab2ce39704f";
        String resultZipPath = "D:zzz";
        FTPUtil.download(ftp, f_pathbase, resultZipPath);

    }
}
