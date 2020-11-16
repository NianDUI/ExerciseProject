package top.niandui.common.expection;

/**
 * Token检查错误
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/9 13:39
 */
public class TokenCheckException extends RuntimeException {

    public TokenCheckException() {
    }

    public TokenCheckException(String message) {
        super(message);
    }

    public TokenCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenCheckException(Throwable cause) {
        super(cause);
    }

    public TokenCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
