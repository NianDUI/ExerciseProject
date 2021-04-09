package top.niandui.algorithm.array;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 运用基础算法思想
 * 典型的排序算法思想、二分查找思想在解 LeetCode 题目时很有用。
 * 链接：https://leetcode-cn.com/leetbook/read/all-about-array/x95vg6/
 *
 * @author liyongda
 * @version 1.0
 * @date 2021年4月6日11:45:42
 */
public class Array02 {

    /**
     * <a href="https://leetcode-cn.com/leetbook/read/all-about-array/x9wv2h/">颜色分类</a>
     * <br>给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
     * 此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
     * <br>双指针：left 指针指向数组的开始；right 指针指向数组的结尾。
     * <br>&nbsp;若 index 位置上的元素值为 0，则说明是红色，要放在最前面，即和 left 指针指向位置上的元素进行交换；
     * <br>&nbsp;若 index 位置上的元素值为 1，则说明是白色（本来就是要放在中间）不需要进行交换，直接将 index 指针后移；
     * <br>&nbsp;若 index 位置上的元素值为 2，则说明是蓝色，要放在最后面，即和 right 指针指向位置上的元素进行交换。
     * <br>注意：index 指针要始终和 left 指针指向同一位置或指向 left 指向位置的后面的位置（因为 left 指针之前的元素都是已经交换好的了），并且 index 指针不能超过 right 指针（因为 right 指针后面的元素也都是已经交换好的了）。
     */
    @Test
    public void t01() {
        // 输入：nums = [2,0,2,1,1,0]
        // 输出：[0,0,1,1,2,2]
        int[] nums = {2, 0, 2, 1, 1, 0};
//        int[] nums = {1, 2, 0};
//        int[] nums = {0, 2, 1};

        /*// 选择排序
        for (int i = 0; i < nums.length - 1; i++) {
            // i到最后一次不需要执行
            for (int j = i + 1; j < nums.length; j++) {
                // 将小的放再i位置
                if (nums[j] < nums[i]) {
                    // j值 < i值，交换
                    int t = nums[j];
                    nums[j] = nums[i];
                    nums[i] = t;
                }
            }
        }*/

        for (int i = 0, l = 0, h = nums.length - 1; i <= h; ) {
            if (i < l) {
                i = l + 1;
            } else if (nums[i] == 0) {
                //  i和l位置不相等时进行加交换
                if (nums[i] != nums[l]) {
                    int t = nums[l];
                    nums[l] = nums[i];
                    nums[i] = t;
                }
                l++;
            } else if (nums[i] == 2) {
                //  i和h位置不相等时进行加交换
                if (nums[i] != nums[h]) {
                    int t = nums[h];
                    nums[h] = nums[i];
                    nums[i] = t;
                }
                h--;
            } else {
                i++;
            }
        }

        System.out.println(Arrays.toString(nums));
    }

    /**
     * <a href="https://leetcode-cn.com/leetbook/read/all-about-array/x90rpm/">数组中的第K个最大元素</a>
     * <br>在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * <br>相关标签:堆、分治算法
     */
    @Test
    public void t02() {
        // 输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
        // 输出: 4
        int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k = 4;

        int[] temp = new int[k];
        for (int i = 0; i < k; i++) {
            temp[i] = nums[i];
        }
        minRoot(temp);

        System.out.println(Arrays.toString(temp));
    }

    // 最小根
    public void minRoot(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[0] > nums[i]) {
                swap(nums, 0, i);
            }
        }
    }

    public void swap(int[] nums, int i, int j) {
        int t = nums[j];
        nums[j] = nums[i];
        nums[i] = t;
    }

    /**
     * <a href=""></a>
     * <br>
     */
    @Test
    public void t() {
        // 输入：nums = [0,0,1,1,1,1,2,3,3]
        // 输出：7, nums = [0,0,1,1,2,3,3]
        int[] nums = {0, 0, 1, 1, 1, 1, 2, 3, 3};

        int l = 0;

        System.out.println("l = " + l);
        System.out.println(Arrays.toString(Arrays.copyOf(nums, l)));
    }
}
