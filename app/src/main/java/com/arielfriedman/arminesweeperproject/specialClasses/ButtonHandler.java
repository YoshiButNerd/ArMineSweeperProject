package com.arielfriedman.arminesweeperproject.specialClasses;

import android.view.MotionEvent;
import android.view.View;

public final class ButtonHandler {
    public static void HandleButton(View view) {

        //remove sfx
        view.setSoundEffectsEnabled(false);

        //add press animation
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                } else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                }
                return false;
            }
        });
    }
}
