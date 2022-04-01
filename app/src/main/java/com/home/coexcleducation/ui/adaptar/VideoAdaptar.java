package com.home.coexcleducation.ui.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.coexcleducation.R;

import java.util.HashMap;
import java.util.List;

public class VideoAdaptar extends BaseAdapter {

    private LayoutInflater mInflater;
    Context mContext;
    List<HashMap<String, String>> mLabelList;
    String lType;

    public VideoAdaptar(Context context, List<HashMap<String, String>> pLabelList, String pType) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mLabelList = pLabelList;
        lType = pType;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.video_item, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.Name_of_challenge);
            lViewholder.mLogo = convertView.findViewById(R.id.image_on_challenge);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            lViewholder.mLabel.setText(mLabelList.get(position).get("chaptername"));
            if(lType.equals("summary")) {
                lViewholder.mLogo.setImageResource(R.drawable.ic_notes);
            } else {
                lViewholder.mLogo.setImageResource(R.drawable.ic_video_play);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public String getItem(int position) {
        return mLabelList.get(position).get("chaptername");
    }

    @Override
    public int getCount() {
        return mLabelList.size();
    }

    class ViewHolder {
        TextView mLabel;
        ImageView mLogo;
    }
}
