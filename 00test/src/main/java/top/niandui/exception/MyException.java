package top.niandui.exception;

/**
 * 自定义错误类
 */
public class MyException extends Exception {
    public MyException() {
        super("自定义错误类");
    }

    public MyException(String message) {
        super(message);
    }
}
