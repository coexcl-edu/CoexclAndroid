package com.home.coexcleducation.ui.adaptar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.home.coexcleducation.R;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.CoexclLogs;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LeaderBoardAdaptar  extends BaseAdapter {

    private LayoutInflater mInflater;
    Activity mContext;
    List<HashMap<String, Object>> mStudentList;
    List<Integer> mImages, mImageGirls;

    public LeaderBoardAdaptar(Activity context, List<HashMap<String, Object>> pStudentList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mStudentList = pStudentList;
        mImages = Arrays.asList(new Integer[]{R.drawable.avatar1, R.drawable.avatar6, R.drawable.avatar3
                , R.drawable.avatar1, R.drawable.avatar6, R.drawable.avatar3, R.drawable.avatar1, R.drawable.avatar6, R.drawable.avatar3
                , R.drawable.avatar6});

        mImageGirls =  Arrays.asList(new Integer[]{ R.drawable.avatar2, R.drawable.avatar5, R.drawable.avatar2, R.drawable.avatar5
                , R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar4, R.drawable.avatar2, R.drawable.avatar5, R.drawable.avatar4
                , R.drawable.avatar4});
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final int position = index;
        ViewHolder lViewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.leader_borad_item, null);
            lViewholder = new ViewHolder();
            lViewholder.mLabel = convertView.findViewById(R.id.note_label);
            lViewholder.mProgress = convertView.findViewById(R.id.score);
            lViewholder.mRank = convertView.findViewById(R.id.rank);
            lViewholder.mProfile = convertView.findViewById(R.id.profile);
            lViewholder.mSchool = convertView.findViewById(R.id.school);
            lViewholder.mBackPannel = convertView.findViewById(R.id.back_panel);
            lViewholder.mYourProfile = convertView.findViewById(R.id.your_profile);
            lViewholder.mCrown = convertView.findViewById(R.id.crown);
            convertView.setTag(lViewholder);
        } else {
            lViewholder = (ViewHolder) convertView.getTag();
        }

        try {
            try {
                if(UserDetails.getInstance().getID().contains(mStudentList.get(position).get("_id").toString().substring(0, 8))) {
                    lViewholder.mLabel.setText(mStudentList.get(position).get("name").toString()+" (You)");
                    lViewholder.mYourProfile.setVisibility(View.VISIBLE);
                } else {
                    lViewholder.mYourProfile.setVisibility(View.INVISIBLE);
                    lViewholder.mLabel.setText(mStudentList.get(position).get("name").toString());
                }
            } catch (Exception e) {
                lViewholder.mLabel.setText(mStudentList.get(position).get("name").toString());
            }

            HashMap<String, Object> lStudentObject =  mStudentList.get(position);

            try {
                HashMap<String, Object> lQuiz = lStudentObject.containsKey("quizInfo") ? (HashMap<String, Object>) lStudentObject.get("quizInfo") : null;
                HashMap<String, Object> lSchoolInfo = lStudentObject.containsKey("schoolInfo") ?  (HashMap<String, Object>) lStudentObject.get("schoolInfo") : null;

                Double percentage = Double.parseDouble(lQuiz.get("percent").toString());
                NumberFormat df = DecimalFormat.getInstance();
                df.setMinimumFractionDigits(1);
                df.setMaximumFractionDigits(1);
                df.setRoundingMode(RoundingMode.DOWN);
                lViewholder.mProgress.setText("" + df.format(percentage) + "%");
                lViewholder.mRank.setText("" + (position + 1));
                lViewholder.mSchool.setText(lSchoolInfo.containsKey("schoolName") ? "" + lSchoolInfo.get("schoolName") : "Coexcl Education");
                if(position == 0) {
                    lViewholder.mCrown.setVisibility(View.VISIBLE);
                    lViewholder.mRank.setVisibility(View.GONE);
                    lViewholder.mBackPannel.setBackground(ContextCompat.getDrawable(mContext, R.drawable.gliter_back));
                } else {
                    lViewholder.mCrown.setVisibility(View.GONE);
                    lViewholder.mRank.setVisibility(View.VISIBLE);
                    lViewholder.mBackPannel.setBackground(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, Object> lPersonalInfo = lStudentObject.containsKey("personalInfo") ? (HashMap<String, Object>) lStudentObject.get("personalInfo") : null;
            if(lPersonalInfo != null && lPersonalInfo.containsKey("gender")) {
                CoexclLogs.errorLog("TAG", "gender ---- "+lPersonalInfo.get("gender"));
                if(lPersonalInfo.get("gender").equals("Female")) {
                    lViewholder.mProfile.setImageResource(mImageGirls.get(position));
                } else {
                    lViewholder.mProfile.setImageResource(mImages.get(position));
                }
            } else {
                lViewholder.mProfile.setImageResource(mImages.get(position));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mStudentList.size();
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
        TextView mLabel, mRank, mProgress, mSchool;
        ImageView mProfile, mYourProfile, mCrown;
        LinearLayout mBackPannel;
    }
}

