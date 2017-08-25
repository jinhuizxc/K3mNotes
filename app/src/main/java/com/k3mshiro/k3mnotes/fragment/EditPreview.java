package com.k3mshiro.k3mnotes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k3mshiro.k3mnotes.R;

/**
 * Created by k3mshiro on 8/24/17.
 */

public class EditPreview extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_note, container, false);
        initNote();
        initViews();
        return view;
    }

    private void initNote() {

    }

    private void initViews() {

    }
}
