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

import java.util.List;

import xyz.paopaopaoge.Utils.DataBaseUtils;
import xyz.paopaopaoge.game2048.R;

public class AchievementDialog extends Dialog {

    TextView tv_ok = null;
    SquareListView lv_achievement = null;
    List<DataBaseUtils.ResultRow> results = null;
    Context mContext = null;

    public AchievementDialog(@NonNull Context context, List<DataBaseUtils.ResultRow> list) {
        super(context);
        mContext = context;
        this.results = list;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.achievement);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tv_ok = findViewById(R.id.btn_achievementOK);
        lv_achievement = findViewById(R.id.lv_achievement);
        lv_achievement.setAdapter(new scoreAdapter(results, mContext));


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
