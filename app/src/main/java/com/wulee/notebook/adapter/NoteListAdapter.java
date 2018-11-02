package com.wulee.notebook.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wulee.notebook.R;
import com.wulee.notebook.bean.Note;

import java.util.List;


public class NoteListAdapter extends BaseQuickAdapter<Note,BaseViewHolder> {

    public NoteListAdapter(int layoutResId, List<Note> dataList) {
        super(layoutResId,dataList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder,Note note) {
        baseViewHolder.setText(R.id.tv_list_title,note.getTitle());

        if (note.getIsEncrypt() > 0) {
            baseViewHolder.setText(R.id.tv_list_summary,"Locked");
        } else {
            baseViewHolder.setText(R.id.tv_list_summary, note.getContent());
        }

        baseViewHolder.setText(R.id.tv_list_time , note.getUpdatedAt());
    }
}
