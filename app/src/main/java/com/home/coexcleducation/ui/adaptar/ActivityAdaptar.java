package com.home.coexcleducation.ui.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.home.coexcleducation.R;
import com.home.coexcleducation.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ActivityAdaptar  extends BaseAdapter {

    private LayoutInflater mInflater;
    Context mContext;
    List<HashMap<String, String>> mLabelList;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat lDateformat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    public ActivityAdaptar(Context context,  List<HashMap<String, String>> pLabelList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mLabelList = pLabelList;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_listing_items, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.label);
            lViewholder.mSelected = convertView.findViewById(R.id.logo);
            lViewholder.mDate = convertView.findViewById(R.id.date);
            lViewholder.mLayout = convertView.findViewById(R.id.base_view);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            Date lDate = inputFormat.parse(mLabelList.get(position).get("date"));
            lViewholder.mLabel.setText(mLabelList.get(position).get("title"));
            lViewholder.mDate.setText(lDateformat.format(lDate));
            try {
                lViewholder.mLayout.setBackgroundTintList(ContextCompat.getColorStateList(mContext, new Constants().getColourList().get(position)));
            } catch (Exception e) {
                e.printStackTrace();
                lViewholder.mLayout.setBackgroundTintList(ContextCompat.getColorStateList(mContext, new Constants().getRandomColour()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mLabelList.size();
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
        TextView mLabel, mDate;
        ImageView mSelected;
        CardView mLayout;
    }
}
