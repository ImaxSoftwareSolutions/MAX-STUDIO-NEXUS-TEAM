package com.besome.sketch.lib.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.TextView;

import androidx.core.view.WindowCompat;


import mod.hey.studios.util.Helper;
import pro.sketchware.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.progress);
        setContentView(R.layout.progress);
        TextView tvProgress = findViewById(R.id.tv_progress);
        tvProgress.setText(Helper.getResString(R.string.common_message_loading));
        super.setCancelable(false);

        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            WindowCompat.setDecorFitsSystemWindows(window, false);
        }
    }

    // Kept for API compatibility: the circular indicator needs no manual control.
    public void cancelAnimation() {
    }

    public void pauseAnimation() {
    }

    public void resumeAnimation() {
    }

}
