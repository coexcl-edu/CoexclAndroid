package com.home.coexcleducation.ui.adaptar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import com.home.coexcleducation.R;
import com.home.coexcleducation.listner.NotesDeletionListner;
import com.home.coexcleducation.ui.notes.EditNotesActivity;
import com.home.coexcleducation.ui.notes.NoteListingActivity;

import java.util.HashMap;
import java.util.List;

public class NotesAdaptar extends BaseAdapter {

    private LayoutInflater mInflater;
    Activity mContext;
    List<HashMap<String, String>> mNotesList;
    NotesDeletionListner listner;

    public NotesAdaptar(Activity context, List<HashMap<String, String>> pLabelList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mNotesList = pLabelList;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.note_listing_item, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.note_label);
            lViewholder.mSubHeader = convertView.findViewById(R.id.note_sub);
            lViewholder.mLayout = convertView.findViewById(R.id.note_layout);
            lViewholder.mMenuImportPlace = convertView.findViewById(R.id.menuPlace);
            lViewholder.mPopUpLayout = convertView.findViewById(R.id.popupLayout);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            lViewholder.mLabel.setText(mNotesList.get(position).get("title"));
            lViewholder.mSubHeader.setText(mNotesList.get(position).get("description"));
            lViewholder.mPopUpLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new androidx.appcompat.widget.PopupMenu(mContext, lViewholder.mMenuImportPlace);
                    popup.getMenu().add(Menu.NONE, R.id.edit_profile, Menu.NONE, "Delete");
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_profile:
                                    listner.deleteNoes(position);
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mNotesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView mLabel, mSubHeader;
        RelativeLayout mLayout, mPopUpLayout;
        View mMenuImportPlace;
    }

    public void setOnclickListner(NotesDeletionListner onclickListner){
        listner = onclickListner;
    }
}
