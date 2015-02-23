package com.project.malina.myday.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class SpecialButton extends Button {

    public SpecialButton(Context context) {
        super(context);
    }

    public SpecialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }

}
