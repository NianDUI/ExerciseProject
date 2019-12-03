package top.niandui.tree1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        List<TreeObject> list = new ArrayList<>();
        list.add(new TreeObject("1", "0"));
        list.add(new TreeObject("2", "1"));
        list.add(new TreeObject("3", "1"));
        list.add(new TreeObject("4", "2"));
        list.add(new TreeObject("5", "3"));
        list.add(new TreeObject("6", "3"));
        TreeObject t = new TreeObject();
        List<TreeObject> childTreeObjects = TreeUtil.getChildTree(list, BaseTree.getMinId(list));
        System.out.println(childTreeObjects);

        System.out.println(System.currentTimeMillis() - l);


    }

}


