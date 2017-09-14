package com.k3mshiro.k3mnotes.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.k3mshiro.k3mnotes.R;

/**
 * Created by k3mshiro on 9/10/17.
 */

public class PrioritySetDialog extends Dialog implements View.OnClickListener {

    private Button btnNone, btnLow, btnMedium, btnHigh;
    private String textColor;
    private int textResId;
    private int priorityValue;
    private OnCallBack mPriorityListener;

    public interface OnCallBack {
        void setPriorityText(int textResId);

        void setPriorityTextColor(String textColor);

        void setPriorityValue(int priorityValue);
    }

    public void setmPriorityListener(OnCallBack mPriorityListener) {
        this.mPriorityListener = mPriorityListener;
    }

    public PrioritySetDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.priority_bar_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initViews();
    }

    private void initViews() {
        btnNone = (Button) findViewById(R.id.btn_none);
        btnLow = (Button) findViewById(R.id.btn_low);
        btnMedium = (Button) findViewById(R.id.btn_medium);
        btnHigh = (Button) findViewById(R.id.btn_high);

        btnNone.setOnClickListener(this);
        btnLow.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHigh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_none:
                priorityValue = 0;
                textResId = R.string.none_priority;
                textColor = "#2196F3";
                break;
            case R.id.btn_low:
                priorityValue = 1;
                textResId = R.string.low_priority;
                textColor = "#4CAF50";
                break;
            case R.id.btn_medium:
                priorityValue = 2;
                textResId = R.string.medium_priority;
                textColor = "#FFEA00";
                break;
            case R.id.btn_high:
                priorityValue = 3;
                textResId = R.string.high_priority;
                textColor = "#FB8C00";
                break;
            default:
                break;
        }

        mPriorityListener.setPriorityText(textResId);
        mPriorityListener.setPriorityTextColor(textColor);
        mPriorityListener.setPriorityValue(priorityValue);

        cancel();
    }
}
