package top.niandui.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public interface IBaseTree<R, T extends IBaseTree<R, T>> extends Serializable {
    long serialVersionUID = 1L;
    @JsonIgnore
    R getTreeId();
    @JsonIgnore
    R getTreeParentId();
    void setChildren(List<T> children);
    List<T> getChildren();
}
