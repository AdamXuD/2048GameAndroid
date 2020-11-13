package xyz.paopaopaoge.viewFor2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import xyz.paopaopaoge.Utils.DataBaseUtils;
import xyz.paopaopaoge.game2048.R;

public class scoreAdapter extends BaseAdapter {

    private List<DataBaseUtils.ResultRow> list = null;
    Context mContext = null;
    LayoutInflater mInflater = null;

    public scoreAdapter(List<DataBaseUtils.ResultRow> resultRows, Context context){
        this.list = resultRows;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = null;

        if(convertView == null)
            view = mInflater.inflate(R.layout.achievement_listitem, null);
        else
            view = convertView;

        TextView tv_score = view.findViewById(R.id.tv_achievementScore);
        TextView tv_maxNum = view.findViewById(R.id.tv_achievementMaxNum);
//        TextView tv_uploadTime = view.findViewById(R.id.tv_achievementUploadTime);
        RadioButton rb_isUndo = view.findViewById(R.id.rb_isUndo);

        tv_score.setText(String.valueOf(list.get(position).getScore()));
        tv_maxNum.setText(String.valueOf(list.get(position).getMaxNum()));
//        tv_uploadTime.setText(String.valueOf(list.get(position).getUpLoadTime()));
        rb_isUndo.setChecked(list.get(position).isHasUndo());

        return view;
    }
}
