package xyz.paopaopaoge.viewFor2048;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SquareListView extends ListView {
    public SquareListView(Context context) {
        super(context);
    }

    public SquareListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();

        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
