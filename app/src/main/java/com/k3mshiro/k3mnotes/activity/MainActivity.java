package com.k3mshiro.k3mnotes.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.adapter.ModifiedDateSortedAdapter;
import com.k3mshiro.k3mnotes.adapter.NoteAdapter;
import com.k3mshiro.k3mnotes.customview.dialog.ReminderDialog;
import com.k3mshiro.k3mnotes.customview.recycleview.RecyclerViewEmptySupport;
import com.k3mshiro.k3mnotes.customview.recycleview.VerticalItemSpace;
import com.k3mshiro.k3mnotes.customview.toasty.Toasty;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.OnItemClickListener, DialogInterface.OnDismissListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private String theme = "name_of_the_theme";
    private String viewMode = "name_of_view_mode";
    private int mTheme = -1;

    private Toolbar listToolbar;
    private RecyclerViewEmptySupport rvList;
    private FloatingActionButton fab;
    private FloatingActionButton fabNewNote;
    private FloatingActionButton fabNewPhoto;
    private FloatingActionButton fabNewAlarm;
    private RelativeLayout rltListNote;

    private NoteAdapter mNoteAdapter;
    private List<NoteDTO> noteDTOs;
    private NoteDAO noteDAO;
    private boolean isFloatingActionButtonShow = false;
    private NoteDTO deletedNote, editedNote;
    private int editPos;

    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedPreferences(ConstantUtil.THEME_PREFERENCES, MODE_PRIVATE).getBoolean(ConstantUtil.RECREATE_ACTIVITY, false)) {
            SharedPreferences.Editor editor = getSharedPreferences(ConstantUtil.THEME_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(ConstantUtil.RECREATE_ACTIVITY, false);
            editor.apply();
            recreate();
        } else if (getSharedPreferences(ConstantUtil.VIEW_PREFERENCES, MODE_PRIVATE).getBoolean(ConstantUtil.RECREATE_ACTIVITY, false)) {
            SharedPreferences.Editor editor = getSharedPreferences(ConstantUtil.VIEW_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(ConstantUtil.RECREATE_ACTIVITY, false);
            editor.apply();
            recreate();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        theme = getSharedPreferences(ConstantUtil.THEME_PREFERENCES, MODE_PRIVATE)
                .getString(ConstantUtil.THEME_SAVED, ConstantUtil.LIGHTTHEME);
        if (theme.equals(ConstantUtil.LIGHTTHEME)) {
            mTheme = R.style.CustomStyle_LightTheme;
        } else {
            mTheme = R.style.CustomStyle_DarkTheme;
        }
        this.setTheme(mTheme);
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                initializeComponents();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        REQUEST_CODE_PERMISSIONS);
            }
        } else {
            initializeComponents();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            initializeComponents();
        } else {
            Toast.makeText(this,
                    "Cấp quyền cho ứng dụng, vui lòng thử lại!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initializeComponents() {
        setContentView(R.layout.activity_list_note);
        initNotes();
        initViews();
        initSwipe();
    }

    private void initNotes() {
        noteDAO = new NoteDAO(this);
        noteDAO.openDatabase();
        mNoteAdapter = new ModifiedDateSortedAdapter(MainActivity.this);

        viewMode = getSharedPreferences(ConstantUtil.VIEW_PREFERENCES, MODE_PRIVATE)
                .getString(ConstantUtil.VIEWMODE_SAVED, ConstantUtil.ALLMODE);
        if (viewMode.equals(ConstantUtil.ALLMODE)) {
            noteDTOs = noteDAO.getAllNotes(1);
        } else if (viewMode.equals(ConstantUtil.FAVORITEMODE)) {
            noteDTOs = noteDAO.getAllNotes(2);
        } else if (viewMode.equals(ConstantUtil.REMINDERMODE)) {
            noteDTOs = noteDAO.getAllNotes(3);
        }

        mNoteAdapter.addAll(noteDTOs);
        mNoteAdapter.setOnItemClickListener(this);
    }

    private void initViews() {
        rltListNote = (RelativeLayout) findViewById(R.id.listNote_act);

        listToolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(listToolbar);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>All Notes</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>All Notes</font>"));
            }
        }

        rvList = (RecyclerViewEmptySupport) findViewById(R.id.cardList);
        rvList.setEmptyView(findViewById(R.id.list_empty_view));
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(llm);
        rvList.setHasFixedSize(true); // nang cao hieu suat khi cac item cung do rong va chieu cao
        rvList.addItemDecoration(new VerticalItemSpace(ConstantUtil.VERTICAL_ITEM_SPACE));//add ItemDecoration - them khoang cach

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabNewNote = (FloatingActionButton) findViewById(R.id.fab_new_note);
        fabNewAlarm = (FloatingActionButton) findViewById(R.id.fab_new_alarm);
        fabNewPhoto = (FloatingActionButton) findViewById(R.id.fab_new_photo);

        if (theme.equals(ConstantUtil.DARKTHEME)) {
            listToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_500));
        } else {
            listToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            rltListNote.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
        }

        fab.setOnClickListener(this);
        fabNewNote.setOnClickListener(this);
        fabNewAlarm.setOnClickListener(this);
        fabNewPhoto.setOnClickListener(this);

        rvList.setAdapter(mNoteAdapter);
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                switch (swipeDirection) {
                    case ItemTouchHelper.LEFT:
                        deleteNote(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        deleteNote(position);
                    default:
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_settings:
                Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSetting);
                break;

            case R.id.item_about:
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                break;

            case R.id.item_trash:
                Intent intentTrash = new Intent(MainActivity.this, TrashActivity.class);
                startActivityForResult(intentTrash, ConstantUtil.REQUEST_CODE_TRASH);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        editNote(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (!isFloatingActionButtonShow) {
                    runAllFAB();
                    isFloatingActionButtonShow = true;
                } else {
                    returnAllFAB();
                    isFloatingActionButtonShow = false;
                }
                break;

            case R.id.fab_new_note:
                showCreateNoteScreen();
                isFloatingActionButtonShow = false;
                break;

            case R.id.fab_new_alarm:
                ReminderDialog reminderDialog = new ReminderDialog(this);
                reminderDialog.show();
                reminderDialog.setOnDismissListener(this);
                isFloatingActionButtonShow = false;
                break;

            case R.id.fab_new_photo:
                takeSnapshot();
                returnAllFAB();
                isFloatingActionButtonShow = false;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantUtil.REQUEST_CODE_EDIT:
                if (resultCode == ConstantUtil.RESULT_CODE_SUCCESS) {
                    mNoteAdapter.updateItemAt(editPos, editedNote);
                } else if (resultCode == ConstantUtil.RESULT_CODE_DELETE) {
                    mNoteAdapter.remove(editedNote);
                    noteDAO.updateToTrash(editedNote, 1);
                    Toasty.success(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                }
                break;

            case ConstantUtil.REQUEST_CODE_TRASH:
                if (resultCode == TrashActivity.RESULT_CODE_PUTBACK) {
                    mNoteAdapter.notifyDataSetChanged();
                }
                break;

            case ConstantUtil.REQUEST_OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String snapshotHtml = ConstantUtil.getSnapshotPath(bitmap);
                    Intent intent = new Intent(MainActivity.this, SnapshotNoteActivity.class);
                    intent.putExtra(ConstantUtil.SNAPSHOT, snapshotHtml);
                    startActivity(intent);
                } else if (resultCode == RESULT_CANCELED) {
                    Toasty.info(this, "Action canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(this, "Action Failed", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /***************** Add, Remove, Edit **********************************/

    private void showCreateNoteScreen() {
        Intent intentCreate = new Intent(MainActivity.this, CreateNoteActivity.class);
        startActivity(intentCreate);
    }

    private void editNote(int position) {
        editPos = position;
        editedNote = mNoteAdapter.get(position);
        Intent intentEdit = new Intent(MainActivity.this, EditNoteActivity.class);
        intentEdit.putExtra(ConstantUtil.EDIT_NOTE, editedNote);
        startActivityForResult(intentEdit, ConstantUtil.REQUEST_CODE_EDIT);
    }

    private void deleteNote(int position) {
        deletedNote = mNoteAdapter.get(position);
        mNoteAdapter.remove(deletedNote);
        Snackbar snackbar = Snackbar.make(rltListNote, "1 note was deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoteAdapter.add(deletedNote);
                    }
                });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event == DISMISS_EVENT_TIMEOUT) {
                    noteDAO.updateToTrash(deletedNote, 1);
                }
            }
        });

        View sbView = snackbar.getView();
        TextView sbText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if (theme.equals(ConstantUtil.LIGHTTHEME)) {
            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_800));
            sbText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
        } else if (theme.equals(ConstantUtil.DARKTHEME)) {
            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
            sbText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_800));
        }

        snackbar.show();
    }

    private void takeSnapshot() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, ConstantUtil.REQUEST_OPEN_CAMERA);
        } else {
            Toasty.error(getApplication(), "Camera is unsupported!", Toast.LENGTH_SHORT).show();
        }
    }


    /***************** Animation Setting **********************************/
    private void showAllFloatingActionButon() {
        fabNewPhoto.show();
        fabNewNote.show();
        fabNewAlarm.show();
    }

    private void hideAllFloatingActionButon() {
        fabNewPhoto.hide();
        fabNewNote.hide();
        fabNewAlarm.hide();
    }

    private void goToW() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewAlarm.getLayoutParams();
        Animation goWanimation = AnimationUtils.loadAnimation(this, R.anim.anim_go_w);
        paramsW.rightMargin = (int) (fabNewAlarm.getWidth() * 1.8);
        fabNewAlarm.setLayoutParams(paramsW);
        fabNewAlarm.startAnimation(goWanimation);
    }

    private void returnW() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewAlarm.getLayoutParams();
        Animation retWanimation = AnimationUtils.loadAnimation(this, R.anim.anim_return_w);
        paramsW.rightMargin = (int) (fabNewAlarm.getWidth() * 1.8);
        fabNewAlarm.setLayoutParams(paramsW);
        fabNewAlarm.startAnimation(retWanimation);
    }

    private void goToNW() {
        FrameLayout.LayoutParams paramsNW = (FrameLayout.LayoutParams) fabNewPhoto.getLayoutParams();
        Animation goNWanimation = AnimationUtils.loadAnimation(this, R.anim.anim_go_nw);
        paramsNW.rightMargin = (int) (fabNewPhoto.getWidth() * 1.5);
        paramsNW.bottomMargin = (int) (fabNewPhoto.getHeight() * 1.5);
        fabNewPhoto.setLayoutParams(paramsNW);
        fabNewPhoto.startAnimation(goNWanimation);
    }

    private void returnNW() {
        FrameLayout.LayoutParams paramsNW = (FrameLayout.LayoutParams) fabNewPhoto.getLayoutParams();
        Animation retNWanimation = AnimationUtils.loadAnimation(this, R.anim.anim_return_nw);
        paramsNW.rightMargin = (int) (fabNewPhoto.getWidth() * 1.5);
        paramsNW.bottomMargin = (int) (fabNewPhoto.getHeight() * 1.5);
        fabNewPhoto.setLayoutParams(paramsNW);
        fabNewPhoto.startAnimation(retNWanimation);
    }

    private void goToN() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewNote.getLayoutParams();
        Animation goNanimation = AnimationUtils.loadAnimation(this, R.anim.anim_go_n);
        paramsW.bottomMargin = (int) (fabNewNote.getWidth() * 1.8);
        fabNewNote.setLayoutParams(paramsW);
        fabNewNote.startAnimation(goNanimation);
    }

    private void returnN() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewNote.getLayoutParams();
        Animation retNanimation = AnimationUtils.loadAnimation(this, R.anim.anim_return_n);
        paramsW.bottomMargin = (int) (fabNewNote.getWidth() * 1.8);
        fabNewNote.setLayoutParams(paramsW);
        fabNewNote.startAnimation(retNanimation);
    }

    private void returnAllFAB() {
        hideAllFloatingActionButon();
        returnN();
        returnNW();
        returnW();
    }

    private void runAllFAB() {
        showAllFloatingActionButon();
        goToW();
        goToNW();
        goToN();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mNoteAdapter.notifyDataSetChanged();
        recreate();
    }
}
