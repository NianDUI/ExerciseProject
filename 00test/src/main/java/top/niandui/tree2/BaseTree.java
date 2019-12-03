package top.niandui.tree2;

import java.io.Serializable;
import java.util.List;

public abstract class BaseTree<R, T extends BaseTree<R, T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    abstract R getTreeId();
    abstract R getTreeParentId();
    abstract void setChildren(List<T> children);
    abstract List<T> getChildren();
}
