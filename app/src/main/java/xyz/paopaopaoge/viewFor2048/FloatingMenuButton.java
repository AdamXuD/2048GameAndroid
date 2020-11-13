package xyz.paopaopaoge.viewFor2048;

import android.content.Context;
import android.util.AttributeSet;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FloatingMenuButton extends FloatingActionButton {

    private ArrayList<FloatingActionButton> btnList = new ArrayList<FloatingActionButton>();

    public FloatingMenuButton(Context context) {
        super(context);
    }

    public FloatingMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingMenuButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ArrayList<FloatingActionButton> getBtnList() {
        return btnList;
    }

    public void addBtn(FloatingActionButton btn){
        btnList.add(btn);
    }
}
