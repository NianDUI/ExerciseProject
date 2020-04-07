package top.niandui.common.expection;

/**
 * @Title: ReStateException.java
 * @description: 状态错误
 * @time: 2020/4/7 16:25
 * @author: liyongda
 * @version: 1.0
 */
public class ReStateException extends RuntimeException {

    public ReStateException() {
    }

    public ReStateException(String message) {
        super(message);
    }

    public ReStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReStateException(Throwable cause) {
        super(cause);
    }

    public ReStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
