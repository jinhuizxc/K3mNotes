package com.k3mshiro.k3mnotes.activity;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.fragment.SettingsFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if (theme.equals(MainActivity.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        final Drawable backArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back_white_24dp);
        if (backArrow != null) {
            if (theme.equals(MainActivity.DARKTHEME)) {
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
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>Settings</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>Settings</font>"));
            }
        }

        if (theme.equals(MainActivity.DARKTHEME)) {
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_500));
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.setting_content, new SettingsFragment()).commit();
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
