package top.niandui.utils;

/**
 * @description 统一状态码
 * @author huangwx
 * @date 2019/11/7 11:51
 */
public class StatusCode {

	// 成功状态码
	public static final int EXECUTE_SUCCESS = 0;
	// 失败状态码
	public static final int EXECUTE_FAIL = 1;

	// 成功状态码
	public static final int HTTP_SUCCESS = 200;
	// TOKEN失效
	public static final int HTTP_TOKEN_INVALID = 401;
	// TOKEN不合法
	public static final int HTTP_TOKEN_ERROR = 402;
	// 请求被拒绝。附带JSON格式原因。
	public static final int HTTP_DATA_ERROR = 403;

	// 系统异常。
	public static final int HTTP_SERVER_ERROR = 500;

	// 重名状态码
	public static final int RENAME = 1001;
	// 参数格式错误码
	public static final int DATAERROR = 1002;
    // 格式错误码
    public static final int FORMAT = 1004;
	// 状态错误码
    public static final int RESTATE = 1005;
	
	// RSA解密失败
	public static final int DECRYPTION_FAILURE = 2003;
	// 账号被锁定
	public static final int ACCOUNT_LOCKED = 2004;
	// 多次登录失败，锁定十分钟
	public static final int LOGINFAILS_ACCOUNT_LOCKED = 2005;
	// 用户名密码错误
	public static final int USERNAME_PASSWORD_ERROR = 2006;
	// 非法访问
	public static final int ILLEGAL_ACCESS_ERROR = 2007;
	// 没有该权限
	public static final int UNAUTHORIZED_ACCESS_ERROR = 2008;
	// 账号被异地登录
	public static final int REMOTE_LOGIN_ERROR = 2009;
	// 用户已被删除标识
	public static final int USERACOUNT_DELETE = 2011;
	// 用户电脑和登录账号不匹配
	public static final int USECP_ACCOUNT_MISMATCHING = 2012;
	// 存在子集不能删除
	public static final int HAVECHILD = 2013;
	
	// 旧密码输入不正确
	public static final int OLDPASSWORDEEROOR = 2014;
}
