package top.niandui.common.expection;

/**
 * Token失效错误
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/4/12 13:43
 */
public class TokenInvaLIDException extends RuntimeException {
    public TokenInvaLIDException() {
    }

    public TokenInvaLIDException(String message) {
        super(message);
    }

    public TokenInvaLIDException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInvaLIDException(Throwable cause) {
        super(cause);
    }

    public TokenInvaLIDException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
