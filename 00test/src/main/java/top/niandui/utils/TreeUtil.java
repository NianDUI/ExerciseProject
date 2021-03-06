//package top.niandui.top.niandui.utils;
//
//import com.synqnc.common.base.IBaseTree;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * 把一个list集合,里面的bean含有 id, parentId, children 转为树形式
// */
//public class TreeUtil {
//	/**
//	 * 根据父节点的ID获取所有子节点
//	 * @param list 节点列表
//	 * @param praentId 传入的父节点ID
//	 * @return String
//	 */
//	public static <R, T extends IBaseTree<R, T>> List<T> getChildTreeObjects(List<T> list, R praentId) {
//		T t = (T) new IBaseTree<R, T>() {
//			private List<T> children = new ArrayList<T>();
//
//			@Override
//			public R getTreeId() {
//				return praentId;
//			}
//
//			@Override
//			public R getTreeParentId() {
//				return null;
//			}
//
//			@Override
//			public void setChildren(List<T> children) {
//				this.children = children;
//			}
//
//			@Override
//			public List<T> getChildren() {
//				return children;
//			}
//		};
//        // 递归所有节点
//        recursionFn(list, t);
//        return t.getChildren();
//	}
//
//	/**
//	 * 递归列表
//	 */
//	private static <R, T extends IBaseTree<R, T>> void recursionFn(List<T> list, T t) {
//		List<T> childList = getChildList(list, t);// 得到子节点列表
//		t.setChildren(childList);
//		for (T tChild : childList) {
//			if (hasChild(list, tChild)) {// 判断是否有子节点
//				//returnList.add(TreeObject);
//				Iterator<T> it = childList.iterator();
//				while (it.hasNext()) {
//					T n = it.next();
//					recursionFn(list, n);
//				}
//			}
//		}
//	}
//
//	// 得到子节点列表
//	private static <R, T extends IBaseTree<R, T>> List<T> getChildList(List<T> list, T t) {
//		List<T> tlist = new ArrayList<T>();
//		Iterator<T> it = list.iterator();
//		while (it.hasNext()) {
//			T n = it.next();
//			if (n.getTreeParentId().equals(t.getTreeId())) {
//				tlist.add(n);
//				// 匹配上则去除,优化性能
//				it.remove();
//			}
//		}
//		return tlist;
//	}
//	// 判断是否有子节点
//	private static <R, T extends IBaseTree<R, T>> boolean hasChild(List<T> list, T t) {
////		return getChildList(list, t).size() > 0 ? true : false;
//        Iterator<T> it = list.iterator();
//        while (it.hasNext()) {
//            T n = it.next();
//            if (n.getTreeParentId().equals(t.getTreeId())) {
//                return true;
//            }
//        }
//        return false;
//	}
//}
