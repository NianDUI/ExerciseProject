package top.niandui.tree1;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @param <R> id和父id的类型
 * @param <T> 子类的类型
 *           例：public class TreeObject extends BaseTree<Long, TreeObject>{}
 */
public abstract class BaseTree<R, T extends BaseTree> {
    private Supplier<R> getIdMethod = null;

    private Supplier<R> getParentIdMethod = null;

    private Supplier<List<T>> getChildrenMethod = null;

    private Consumer<List<T>> setChildrenMethod = null;

    public BaseTree() {
        setting();
    }

    /**
     * 设置方法，四个属性必须设置
     * 例：
     * public void setting() {
     * 		setGetIdMethod(this::getId);
     * 		setGetParentIdMethod(this::getParentId);
     * 		setGetChildrenMethod(this::getChildren);
     * 		setSetChildrenMethod(this::setChildren);
     * }
     */
    public abstract void setting();

    // 获取最小的父id
    public static <R, T extends BaseTree<R, T>> R getMinId(List<T> list) {
        Optional<T> min = list.stream().min((o1, o2) ->
             (int) (Long.valueOf(o1.getTreeParentId().toString()) - Long.valueOf(o2.getTreeParentId().toString()))
        );
        return min.get().getTreeParentId();
    }


    // 获取相应属性的数据
    public R getTreeId() {
        return getIdMethod.get();
    }

    public R getTreeParentId() {
        return getParentIdMethod.get();
    }

    public List<T> getTreeChildren() {
        return getChildrenMethod.get();
    }

    public void setTreeChildren(List<T> children) {
        setChildrenMethod.accept(children);
    }



    // 设置获取响应属性方法的方法
    protected void setGetIdMethod(Supplier<R> getIdMethod) {
        this.getIdMethod = getIdMethod;
    }

    protected void setGetParentIdMethod(Supplier<R> getParentIdMethod) {
        this.getParentIdMethod = getParentIdMethod;
    }

    protected void setGetChildrenMethod(Supplier<List<T>> getChildrenMethod) {
        this.getChildrenMethod = getChildrenMethod;
    }

    protected void setSetChildrenMethod(Consumer<List<T>> setChildrenMethod) {
        this.setChildrenMethod = setChildrenMethod;
    }
}
