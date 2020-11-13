package xyz.paopaopaoge.Game;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Stack;

import xyz.paopaopaoge.Utils.SerializeUtils;


public class Game{
    public interface OnGameStateChangedListener{
        public void printer();
        public void overHandler();
    }

    public static class GameData implements Cloneable, java.io.Serializable{
        public int map[][] = new int[4][4];
        public int score = 0;
        public int step = 0;

        @NonNull
        @Override
        protected Object clone() {
            GameData data = new GameData();
            data.score = this.score;
            data.step = this.step;
            for(int row = 0; row < map.length; row++)
                data.map[row] = map[row].clone();
            return data;
        }
    }

    public static class GameFlag implements java.io.Serializable{
        public boolean hasUploaded = false;
        public boolean hasUndo = false;
    }

    public enum Key {
        UP, DOWN, LEFT, RIGHT
    };

    private boolean isJustMove = false;

    private GameData data;
    private GameFlag flag;

    private Stack<GameData> history = new Stack<GameData>();

    private OnGameStateChangedListener func = null;

    private Random r;

    private void srand() {
        r = new Random();
    }

    private int rand() {
        return r.nextInt(10);
    }

    public Game() {
        srand();
    }

    public void start() {
        data = new GameData();
        flag = new GameFlag();
        history.clear();
        randNum();
        randNum();
        recordMap();
        func.printer();
    }

    public void goOn(String gameDataString, String gameFlagString) {
        try {
            data = (GameData) SerializeUtils.parseObject(gameDataString);
            flag = (GameFlag) SerializeUtils.parseObject(gameFlagString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        recordMap();
        func.printer();
    }

    public void setManualFunc(OnGameStateChangedListener object) {
        this.func = object;
    }

    public void setUploaded() {
        flag.hasUploaded = true;
    }

    public int getMaxNum() {
        int num = 0;
        for (int row = 0; row < 4; row++)
            for (int col = 0; col < 4; col++)
                if (data.map[col][row] > num)
                    num = data.map[col][row];
        return num;
    }

    public boolean isOver() {
        for (int row = 0; row < 4; row++)
            for (int col = 0; col < 4; col++)
                if (data.map[row][col] == 0)
                    return false; // 如果有为 0 的格子
        // 如果格子已满 则判断每一格能否与相邻格合并
        for (int row = 0; row < 4; row++)
            for (int col = 0; col < 4; col++)
                if (data.map[row][col] == (row == 3 ? 0 : data.map[row + 1][col])
                        || data.map[row][col] == (col == 3 ? 0 : data.map[row][col + 1]))
                    return false; // 只要存在一个可合并的未结束

        return true; // 否则即结束
    }

    public GameData getGameData() {
        return this.data;
    }

    public GameFlag getGameFlag() {
        return this.flag;
    }

    public void moveOperation(Key key) {
        switch (key) {
            case UP:
                onUpKey();
                break;
            case DOWN:
                onDownKey();
                break;
            case LEFT:
                onLeftKey();
                break;
            case RIGHT:
                onRightKey();
                break;
        }

        if (isJustMove) {
            randNum();
            isJustMove = false;
            data.step++;
            recordMap();
            func.printer();
        }

        if (isOver())
            func.overHandler();
    }

    public void undo() {
        if (!flag.hasUndo)
            flag.hasUndo = true;
        GameData tmp = history.peek();
        if (tmp.step == this.data.step) {
            if (history.size() != 1)
                history.pop();
            tmp = history.peek();
        }

        this.data = (GameData) tmp.clone();
        func.printer();
    }

    private void randNum() {
        int point[] = new int[2];
        do {
            point[0] = rand() % 4;
            point[1] = rand() % 4;
        } while (this.data.map[point[0]][point[1]] != 0);
        this.data.map[point[0]][point[1]] = rand() % 2 == 0 ? 2 : 4;
    }

    private void compact(int line[]) // 向左压缩
    {
        int count = 0;
        for (int i = 0; i < 4; i++)
            if (line[i] != 0) {
                if (count != i) {
                    line[count] = line[i];

                    this.isJustMove = true;
                }
                count++;
            }
        for (; count < 4; count++)
            line[count] = 0;
    }

    private void merge(int line[]) // 合并相同项
    {
        for (int i = 0; i < 3; i++)
            if (line[i] == line[i + 1] && line[i] != 0) {
                line[i] *= 2;
                line[i + 1] = 0;

                data.score += line[i];

                this.isJustMove = true;
            }
    }

    private void rotateMap(int count) // 旋转地图， round 圈数
    {
        for (int i = 0; i < count; i++) {
            int tmp[][] = new int[4][4];
//            tmp = this.data.map.clone();
//            System.arraycopy(this.data.map, 0, tmp, 0, 16);
            for(int row = 0; row < this.data.map.length; row++)
                    tmp[row] = this.data.map[row].clone();

            for (int row = 0; row < 4; row++)
                for (int col = 0; col < 4; col++)
                    this.data.map[col][3 - row] = tmp[row][col];
        }
    }

    private void move() {
        for (int i = 0; i < 4; i++) {
            compact(this.data.map[i]);
            merge(this.data.map[i]);
            compact(this.data.map[i]);
        }
    }

    private void onUpKey() {
        rotateMap(3);
        move();
        rotateMap(1);
    }

    private void onDownKey() {
        rotateMap(1);
        move();
        rotateMap(3);
    }

    private void onLeftKey() {
        move();
    }

    private void onRightKey() {
        rotateMap(2);
        move();
        rotateMap(2);
    }

    private void recordMap() {
        GameData tmp;
        tmp = (GameData) this.data.clone();
        history.push(tmp);
    }
}