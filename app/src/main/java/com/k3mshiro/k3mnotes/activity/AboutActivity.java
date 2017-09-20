package com.k3mshiro.k3mnotes.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;


public class AboutActivity extends AppCompatActivity {
    private String theme;
    private Toolbar aboutToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        theme = getSharedPreferences(ConstantUtil.THEME_PREFERENCES, MODE_PRIVATE)
                .getString(ConstantUtil.THEME_SAVED, ConstantUtil.LIGHTTHEME);
        if (theme.equals(ConstantUtil.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        aboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(aboutToolbar);

        final Drawable backArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_white_24dp);
        if (backArrow != null) {
            if (theme.equals(ConstantUtil.DARKTHEME)) {
                backArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.grey300), PorterDuff.Mode.SRC_ATOP);
            } else {
                backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>About</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>About</font>"));
            }
        }
        TextView tvVersionCode = (TextView) findViewById(R.id.tv_about_verCode);
        TextView tvEmailContact = (TextView) findViewById(R.id.tv_about_emailContact);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "Version: " + pInfo.versionName;
            tvVersionCode.setText(version);
            tvEmailContact.setText(getResources().getText(R.string.email_address));
            Linkify.addLinks(tvEmailContact, Linkify.ALL);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (theme.equals(ConstantUtil.DARKTHEME)) {
            aboutToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_500));
        } else {
            aboutToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
