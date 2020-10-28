package top.niandui.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Title: Algorithm01.java
 * @description: 算法01：二分搜索、杨辉三角
 * @time: 2020/10/27 10:12
 * @author: liyongda
 * @version: 1.0
 */
public class Algorithm01 {

    @Test
    public void binarySearch() {
        // 二分搜索，有序
        int[] a = new int[50];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        int l = 0, p = a.length;
        int m, t = 20, r = -1;
//        a[t] = t - 1;
        while (l <= p) {
            m = (l + p) / 2;
            if (t > a[m]) {
                l = m + 1;
            } else if (t < a[m]) {
                p = m - 1;
            } else {
                r = m;
                break;
            }
        }
        System.out.println("r = " + r);
        System.out.println(Arrays.binarySearch(a, t));
    }

    @Test
    public void yangHuiSanJiao() {
        // 杨辉三角
        int l = 6;
        if (l < 1) {
            return;
        }
        int[][] a = new int[l][];
        a[0] = new int[]{1};
        for (int i = 1; i < l; i++) {
            int[] b = new int[i + 1];
            b[0] = b[i] = 1;
            for (int j = 1; j < i; j++) {
                b[j] = a[i - 1][j - 1] + a[i - 1][j];
            }
            a[i] = b;
        }
        StringBuilder sb = new StringBuilder();
        for (int[] b : a) {
            sb.delete(0, sb.length());
            for (int i : b) {
                sb.append(i).append("\t");
            }
            System.out.println(sb.toString().trim());
        }
    }
}
