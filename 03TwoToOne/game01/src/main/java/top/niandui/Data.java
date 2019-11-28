package top.niandui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据
 */
public class Data {

    private Data() {}

    // 棋盘
    private static Piece[][] site;
    // 红方棋子
    private static List<Piece> red;
    // 蓝方棋子
    private static List<Piece> blue;
    // 红方棋子存活数量
    private static int redAmount;
    // 蓝方棋子存活数量
    private static int blueAmount;

    // 数据初始化
    static {
        site = new Piece[4][4];
        red = new ArrayList<>(site[0].length);
        blue = new ArrayList<>(site[site.length - 1].length);
        for (int j = 0; j < site[0].length; j++) {
            site[0][j] = new Piece(Camp.red, "\033[0;31m" + (j + 1) + "\033[0m", j, 0, j);
            site[site.length - 1][j] = new Piece(Camp.blue, "\033[0;34m" + (j + 1) + "\033[0m",j, site.length - 1, j);
            red.add(site[0][j]);
            blue.add(site[site.length - 1][j]);
        }
        redAmount = 4;
        blueAmount = 4;
    }

    // 检测状态
    public static Camp detectionStatus() {
        Camp camp = null;
        if (redAmount <= 1) {
            camp = Camp.red;
        }
        if (blueAmount <= 1) {
            camp = Camp.blue;
        }
        return camp;
    }

    // 移动棋子
    public static void movePiece(Piece piece, int direction) {
        int[] position = piece.getPosition();
        switch (direction) {
            case 0:
                site[position[0] - 1][position[1]] = site[position[0]][position[1]];
                site[position[0]][position[1]] = null;
                position[0] = position[0] - 1;
                break;
            case 1:
                site[position[0] + 1][position[1]] = site[position[0]][position[1]];
                site[position[0]][position[1]] = null;
                position[0] = position[0] + 1;
                break;
            case 2:
                site[position[0]][position[1] - 1] = site[position[0]][position[1]];
                site[position[0]][position[1]] = null;
                position[1] = position[1] - 1;
                break;
            case 3:
                site[position[0]][position[1] + 1] = site[position[0]][position[1]];
                site[position[0]][position[1]] = null;
                position[1] = position[1] + 1;
                break;
        }
    }

    // 棋子死亡
    public static void poeceDeath(Piece piece) {
        int[] position = piece.getPosition();
        site[position[0]][position[1]] = null;
        // 棋子死亡
        piece.death();
        if (piece.getCamp() == Camp.red) {
            redAmount--;
        } else {
            blueAmount--;
        }
    }

    public static Piece[][] getSite() {
        return site;
    }

    public static List<Piece> getRed() {
        return red;
    }

    public static List<Piece> getBlue() {
        return blue;
    }

    public static int getRedAmount() {
        return redAmount;
    }

    public static int getBlueAmount() {
        return blueAmount;
    }
}
