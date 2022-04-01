package com.home.coexcleducation.quiz;

import android.content.SharedPreferences;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.freshchat.consumer.sdk.activity.CategoryListActivity;
import com.home.coexcleducation.R;
import com.home.coexcleducation.ui.adaptar.ChooseCategoryAdaptar;
import com.home.coexcleducation.utils.PreferenceHelper;

import java.util.Arrays;
import java.util.List;

public class ChooseQuizCategoryActivity extends AppCompatActivity {

    ListView mListView;
    SharedPreferences.Editor mPreference;
    AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        mPreference = PreferenceHelper.getSharedPreference(this).edit();
        mListView = findViewById(R.id.listview);
        back = findViewById(R.id.back);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        List<String> quizName = Arrays.asList(getResources().getStringArray(R.array.category_quiz));
        ChooseCategoryAdaptar lAdaptar = new ChooseCategoryAdaptar(this, quizName);
        mListView.setAdapter(lAdaptar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    mPreference.putString("categoryName", quizName.get(position)).commit();
                    switch (position) {
                        case 0:
                            mPreference.putInt("category", 0).commit();
                            break;
                        case 1:
                            mPreference.putInt("category", 24).commit();
                            break;
                        case 2:
                            mPreference.putInt("category", 23).commit();
                            break;
                        case 3:
                            mPreference.putInt("category", 21).commit();
                            break;
                        case 4:
                            mPreference.putInt("category", 22).commit();
                            break;
                        case 5:
                            mPreference.putInt("category", 9).commit();
                            break;
                        case 6:
                            mPreference.putInt("category", 11).commit();
                            break;

                        case 7:
                            mPreference.putInt("category", 10).commit();
                            break;
                        case 8:
                            mPreference.putInt("category", 12).commit();
                            break;
                        case 9:
                            mPreference.putInt("category", 13).commit();
                            break;
                        case 10:
                            mPreference.putInt("category", 14).commit();
                            break;
                        case 11:
                            mPreference.putInt("category", 15).commit();
                            break;
                        case 12:
                            mPreference.putInt("category", 16).commit();
                            break;
                        case 13:
                            mPreference.putInt("category", 17).commit();
                            break;
                        case 14:
                            mPreference.putInt("category", 18).commit();
                            break;

                        case 15:
                            mPreference.putInt("category", 19).commit();
                            break;
                        case 16:
                            mPreference.putInt("category", 20).commit();
                            break;
                        case 17:
                            mPreference.putInt("category", 25).commit();
                            break;
                        case 18:
                            mPreference.putInt("category", 26).commit();
                            break;
                        case 19:
                            mPreference.putInt("category", 27).commit();
                            break;
                        case 20:
                            mPreference.putInt("category", 28).commit();
                            break;
                        case 21:
                            mPreference.putInt("category", 29).commit();
                            break;
                        case 22:
                            mPreference.putInt("category", 30).commit();
                            break;
                        case 23:
                            mPreference.putInt("category", 31).commit();
                            break;
                        case 24:
                            mPreference.putInt("category", 32).commit();
                            break;

                        default:
                            mPreference.putInt("category", 0).commit();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                }

        });
    }

//    public static class QuizPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
//        @Override
//        public void onCreate(@Nullable Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.settings_main);
//            Preference list_preference_difficulty = findPreference(getString(R.string.difficulty_key));
//            Preference list_preference_category = findPreference(getString(R.string.category_key));
//            list_preference_difficulty.setOnPreferenceChangeListener(this);
//            list_preference_category.setOnPreferenceChangeListener(this);
//            bindPreferenceSummaryToValue(list_preference_difficulty);
//            bindPreferenceSummaryToValue(list_preference_category);
//        }
//
//        private void bindPreferenceSummaryToValue(Preference preference) {
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
//            String value = sharedPreferences.getString(preference.getKey(), " ");
//            onPreferenceChange(preference, value);
//
//        }
//
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringValue = newValue.toString();
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(stringValue);
//            if (prefIndex >= 0) {
//                CharSequence[] labels = listPreference.getEntries();
//                preference.setSummary(labels[prefIndex]);
//            }
//            return true;
//        }
//
//
//    }

}
