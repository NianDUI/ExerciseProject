package top.niandui.tree3;

import java.io.Serializable;
import java.util.List;

public interface BaseTree<R, T extends BaseTree<R, T>> extends Serializable {
    long serialVersionUID = 1L;
    R getTreeId();
    R getTreeParentId();
    void setChildren(List<T> children);
    List<T> getChildren();
}
