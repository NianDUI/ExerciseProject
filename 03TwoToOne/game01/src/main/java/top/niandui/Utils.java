package top.niandui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 工具
 */
public class Utils {

    public static void run() {
        while (true) {
            showSite();
            operate();
        }
    }

    private static final Scanner sc = new Scanner(System.in);

    private static String readLine() {
        return sc.nextLine();
    }

    // 展示棋盘
    private static void showSite() {
        StringBuilder sb = new StringBuilder("\033[2J");
        for (int i = 0; i < Data.getSite().length; i++) {
            sb.append("\t");
            for (int j = 0; j < Data.getSite()[i].length; j++) {
                if (j > 0) {
                    sb.append(" - ");
                }
                if (Data.getSite()[i][j] == null) {
                    sb.append("+");
                } else {
                    sb.append(Data.getSite()[i][j].getName());
                }
            }
            sb.append("\n");
        }
        /*int j = 0;
        for (Piece[] row : Data.getSite()) {
            for (Piece col : row) {
                j++;
                if (col == null) {
                    sb.append("+");
                } else {
                    sb.append(col.getName());
                }
                if (j < 4) {
                    sb.append(" - ");
                }
            }
            sb.append("\n");
            j = 0;
        }*/
        System.out.println(sb);
    }

    // 步数
    private static long step = 0;

    // 操作棋子
    private static void operate() {
        step++;
        Camp camp = null;
        if (step % 2 == Camp.red.ordinal()) {
            camp = Camp.red;
        } else {
            camp = Camp.blue;
        }
        // 选择棋子
        Piece piece = selectPiece(camp);
        // 移动棋子
        movePiece(piece);
        // 检测棋子死亡
        detectPieceDeath(piece);

//        System.exit(0);
        // 检测状态
        detectionStatus();
    }

    // 检测状态
    private static void detectionStatus() {
        Camp camp = Data.detectionStatus();
        if (camp != null) {
            showSite();
            StringBuilder sb = new StringBuilder();
            sb.append((camp == Camp.red ? Camp.blue : Camp.red).getValue() + "，胜！\n");
            sb.append(camp.getValue() + "，输！\n");
            sb.append("共进行移动 " + step + " 次。\n");
            System.out.println(sb);
            System.exit(0);
        }
    }

    /**
     * 选择棋子
     * @param camp 阵营
     * @return 选中的棋子
     */
    public static Piece selectPiece(Camp camp) {
        StringBuilder sb = new StringBuilder();
        List<Piece> pieceList = camp == Camp.red ? Data.getRed() : Data.getBlue();
        /**
         * 选择棋子
         */
        sb.append(camp.getValue()).append("：\n请选择要移动的棋子(");
        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i).isStatus()) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(pieceList.get(i).getName());
            }
        }
        sb.append(")：");
        String out = sb.toString();
        while (true) {
            try {
                System.out.print(out);
                int num = Integer.parseInt(readLine());
                if (num > 0 && num <= 4) {
                    num--;
                    if (pieceList.get(num).isStatus()) {
                        // 选中棋子
                        return pieceList.get(num);
                    } else {
                        System.out.println("\033[1;31m请选择存活的棋子！\033[0m");
                    }
                } else {
                    System.out.println("\033[1;31m输入超出范围！\033[0m");
                }
            } catch (Exception e) {}
        }
    }

    /**
     * 移动棋子
     * @param piece 要移动的棋子
     */
    public static void movePiece(Piece piece) {
        StringBuilder sb = new StringBuilder();
        /**
         * 移动棋子
         */
        sb.append("请选择要移动的方向(");
        Piece[][] site = Data.getSite();
        int[] position = piece.getPosition();
        // 棋子可以移动的方向：0:上、1:下、2:左、3:右
        boolean[] isMove = new boolean[4];
        if (position[0] - 1 >= 0 && site[position[0] - 1][position[1]] == null) {
            sb.append("1:↑");
            isMove[0] = true;
        }
        if (position[0] + 1 < site.length && site[position[0] + 1][position[1]] == null) {
            if (isMove[0]) {
                sb.append(",");
            }
            sb.append("2:↓");
            isMove[1] = true;
        }
        if (position[1] - 1 >= 0 && site[position[0]][position[1] - 1] == null) {
            if (isMove[1]) {
                sb.append(",");
            }
            sb.append("3:←");
            isMove[2] = true;
        }
        if (position[1] + 1 < site[position[0]].length && site[position[0]][position[1] + 1] == null) {
            if (isMove[2]) {
                sb.append(",");
            }
            sb.append("4:→");
            isMove[3] = true;
        }
        sb.append(")：");
        String out = sb.toString();
        while (true) {
            try {
                System.out.print(out);
                int direction = Integer.parseInt(readLine());
                if (direction > 0 && direction <= 4) {
                    direction--;
                    if (isMove[direction]) {
                        // 移动棋子
                        Data.movePiece(piece, direction);
                        break;
                    } else {
                        System.out.println("\033[1;31m请选择可以移动的方向！\033[0m");
                    }
                } else {
                    System.out.println("\033[1;31m输入超出范围！\033[0m");
                }
            } catch (Exception e) {}
        }
    }


    /**
     * 检测棋子死亡，默认也检测临近的友方棋子
     * @param piecee
     */
    public static void detectPieceDeath(Piece piecee) {
        detectPieceDeath(piecee, true);
    }

    /**
     * 检测棋子死亡 Detect death
     * @param piece 要围绕检测的棋子
     * @param isDetectTeammate 是否要检测临近的友方棋子
     */
    public static void detectPieceDeath(Piece piece, boolean isDetectTeammate) {
        StringBuilder sb = new StringBuilder();
        Piece[][] site = Data.getSite();
        int[] position = piece.getPosition();
        Camp camp = piece.getCamp();
        /**
         * 检测是否有棋子死亡
         */
        // 棋子的环境，方向：0:上、1:下、2:左、3:右，无：null、友方：true、敌方：false
        Boolean[] pieceMilieu = new Boolean[4];
        if (position[0] - 1 >= 0 && site[position[0] - 1][position[1]] != null) {
            // 1:↑
            if (site[position[0] - 1][position[1]].getCamp() == camp) {
                pieceMilieu[0] = true;
            } else {
                pieceMilieu[0] = false;
            }
        }
        if (position[0] + 1 < site.length && site[position[0] + 1][position[1]] != null) {
            // 2:↓
            if (site[position[0] + 1][position[1]].getCamp() == camp) {
                pieceMilieu[1] = true;
            } else {
                pieceMilieu[1] = false;
            }
        }
        if (position[1] - 1 >= 0 && site[position[0]][position[1] - 1] != null) {
            // 3:←
            if (site[position[0]][position[1] - 1].getCamp() == camp) {
                pieceMilieu[2] = true;
            } else {
                pieceMilieu[2] = false;
            }
        }
        if (position[1] + 1 < site[position[0]].length && site[position[0]][position[1] + 1] != null) {
            // 4:→
            if (site[position[0]][position[1] + 1].getCamp() == camp) {
                pieceMilieu[3] = true;
            } else {
                pieceMilieu[3] = false;
            }
        }

        // 是否先对临近友放的棋子进行检测
        if (isDetectTeammate) {
            for (int i = 0; i < pieceMilieu.length; i++) {
                if (pieceMilieu[i] != null && pieceMilieu[i]) {
                    Piece onePiece = null;
                    switch (i) {
                        case 0:
                            onePiece = site[position[0] - 1][position[1]];
                            break;
                        case 1:
                            onePiece = site[position[0] + 1][position[1]];
                            break;
                        case 2:
                            onePiece = site[position[0]][position[1] - 1];
                            break;
                        case 3:
                            onePiece = site[position[0]][position[1] + 1];
                            break;
                    }
                    detectPieceDeath(onePiece, false);
                }
            }
        }

        // 进一步检测的方向Further detection direction
        int fdd = -1;
        if (pieceMilieu[0] != null && pieceMilieu[1] != null && pieceMilieu[0] != pieceMilieu[1]) {
            // 上下
            if (pieceMilieu[0] == false) {
                fdd = 0;
            } else {
                fdd = 1;
            }
        }
        if (pieceMilieu[2] != null && pieceMilieu[3] != null && pieceMilieu[2] != pieceMilieu[3]) {
            // 左右
            if (pieceMilieu[2] == false) {
                fdd = 2;
            } else {
                fdd = 3;
            }
        }
        // 进一步检测，该方向上是否还有第二个敌方棋子
        if (fdd >= 0) {
            // 该方向上的第一个敌方棋子
            Piece onePiece = null;
            // 该方向上的第二个棋子
            Piece secondPiece = null;
            switch (fdd) {
                case 0:
                    onePiece = site[position[0] - 1][position[1]];
                    if (position[0] - 2 >= 0) {
                        secondPiece = site[position[0] - 2][position[1]];
                    }
                    break;
                case 1:
                    onePiece = site[position[0] + 1][position[1]];
                    if (position[0] + 2 < site.length) {
                        secondPiece = site[position[0] + 2][position[1]];
                    }
                    break;
                case 2:
                    onePiece = site[position[0]][position[1] - 1];
                    if (position[1] - 2 >= 0) {
                        secondPiece = site[position[0]][position[1] - 2];
                    }
                    break;
                case 3:
                    onePiece = site[position[0]][position[1] + 1];
                    if (position[1] + 2 < site[position[0]].length) {
                        secondPiece = site[position[0]][position[1] + 2];
                    }
                    break;
            }
            // 第二个棋子：null或者是友方：敌方棋子会死亡，否则不会死亡
            if (secondPiece == null || secondPiece.getCamp() == camp) {
                Data.poeceDeath(onePiece);
                sb.append(onePiece.getCamp().getValue()).append(" ").append(onePiece.getName()).append(" 棋子死亡！");
            }
        }
//        System.out.println(Arrays.toString(pieceMilieu));
        System.out.println(sb);
    }

}
