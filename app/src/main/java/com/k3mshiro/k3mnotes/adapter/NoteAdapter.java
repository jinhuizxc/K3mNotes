package com.k3mshiro.k3mnotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.activity.MainActivity;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final String theme;
    private SortedList<NoteDTO> sortedNotes;
    private static OnItemClickListener listener;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(NoteAdapter.OnItemClickListener listener) {
        NoteAdapter.listener = listener;
    }

    public NoteAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        sortedNotes = new SortedList<>(NoteDTO.class, new SortedList.Callback<NoteDTO>() {
            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public int compare(NoteDTO note1, NoteDTO note2) {
                Date date1 = dateStringConverter(note1.getModifiedDate());
                Date date2 = dateStringConverter(note2.getModifiedDate());
                return date2.compareTo(date1);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(NoteDTO oldItem, NoteDTO newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(NoteDTO note1, NoteDTO note2) {
                return note1.getId() == note2.getId();
            }
        });
        theme = mContext.getSharedPreferences(MainActivity.THEME_PREFERENCES, Context.MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new NoteAdapter.NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteDTO note = sortedNotes.get(position);
        holder.tvDate.setText(note.getModifiedDate().substring(0, 10));
        holder.tvTitle.setText(note.getTitle());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.tvContent.setText(Html.fromHtml(note.getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvContent.setText(Html.fromHtml(note.getContent()));
        }
        holder.tvDate.setBackgroundColor(Color.parseColor(note.getColor()));
        holder.tvTitle.setTextColor(Color.parseColor(note.getColor()));
        if (note.getFavoriteValue() == 1) {
            holder.ivFavoriteIcon.setVisibility(View.VISIBLE);
        }
        if (theme.equals(MainActivity.LIGHTTHEME)) {
            holder.ivFavoriteIcon.setBackgroundResource(R.drawable.amber_triangle_drawable);
        } else {
            holder.ivFavoriteIcon.setBackgroundResource(R.drawable.cyan_triangle_drawable);
        }
    }

    @Override
    public int getItemCount() {
        return sortedNotes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFavoriteIcon;
        private TextView tvDate;
        private TextView tvContent;
        private TextView tvTitle;

        private NoteViewHolder(final View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivFavoriteIcon = (ImageView) itemView.findViewById(R.id.favorite_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }

    public NoteDTO get(int position) {
        return sortedNotes.get(position);
    }

    public int add(NoteDTO note) {
        return sortedNotes.add(note);
    }

    public int indexOf(NoteDTO note) {
        return sortedNotes.indexOf(note);
    }

    public void updateItemAt(int index, NoteDTO note) {
        sortedNotes.updateItemAt(index, note);
    }

    public void addAll(List<NoteDTO> listNotes) {
        sortedNotes.beginBatchedUpdates();
        for (int i = 0; i < listNotes.size(); i++) {
            NoteDTO note = listNotes.get(i);
            sortedNotes.add(note);
        }
        sortedNotes.endBatchedUpdates();
    }

    public void addAll(NoteDTO[] notes) {
        addAll(Arrays.asList(notes));
    }

    public boolean remove(NoteDTO note) {
        return sortedNotes.remove(note);
    }

    public NoteDTO removeItemAt(int index) {
        return sortedNotes.removeItemAt(index);
    }

    public void clear() {
        sortedNotes.beginBatchedUpdates();
        while (sortedNotes.size() > 0) {
            sortedNotes.removeItemAt(sortedNotes.size() - 1);
        }
        sortedNotes.endBatchedUpdates();
    }

    private Date dateStringConverter(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
