package com.k3mshiro.k3mnotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;

public class SnapshotNoteActivity extends CreateNoteActivity {

    private String snapShotHTML;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatas();
    }

    private void getDatas() {
        Intent intent = getIntent();
        snapShotHTML = intent.getExtras().getString(ConstantUtil.SNAPSHOT);
        redtContent.setHtml(snapShotHTML);
    }
}
