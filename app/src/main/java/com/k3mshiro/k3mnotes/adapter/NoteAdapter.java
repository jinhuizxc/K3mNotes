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
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final String theme;
    protected SortedList<NoteDTO> sortedNotes;
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
        theme = mContext.getSharedPreferences(ConstantUtil.THEME_PREFERENCES, Context.MODE_PRIVATE)
                .getString(ConstantUtil.THEME_SAVED, ConstantUtil.LIGHTTHEME);
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
        if (note.getTimeReminder() > 0) {
            holder.ivReminderIcon.setVisibility(View.VISIBLE);
        }
        if (theme.equals(ConstantUtil.LIGHTTHEME)) {
            holder.ivFavoriteIcon.setBackgroundResource(R.drawable.amber_triangle_drawable);
            holder.ivReminderIcon.setBackgroundResource(R.drawable.ic_alarm_on_amber_a700_24dp);
        } else {
            holder.ivFavoriteIcon.setBackgroundResource(R.drawable.cyan_triangle_drawable);
            holder.ivReminderIcon.setBackgroundResource(R.drawable.ic_alarm_on_cyan_600_24dp);
        }

        holder.itemView.setLongClickable(true);
    }

    @Override
    public int getItemCount() {
        return sortedNotes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFavoriteIcon;
        private ImageView ivReminderIcon;
        private TextView tvDate;
        private TextView tvContent;
        private TextView tvTitle;

        private NoteViewHolder(final View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivFavoriteIcon = (ImageView) itemView.findViewById(R.id.favorite_icon);
            ivReminderIcon = (ImageView) itemView.findViewById(R.id.reminder_icon);
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

    protected Date dateStringConverter(String dateString) {
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
