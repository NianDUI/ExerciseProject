package top.niandui.common.uitls;

import top.niandui.common.base.IBaseTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 把一个List<? extend IBaseTree>集合转为树形式
 */
public class TreeUtil {

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     节点列表
     * @param praentId 传入的根节点ID
     * @param <R>      节点id和父id的类型
     * @param <T>      节点类型(IBaseTree的实现类)
     * @return 树形处理后的列表
     */
    public static <R, T extends IBaseTree<R, T>> List<T> getChildTreeObjects(List<T> list, R praentId) {
        T t = (T) new IBaseTree<R, T>() {
            private List<T> children;

            @Override
            public R getTreeId() {
                return praentId;
            }

            @Override
            public R getTreeParentId() {
                return null;
            }

            @Override
            public void setChildren(List<T> children) {
                this.children = children;
            }

            @Override
            public List<T> getChildren() {
                return children;
            }
        };
        // 递归所有节点
        recursionFn(list, t);
        return t.getChildren();
    }

    /**
     * 递归节点列表
     *
     * @param list 节点列表
     * @param t    节点
     * @param <R>  节点id和父id的类型
     * @param <T>  节点类型(IBaseTree的实现类)
     */
    private static <R, T extends IBaseTree<R, T>> void recursionFn(List<T> list, T t) {
        // 获取指定节点的子节点列表
        List<T> childList = getChildList(list, t);
        t.setChildren(childList);
        for (T tChild : childList) {
            // 判断指定节点是否有子节点
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 获取指定节点的子节点列表
     *
     * @param list 节点列表
     * @param t    节点
     * @param <R>  节点id和父id的类型
     * @param <T>  节点类型(IBaseTree的实现类)
     * @return 子节点列表
     */
    private static <R, T extends IBaseTree<R, T>> List<T> getChildList(List<T> list, T t) {
        List<T> tlist = new ArrayList<T>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T n = it.next();
            if (Objects.equals(n.getTreeParentId(), t.getTreeId())) {
                tlist.add(n);
                // 匹配上则去除,优化性能
                it.remove();
            }
        }
        return tlist;
    }

    /**
     * 判断指定节点是否有子节点
     *
     * @param list 节点列表
     * @param t    节点
     * @param <R>  节点id和父id的类型
     * @param <T>  节点类型(IBaseTree的实现类)
     * @return true有、false无
     */
    private static <R, T extends IBaseTree<R, T>> boolean hasChild(List<T> list, T t) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T n = it.next();
            if (Objects.equals(n.getTreeParentId(), t.getTreeId())) {
                return true;
            }
        }
        return false;
    }
}
