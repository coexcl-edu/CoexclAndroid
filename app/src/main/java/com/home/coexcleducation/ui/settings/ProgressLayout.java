package com.home.coexcleducation.ui.settings;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatActionListener;
import com.freshchat.consumer.sdk.FreshchatMessage;
import com.home.coexcleducation.R;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.ui.adaptar.LeaderBoardAdaptar;
import com.home.coexcleducation.utils.ApiUtilty;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl;
import com.home.coexcleducation.utils.ViewUtils;
import com.ramijemli.percentagechartview.PercentageChartView;

import java.util.HashMap;
import java.util.List;

public class ProgressLayout extends AppCompatActivity {

    PercentageChartView mChart;
    ListView mTopStudent;
    ImageView mBack;
    TextView mTopStudentHeader;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_layout);
        new ViewUtils().setWindowBackground(this);
        mChart = findViewById(R.id.chart);
        mBack = findViewById(R.id.back);
        mTopStudent = findViewById(R.id.leader_borad_list);
        mTopStudentHeader = findViewById(R.id.topStudent);
        mProgressBar = findViewById(R.id.progress_bar);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                mChart.setProgress(UserDetails.getInstance().getProgressReport(), true);
            }
        }, 400);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewUtils().exitActivityToRight(ProgressLayout.this);
            }
        });

        mTopStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        new FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Progress Report");
        new FetchLeaderBoard().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new ViewUtils().exitActivityToRight(ProgressLayout.this);
    }

    class FetchLeaderBoard extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String lRes = new ApiUtilty().fetchLeaderBoard(ProgressLayout.this).getResponse();
                CoexclLogs.errorLog("LederBorad", "lRes - "+lRes);
                return lRes;
            } catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            try {
                if (!result.equals("")) {
                    ObjectMapper lMapper = new ObjectMapper();
                    List<HashMap<String, Object>> lResponseObject = lMapper.readValue(result, List.class);
                    if(!lResponseObject.isEmpty()) {
                        LeaderBoardAdaptar lLeaderBoardAdaptar = new LeaderBoardAdaptar(ProgressLayout.this, lResponseObject);
                        mTopStudent.setAdapter(lLeaderBoardAdaptar);
                        mTopStudentHeader.setVisibility(View.VISIBLE);
                    } else
                        mTopStudentHeader.setVisibility(View.GONE);
                } else {
                    mTopStudentHeader.setVisibility(View.GONE);
                    new ViewUtils().displayToast("Unable to fetch top student list", "failure", ProgressLayout.this, "");
                }
            } catch (Exception e) {
                mTopStudentHeader.setVisibility(View.GONE);
                    e.printStackTrace();
            }
        }
    }

}
