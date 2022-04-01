package com.home.coexcleducation.ui.adaptar;



import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.coexcleducation.R;


public class ListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	Context mContext;
	List<String> mLabelList;

	public ListAdapter(Context context, List<String> pLabelList) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		mLabelList = pLabelList;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		final int position = index;
		ViewHolder lViewholder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.spinner_item, null);
			lViewholder = new ViewHolder();
			lViewholder.mLabel = convertView.findViewById(R.id.note_label);
			convertView.setTag(lViewholder);
		} else {
			lViewholder = (ViewHolder) convertView.getTag();
		}

		try {
			lViewholder.mLabel.setText(mLabelList.get(position));
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
		return mLabelList.get(position);
	}

	@Override
	public int getCount() {
		return mLabelList.size();
	}

	class ViewHolder {
		TextView mLabel;
		ImageView mSelected;
	}
}