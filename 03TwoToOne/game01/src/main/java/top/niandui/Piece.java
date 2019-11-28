package top.niandui;

/**
 * 棋子
 */
public class Piece {

    // 阵营
    private Camp camp;
    // 编号
    private int num;
    // 名称
    private String name;
    // 位置
    private int[] position = new int[2];
    // 状态，1存活，0,死亡
    private boolean status;


    public Piece(Camp camp, String name, int num, int i, int j) {
        this.camp = camp;
        this.name = name;
        this.num = num;
        position[0] = i;
        position[1] = j;
        status = true;
    }

    public void death() {
        // 棋子死亡
        status = false;
    }


    public Camp getCamp() {
        return camp;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public int[] getPosition() {
        return position;
    }

    public boolean isStatus() {
        return status;
    }
}
