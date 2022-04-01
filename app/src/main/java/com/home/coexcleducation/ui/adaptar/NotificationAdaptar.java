package com.home.coexcleducation.ui.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.home.coexcleducation.R;
import com.home.coexcleducation.jdo.NotificationJDO;
import com.home.coexcleducation.utils.CoexclLogs;

import java.util.List;

public class NotificationAdaptar extends BaseAdapter {

    private LayoutInflater mInflater;
    Context mContext;
    List<NotificationJDO> courseList;

    public NotificationAdaptar(Context context, List<NotificationJDO> pCourseList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        courseList = pCourseList;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_item_layout, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.course_name);
            lViewholder.mBody = convertView.findViewById(R.id.course_price);
            lViewholder.mImage = convertView.findViewById(R.id.course_image);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            lViewholder.mLabel.setText(courseList.get(position).getTitle());
            lViewholder.mBody.setText(courseList.get(position).getMessage());
            String lUrl = courseList.get(position).getImage();
            CoexclLogs.errorLog("Adapter", "Notifaction url - "+lUrl);
            if(lUrl != null && !lUrl.isEmpty()) {
                Glide.with(mContext)
                        .load(lUrl)
                        .centerCrop()
                        .placeholder(R.drawable.push_notifications)
                        .into(lViewholder.mImage);
            } else {
                lViewholder.mImage.setVisibility(View.GONE);
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
        return courseList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    class ViewHolder {
        TextView mLabel, mBody;
        ImageView mImage;
    }
}

