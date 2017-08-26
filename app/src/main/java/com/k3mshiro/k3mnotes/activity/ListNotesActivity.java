package com.k3mshiro.k3mnotes.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.adapter.NoteAdapter;
import com.k3mshiro.k3mnotes.customview.recycleview.RecyclerViewEmptySupport;
import com.k3mshiro.k3mnotes.customview.recycleview.VerticalItemSpace;
import com.k3mshiro.k3mnotes.customview.toasty.Toasty;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.List;

public class ListNotesActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = ListNotesActivity.class.getName();
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int VERTICAL_ITEM_SPACE = 30;
    public static final int REQUEST_CODE_CREATE = 101;
    public static final String EDIT_NOTE = "EDIT_NOTE";
    public static final int REQUEST_CODE_EDIT = 102;

    private Toolbar listToolbar;
    private RecyclerViewEmptySupport rvList;
    private FloatingActionButton fab;
    private FloatingActionButton fabNewNote;
    private FloatingActionButton fabNewPhoto;
    private FloatingActionButton fabNewAlarm;
    private RelativeLayout rltLayout;

    private NoteAdapter mNoteAdapter;
    private List<NoteDTO> noteDTOs;
    private NoteDAO noteDAO;
    private boolean isFloatingActionButtonShow = false;
    private NoteDTO deletedNote;
    private int deletePos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                initializeComponents();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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

    private void initViews() {
        rltLayout = (RelativeLayout) findViewById(R.id.listNote_act);

        listToolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(listToolbar);

        rvList = (RecyclerViewEmptySupport) findViewById(R.id.cardList);
        rvList.setEmptyView(findViewById(R.id.list_empty_view));
        LinearLayoutManager llm = new LinearLayoutManager(ListNotesActivity.this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(llm);
        rvList.setHasFixedSize(true); // nang cao hieu suat khi cac item cung do rong va chieu cao
        rvList.addItemDecoration(new VerticalItemSpace(VERTICAL_ITEM_SPACE));//add ItemDecoration - them khoang cach
        /* them divider
        rvList.addItemDecoration(new DividerItem(this));
        //or
        rvList.addItemDecoration(
                new DividerItem(this, R.drawable.divider));
         */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabNewNote = (FloatingActionButton) findViewById(R.id.fab_new_note);
        fabNewAlarm = (FloatingActionButton) findViewById(R.id.fab_new_alarm);
        fabNewPhoto = (FloatingActionButton) findViewById(R.id.fab_new_photo);
        fab.setOnClickListener(this);
        fabNewNote.setOnClickListener(this);
        fabNewAlarm.setOnClickListener(this);
        fabNewPhoto.setOnClickListener(this);

        rvList.setAdapter(mNoteAdapter);
    }

    private void initNotes() {
        noteDAO = new NoteDAO(this);
        noteDAO.openDatabase();
        noteDTOs = noteDAO.getListNotes();
        mNoteAdapter = new NoteAdapter(ListNotesActivity.this, noteDTOs);
        mNoteAdapter.setOnItemClickListener(this);
        mNoteAdapter.notifyDataSetChanged();

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
                break;
            case R.id.item_about:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CREATE:
                if (resultCode == CreateNoteActivity.RESULT_CODE_SUCCESS) {
                    mNoteAdapter.notifyItemInserted(noteDTOs.size());
                } else if (resultCode == CreateNoteActivity.RESULT_CODE_FAILURE) {
                    Toasty.error(this, "Couldn't create new note! Try again!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_EDIT:
                if (resultCode == CreateNoteActivity.RESULT_CODE_SUCCESS) {
                    mNoteAdapter.notifyDataSetChanged();
                } else if (resultCode == CreateNoteActivity.RESULT_CODE_FAILURE) {
                    Toasty.error(this, "Couldn't create new note! Try again!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        editNote(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (isFloatingActionButtonShow == false) {
                    showAllFloatingActionButon();
                    goToW();
                    goToNW();
                    goToN();
                    isFloatingActionButtonShow = true;
                } else {
                    hideAllFloatingActionButon();
                    returnN();
                    returnNW();
                    returnW();
                    isFloatingActionButtonShow = false;
                }
                break;

            case R.id.fab_new_note:
                showCreateNoteScreen();
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButtonShow = false;
                break;

            case R.id.fab_new_alarm:
                Toast.makeText(this, "New alarm click", Toast.LENGTH_SHORT).show();
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButtonShow = false;
                break;

            case R.id.fab_new_photo:
                Toast.makeText(this, "New photo click", Toast.LENGTH_SHORT).show();
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButtonShow = false;
                break;
        }
    }

    /***************** Add, Remove, Edit **********************************/

    private void showCreateNoteScreen() {
        Intent intentCreate = new Intent(ListNotesActivity.this, CreateNoteActivity.class);
        startActivityForResult(intentCreate, ListNotesActivity.REQUEST_CODE_CREATE);
    }

    private void editNote(int position) {
        NoteDTO editNote = noteDTOs.get(position);
        Intent intentEdit = new Intent(ListNotesActivity.this, EditNoteActivity.class);
        intentEdit.putExtra(EDIT_NOTE, editNote);
        startActivityForResult(intentEdit, ListNotesActivity.REQUEST_CODE_EDIT);
    }

    private void deleteNote(int position) {
        deletedNote = noteDTOs.get(position);
        deletePos = position;
        noteDAO.deleteNote(deletedNote);
        mNoteAdapter.deleteNote(position);
        Snackbar.make(rltLayout, "1 item has been deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteDTOs.add(deletePos, deletedNote);
                        noteDAO.createNewNote(deletedNote);
                        mNoteAdapter.notifyItemInserted(deletePos);
                    }
                }).show();
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
}
