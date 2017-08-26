package com.k3mshiro.k3mnotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context mContext;
    private List<NoteDTO> mNoteDTOs;
    private LayoutInflater mLayoutInflater;

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        NoteAdapter.listener = listener;
    }


    public NoteAdapter(Context context, List<NoteDTO> mNoteDTOs) {
        this.mContext = context;
        this.mNoteDTOs = mNoteDTOs;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteDTO note = mNoteDTOs.get(position);
        holder.tvDate.setText(note.getDate());
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvDate.setBackgroundColor(Color.parseColor(note.getColor()));
        holder.tvTitle.setTextColor(Color.parseColor(note.getColor()));
    }

    @Override
    public int getItemCount() {
        return mNoteDTOs.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvContent;
        private TextView tvTitle;

        public NoteViewHolder(final View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
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

    public void addNote(NoteDTO newNote) {
        mNoteDTOs.add(newNote);
        notifyItemInserted(mNoteDTOs.size());
    }

    public void deleteNote(int position) {
        mNoteDTOs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNoteDTOs.size());
    }

    public void editNote(int position) {
        notifyItemChanged(position);
    }
}
