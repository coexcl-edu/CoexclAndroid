package com.home.coexcleducation.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.home.coexcleducation.R;
import com.home.coexcleducation.quiz.solution.AnswersFragment;
import com.home.coexcleducation.quiz.solution.SimpleFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SolutionActivity extends AppCompatActivity {

    static ArrayList<Integer> Answers;
    static ArrayList<String> Question;
    static ArrayList<String> optA;
    static ArrayList<String> optB;
    static ArrayList<String> optC;
    static ArrayList<String> optD;
    static ArrayList<Integer> Answer;
    static ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        ButterKnife.bind(this);

        Answers = getIntent().getIntegerArrayListExtra("Answer");
        Answer = getIntent().getIntegerArrayListExtra("Answers");
        Question = getIntent().getStringArrayListExtra("Question");
        optA = getIntent().getStringArrayListExtra("optA");
        optB = getIntent().getStringArrayListExtra("optB");
        optC = getIntent().getStringArrayListExtra("optC");
        optD = getIntent().getStringArrayListExtra("optD");
        back = findViewById(R.id.back);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        getSupportFragmentManager().beginTransaction().add(R.id.content , new AnswersFragment()).commit();

//        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//        tabLayout.setupWithViewPager(viewPager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SolutionActivity.this, QuizHomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, QuizHomeActivity.class);
//        startActivity(intent);
        finish();
    }

    public static ArrayList<Integer> getAnswer() {
        return Answers;
    }

    public static ArrayList<Integer> getAnswers() {
        return Answer;
    }

    public static ArrayList<String> getQuestion() {
        return Question;
    }

    public static ArrayList<String> getOptA() {
        return optA;
    }

    public static ArrayList<String> getOptB() {
        return optB;
    }

    public static ArrayList<String> getOptC() {
        return optC;
    }

    public static ArrayList<String> getOptD() {
        return optD;
    }
}