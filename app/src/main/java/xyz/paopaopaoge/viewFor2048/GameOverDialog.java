package xyz.paopaopaoge.viewFor2048;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import xyz.paopaopaoge.game2048.R;


public class GameOverDialog extends Dialog {

    public interface OnBtnClickedListener {
        void onCancelBtnClicked();
        void onRestartBtnClicked();
    }

    private TextView tv_restart;
    private TextView tv_cancel;
    private TextView tv_gameOverScore;

    private OnBtnClickedListener listener = null;

    int score = 0;

    public GameOverDialog(@NonNull Context context, OnBtnClickedListener onBtnClickedListener, int s) {
        super(context);
        this.listener = onBtnClickedListener;
        this.score = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over_hint);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tv_restart = findViewById(R.id.tv_restart);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_gameOverScore = findViewById(R.id.tv_gameoverscore);

        tv_gameOverScore.setText(String.format("You get %d scores!", score));

        tv_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRestartBtnClicked();
                dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancelBtnClicked();
                dismiss();
            }
        });
    }
}

