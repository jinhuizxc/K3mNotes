package com.k3mshiro.k3mnotes.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.customview.SquareButton;

public class ColorSetDialog extends Dialog implements View.OnClickListener {
    private SquareButton btnRed, btnOrange, btnYellow, btnGreen, btnBlue, btnIndigo, btnViolet;
    private OnCallBack mColorListener;
    private String parseColor = "#4CAF50";
    private int resId = R.drawable.green_circle_bg;

    public interface OnCallBack {
        void setColor(String parseColor);

        void setDrawableResource(int resId);
    }

    public void setmColorListener(OnCallBack mListener) {
        this.mColorListener = mListener;
    }

    public ColorSetDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_bar_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initViews();
    }

    private void initViews() {
        btnRed = (SquareButton) findViewById(R.id.btn_red);
        btnOrange = (SquareButton) findViewById(R.id.btn_orange);
        btnYellow = (SquareButton) findViewById(R.id.btn_yellow);
        btnGreen = (SquareButton) findViewById(R.id.btn_green);
        btnBlue = (SquareButton) findViewById(R.id.btn_blue);
        btnIndigo = (SquareButton) findViewById(R.id.btn_indigo);
        btnViolet = (SquareButton) findViewById(R.id.btn_violet);

        btnRed.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnIndigo.setOnClickListener(this);
        btnViolet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_red:
                parseColor = "#F44336";
                resId = R.drawable.red_circle_bg;
                break;

            case R.id.btn_orange:
                parseColor = "#FB8C00";
                resId = R.drawable.orange_circle_bg;
                break;

            case R.id.btn_yellow:
                parseColor = "#FFEA00";
                resId = R.drawable.yellow_circle_bg;
                break;

            case R.id.btn_green:
                parseColor = "#4CAF50";
                resId = R.drawable.green_circle_bg;
                break;

            case R.id.btn_blue:
                parseColor = "#2196F3";
                resId = R.drawable.blue_circle_bg;
                break;

            case R.id.btn_indigo:
                parseColor = "#3F51B5";
                resId = R.drawable.indigo_circle_bg;
                break;

            case R.id.btn_violet:
                parseColor = "#9C27B0";
                resId = R.drawable.violet_circle_bg;
                break;

            default:
                break;
        }

        mColorListener.setColor(parseColor);
        mColorListener.setDrawableResource(resId);

        cancel();

    }
}
