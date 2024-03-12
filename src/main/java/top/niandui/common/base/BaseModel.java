package top.niandui.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * model基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 16:22
 */
@Data
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
}