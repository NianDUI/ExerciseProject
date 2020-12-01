package top.niandui.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 树类基接口
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/30 14:33
 */
public interface IBaseTree<R, T extends IBaseTree<R, T>> extends Serializable {
    long serialVersionUID = 1L;

    @JsonIgnore
    R getTreeId();

    @JsonIgnore
    R getTreeParentId();

    List<T> getChildren();

    void setChildren(List<T> children);
}
