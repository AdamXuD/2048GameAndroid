package xyz.paopaopaoge.game2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import xyz.paopaopaoge.Game.Game;

public class MainActivity extends AppCompatActivity {

    Game game = new Game();
    DataBaseUtils db = null;

    TextView[] tvList = new TextView[16];
    TextView tv_score = null;
    TextView tv_bestScore = null;

    int bestScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout gameViewLayout = findViewById(R.id.gameview);

        db = new DataBaseUtils(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View gameView = inflater.inflate(R.layout.game_view, null);
        gameViewLayout.addView(gameView);

        findViewsBySameIds(tvList, "tv");
        tv_score = findViewById(R.id.score_tv);
        tv_bestScore = findViewById(R.id.bestScore_tv);

        bestScore = db.queryBestScore();
        tv_bestScore.setText(String.valueOf(bestScore));

        game.setManualFunc(new MyManualFunc());
        gameView.setOnTouchListener(new GameTouchListener());
        game.start();

//        final View testView = View.inflate(this, R.layout.game_over_hint, null);
//        PopupWindow window = new PopupWindow(testView, 100, 100);
//        window.showAtLocation(testView, Gravity.CENTER, 0, 0);


    }

    class GameTouchListener implements View.OnTouchListener {

        private float pressX;
        private float pressY;
        private float moveX;
        private float moveY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                //按下
                case MotionEvent.ACTION_DOWN:
                    pressX = motionEvent.getX();
                    pressY = motionEvent.getY();
                    break;
                //移动
                case MotionEvent.ACTION_MOVE:
                    moveX = motionEvent.getX();
                    moveY = motionEvent.getY();
                    break;
                //松开
                case MotionEvent.ACTION_UP:
                    if (moveX - pressX > 0 && Math.abs(moveY - pressY) < 50) {
                        game.moveOperation(Game.Key.RIGHT);
                    } else if (moveX - pressX < 0 && Math.abs(moveY - pressY) < 50) {
                        game.moveOperation(Game.Key.LEFT);
                    } else if (moveY - pressY > 0 && Math.abs(moveX - pressX) < 50) {
                        game.moveOperation(Game.Key.DOWN);
                    } else if (moveY - pressY < 0 && Math.abs(moveX - pressX) < 50) {
                        game.moveOperation(Game.Key.UP);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    class MyManualFunc implements Game.ManualFunc {

        @Override
        public void printer() {
            Game.GameData data = game.getGameData();

            for (int row = 0; row < 4; row++)
                for (int col = 0; col < 4; col++) {

                    TextView tv = tvList[row * 4 + col];

                    if (data.map[row][col] != 0) {
                        tv.setText(String.valueOf(data.map[row][col]));
                        ((GradientDrawable) tv.getBackground()).setColor(getItemColor(data.map[row][col]));
                    } else {
                        tv.setText("");
                        ((GradientDrawable) tv.getBackground()).setColor(Color.parseColor("#EFC998"));
                    }
                }
            tv_score.setText(String.valueOf(data.score));
            if (data.score > bestScore) {
                bestScore = data.score;
                tv_bestScore.setText(String.valueOf(bestScore));
            }
        }

        @Override
        public void overHandler() {
            Toast.makeText(MainActivity.this, "gameOver!", Toast.LENGTH_LONG);
            db.insertScore(game.getGameData().score, game.getMaxNum(), game.getGameFlag().hasUndo);
        }

        void startAnimation() {
            /*暂时没啥思路*/
            //TODO 写一下动效
        }
    }


    protected <T> void findViewsBySameIds(T[] views, String samePhase) {
        for (int i = 0; i < views.length; i++) {
            String m_resid = samePhase + String.valueOf(i);
            int resID = getResources().getIdentifier(m_resid, "id", getPackageName());
            views[i] = (T) findViewById(resID);
        }
    }

    private int getItemColor(int num) {
        switch (num) {
            case 2:
                return Color.rgb(199, 237, 253);
            case 4:
                return Color.rgb(175, 215, 237);
            case 8:
                return Color.rgb(92, 167, 186);
            case 16:
                return Color.rgb(147, 224, 255);
            case 32:
                return Color.rgb(174, 221, 129);
            case 64:
                return Color.rgb(107, 194, 53);
            case 128:
                return Color.rgb(6, 126, 67);
            case 256:
                return Color.rgb(38, 157, 128);
            case 512:
                return Color.parseColor("#FF9A9E");
            case 1024:
                return Color.parseColor("#FFD0C4");
            case 2048:
                return Color.parseColor("#FFECD2");
            case 4096:
                return Color.parseColor("#FCB69F");
            case 8192:
                return Color.rgb(160, 191, 124);
            case 16384:
                return Color.rgb(101, 147, 74);
            case 32768:
                return Color.rgb(64, 116, 52);
            case 65536:
                return Color.rgb(3, 35, 14);
            default:
                return Color.rgb(255, 255, 255);
        }
    }


    /*DBG*/
    public void onUpBtnClick(View v) {
        game.moveOperation(Game.Key.UP);
    }

    public void onDownBtnClick(View v) {
        game.moveOperation(Game.Key.DOWN);
    }

    public void onLeftBtnClick(View v) {
        game.moveOperation(Game.Key.LEFT);
    }

    public void onRightBtnClick(View v) {
        game.moveOperation(Game.Key.RIGHT);
    }

    public void onRestartBtnClick(View v) {
        game.start();
    }
    /*DBG*/


}