package com.k3mshiro.k3mnotes.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;

import com.k3mshiro.k3mnotes.dto.NoteDTO;

import java.util.Date;

public class ModifiedDateSortedAdapter extends NoteAdapter {
    public ModifiedDateSortedAdapter(Context mContext) {
        super(mContext);
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
                Date modifiedDate1 = dateStringConverter(note1.getModifiedDate());
                Date modifiedDate2 = dateStringConverter(note2.getModifiedDate());
                return modifiedDate2.compareTo(modifiedDate1);
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
    }
}
