package top.niandui;

/**
 * 阵营
 */
public enum Camp {
    red("\033[1;31m红方\033[0m"), blue("\033[1;34m蓝方\033[0m");

    private final String value;

    // 构造函数，枚举类型只能为私有
    private Camp(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
