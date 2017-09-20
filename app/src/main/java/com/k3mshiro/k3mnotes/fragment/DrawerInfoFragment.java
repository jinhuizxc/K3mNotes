package com.k3mshiro.k3mnotes.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;

public class DrawerInfoFragment extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout linearLayout;
    private TextView tvCreatedDate, tvModifiedDate, tvPriority;
    private ImageView ivColor;
    private Button btnHide;
    private String fragmentTheme;

    private String createdDate, modifiedDate, parseColor;
    private int priority;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drawer_info_layout, container, false);
        getDatas();
        initViews();
        return view;
    }

    private void getDatas() {
        Bundle bundle = getArguments();
        createdDate = bundle.getString(ConstantUtil.KEY_CREATED_DATE, "");
        modifiedDate = bundle.getString(ConstantUtil.KEY_MODIFIED_DATE, "");
        parseColor = bundle.getString(ConstantUtil.KEY_COLOR, "#4CAF50");
        priority = bundle.getInt(ConstantUtil.KEY_PRIORITY, 0);
        fragmentTheme = bundle.getString(ConstantUtil.KEY_THEME);
    }

    private void initViews() {
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_info);
        tvCreatedDate = (TextView) view.findViewById(R.id.tv_content_createdDate);
        tvModifiedDate = (TextView) view.findViewById(R.id.tv_content_modifiedDate);
        tvPriority = (TextView) view.findViewById(R.id.tv_content_priority);
        ivColor = (ImageView) view.findViewById(R.id.iv_color);
        btnHide = (Button) view.findViewById(R.id.btn_drawer_back);
        btnHide.setOnClickListener(this);

        tvCreatedDate.setText(createdDate);
        tvModifiedDate.setText(modifiedDate);
        if (parseColor.compareTo("#F44336") == 0) {
            ivColor.setBackgroundResource(R.drawable.red_circle_bg);
        } else if (parseColor.compareTo("#FB8C00") == 0) {
            ivColor.setBackgroundResource(R.drawable.orange_circle_bg);
        } else if (parseColor.compareTo("#FFEA00") == 0) {
            ivColor.setBackgroundResource(R.drawable.yellow_circle_bg);
        } else if (parseColor.compareTo("#4CAF50") == 0) {
            ivColor.setBackgroundResource(R.drawable.green_circle_bg);
        } else if (parseColor.compareTo("#2196F3") == 0) {
            ivColor.setBackgroundResource(R.drawable.blue_circle_bg);
        } else if (parseColor.compareTo("#3F51B5") == 0) {
            ivColor.setBackgroundResource(R.drawable.indigo_circle_bg);
        } else {
            ivColor.setBackgroundResource(R.drawable.violet_circle_bg);
        }

        switch (priority) {
            case 0:
                tvPriority.setText(getText(R.string.none_priority));
                tvPriority.setTextColor(Color.parseColor("#2196F3"));
                break;

            case 1:
                tvPriority.setText(getText(R.string.low_priority));
                tvPriority.setTextColor(Color.parseColor("#4CAF50"));
                break;

            case 2:
                tvPriority.setText(getText(R.string.medium_priority));
                tvPriority.setTextColor(Color.parseColor("#FFEA00"));
                break;

            case 3:
                tvPriority.setText(getText(R.string.high_priority));
                tvPriority.setTextColor(Color.parseColor("#FB8C00"));
                break;

            default:
                break;
        }

        if (fragmentTheme.equals(ConstantUtil.DARKTHEME)) {
            linearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_grey_500));
            btnHide.setBackgroundResource(R.drawable.ic_arrow_back_white_24dp);
        } else {
            btnHide.setBackgroundResource(R.drawable.ic_arrow_back_red_700_48dp);
            linearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drawer_back:
                getFragmentManager().beginTransaction().remove(this).commit();
                break;
            default:
                break;
        }
    }
}
