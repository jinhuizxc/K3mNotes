package com.k3mshiro.k3mnotes.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.adapter.ModifiedDateSortedAdapter;
import com.k3mshiro.k3mnotes.adapter.NoteAdapter;
import com.k3mshiro.k3mnotes.customview.recycleview.RecyclerViewEmptySupport;
import com.k3mshiro.k3mnotes.customview.recycleview.VerticalItemSpace;
import com.k3mshiro.k3mnotes.customview.toasty.Toasty;
import com.k3mshiro.k3mnotes.dao.NoteDAO;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.List;

public class TrashActivity extends AppCompatActivity {
    public static final int RESULT_CODE_PUTBACK = 1031;
    private String theme;
    private NoteDAO noteDAO;
    private List<NoteDTO> noteDTOs;
    private NoteAdapter mTrashAdapter;
    private RelativeLayout rltListTrash;
    private Toolbar trashToolbar;
    private RecyclerViewEmptySupport rvList;

    private NoteDTO deletedNote;
    private int actionID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if (theme.equals(MainActivity.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_bin);
        initNotes();
        initViews();
        initSwipe();
    }

    private void initNotes() {
        noteDAO = new NoteDAO(this);
        noteDAO.openDatabase();
        noteDTOs = noteDAO.getNotesInTrash();
        mTrashAdapter = new ModifiedDateSortedAdapter(TrashActivity.this);
        mTrashAdapter.addAll(noteDTOs);
    }

    private void initViews() {
        rltListTrash = (RelativeLayout) findViewById(R.id.listTrash_act);

        trashToolbar = (Toolbar) findViewById(R.id.trash_toolbar);
        setSupportActionBar(trashToolbar);

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
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>Trash Bin</font>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                actionBar.setTitle(Html.fromHtml("<font face='monospace' color='#FFFFFF'>Trash Bin</font>"));
            }
        }

        rvList = (RecyclerViewEmptySupport) findViewById(R.id.listTrash);
        rvList.setEmptyView(findViewById(R.id.list_empty_trash));
        LinearLayoutManager llm = new LinearLayoutManager(TrashActivity.this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(llm);
        rvList.setHasFixedSize(true); // nang cao hieu suat khi cac item cung do rong va chieu cao
        rvList.addItemDecoration(new VerticalItemSpace(MainActivity.VERTICAL_ITEM_SPACE));//add ItemDecoration - them khoang cach

        if (theme.equals(MainActivity.DARKTHEME)) {
            trashToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_500));
        } else {
            trashToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            rltListTrash.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
        }

        rvList.setAdapter(mTrashAdapter);

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
                deletedNote = mTrashAdapter.get(position);
                switch (swipeDirection) {
                    case ItemTouchHelper.LEFT:
                        actionID = 1;
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrashActivity.this);
                        builder.setTitle("Delete Note");
                        builder.setMessage("This will result in the PERMANENT deletion of this note.");
                        builder.setIcon(R.drawable.do_note_icon);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTrashAdapter.remove(deletedNote);
                                noteDAO.deleteNote(deletedNote);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.create();
                        builder.show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        actionID = 2;
                        mTrashAdapter.remove(deletedNote);
                        noteDAO.updateToTrash(deletedNote, 0);

                        Snackbar snackbar = Snackbar.make(rltListTrash, "1 note has been restored", Snackbar.LENGTH_SHORT);
                        View sbView = snackbar.getView();
                        TextView sbText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        if (theme.equals(MainActivity.LIGHTTHEME)) {
                            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_800));
                            sbText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
                        } else if (theme.equals(MainActivity.LIGHTTHEME)) {
                            sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_lightest));
                            sbText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_grey_800));
                        }
                        snackbar.show();
                    default:
                        actionID = 0;
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                if (actionID == 2) {
                    setResult(RESULT_CODE_PUTBACK);
                }
                return true;

            case R.id.item_empty:
                AlertDialog.Builder builder = new AlertDialog.Builder(TrashActivity.this);
                builder.setTitle("Empty Trash");
                builder.setMessage("Delete all notes in the trash");
                builder.setIcon(R.drawable.do_note_icon);
                builder.setCancelable(false);
                builder.setPositiveButton("Empty", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrashAdapter.clear();
                        noteDAO.emptyTrash();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toasty.success(TrashActivity.this, "All notes has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (NavUtils.getParentActivityName(this) != null) {
            NavUtils.navigateUpFromSameTask(this);
        }
        if (actionID == 2) {
            setResult(RESULT_CODE_PUTBACK);
        }
    }
}
