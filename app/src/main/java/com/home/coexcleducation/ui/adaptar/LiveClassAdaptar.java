package com.home.coexcleducation.ui.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.coexcleducation.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LiveClassAdaptar extends BaseAdapter {

    private LayoutInflater mInflater;
    Context mContext;
    List<HashMap<String, Object>> mLabelList;
    SimpleDateFormat lDateformat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
    SimpleDateFormat lTimeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);


    public LiveClassAdaptar(Context context, List<HashMap<String, Object>> pLabelList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mLabelList = pLabelList;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.live_classes_items, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.note_label);
            lViewholder.mSubHeader = convertView.findViewById(R.id.note_sub);
            lViewholder.mTime = convertView.findViewById(R.id.time);
            lViewholder.mDate = convertView.findViewById(R.id.date);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            Long lLongValue = Long.parseLong( mLabelList.get(position).get("startTime").toString());
            String lDate = lDateformat.format(new Date(lLongValue));
            String lTime = lTimeFormat.format(new Date(lLongValue));
            lViewholder.mLabel.setText(mLabelList.get(position).get("topic")+ "("+mLabelList.get(position).get("subject")+")");
            lViewholder.mSubHeader.setText("By "+mLabelList.get(position).get("teacherName")+" for "+mLabelList.get(position).get("duration")+" mins");
            lViewholder.mTime.setText(lTime);
            lViewholder.mDate.setText(lDate);
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
        return (String) mLabelList.get(position).get("teacherName");
    }

    @Override
    public int getCount() {
        return mLabelList.size();
    }

    class ViewHolder {
        TextView mLabel, mSubHeader, mTime, mDate;

    }
}

