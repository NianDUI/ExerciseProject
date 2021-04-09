package top.niandui.algorithm.array;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 做好初始定义
 * 做数组类算法问题的时候，我们常常需要定义一个变量，明确该变量的定义，并且在书写整个逻辑的时候，要不停的维护住这个变量的意义。也特别需要注意初始值和边界的问题。
 * 链接：https://leetcode-cn.com/leetbook/read/all-about-array/x9x0s5/
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/4/2 11:00
 */
public class Array01 {

    /**
     * 移动零
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * 示例:
     * 输入: [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     * 说明:
     * 必须在原数组上操作，不能拷贝额外的数组。
     * 尽量减少操作次数。
     * <p>
     * https://leetcode-cn.com/leetbook/read/all-about-array/x9rh8e/
     */
    @Test
    public void t01() {
        int[] nums = {0, 1, 0, 3, 12};

        int j = 0;
        for (int i : nums) {
            if (i != 0) {
                nums[j++] = i;
            }
        }
        while (j < nums.length) {
            nums[j++] = 0;
        }

        System.out.println(Arrays.toString(nums));
    }

    /**
     * <a href="https://leetcode-cn.com/leetbook/read/all-about-array/x9p1iv/">移除元素</a><br>
     * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     */
    @Test
    public void t02() {
        // 输入：nums = [0,1,2,2,3,0,4,2], val = 2
        // 输出：5, nums = [0,1,4,0,3]
        int[] nums = {0, 1, 2, 2, 3, 0, 4, 2};
        int val = 2;

        int i = 0;
        for (int j : nums) {
            if (j != val) {
                nums[i++] = j;
            }
        }

        System.out.println("i = " + i);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * <a href="https://leetcode-cn.com/leetbook/read/all-about-array/x9a60t/">删除排序数组中的重复项</a>
     * <br>给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
     */
    @Test
    public void t03() {
        // 输入：nums = [0,0,1,1,1,2,2,3,3,4]
        // 输出：5, nums = [0,1,2,3,4]
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};

        int l = 0, pre = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != pre) {
                pre = nums[i];
                nums[l++] = nums[i];
            }
        }

        System.out.println("l = " + l);
        System.out.println(Arrays.toString(Arrays.copyOf(nums, l)));
    }

    /**
     * <a href="https://leetcode-cn.com/leetbook/read/all-about-array/x9nivs/">删除排序数组中的重复项 II</a>
     * <br>给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 最多出现两次 ，返回删除后数组的新长度。
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     */
    @Test
    public void t04() {
        // 输入：nums = [0,0,1,1,1,1,2,3,3]
        // 输出：7, nums = [0,0,1,1,2,3,3]
        int[] nums = {0, 0, 1, 1, 1, 1, 2, 3, 3};

        int l = 0, n = 0, pre = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != pre) {
                // 不相等，更新pre和n
                pre = nums[i];
                n = 0;
            }
            if (n++ < 2) {
                // 出现2次内，长度加+，并赋值
                nums[l++] = nums[i];
            }
        }

        System.out.println("l = " + l);
        System.out.println(Arrays.toString(Arrays.copyOf(nums, l)));
    }

    /**
     * <a href=""></a>
     * <br>
     */
    @Test
    public void t() {

    }
}
