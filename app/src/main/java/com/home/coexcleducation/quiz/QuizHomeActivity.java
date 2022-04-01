package com.home.coexcleducation.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.home.coexcleducation.R;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.quiz.api.Api;
import com.home.coexcleducation.quiz.api.ApiCount;
import com.home.coexcleducation.quiz.api.QuizQuestions;
import com.home.coexcleducation.quiz.api.Result;
import com.home.coexcleducation.quiz.question.Question;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl;
import com.home.coexcleducation.utils.PreferenceHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class QuizHomeActivity extends AppCompatActivity {
	Button start;
	Button filter;
	ProgressBar progressBar;
	Question q;
	String difficulty;
	String category;
	ImageView mBack;

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.home_start) {
				progressBar.setVisibility(View.VISIBLE);
				q = new Question(getApplicationContext());
				view.setClickable(false);
//				fetchQuestionCount();
				fetchQuestionAPI(10);
				new FirebaseAnalyticsCoexcl().logFirebaseEvent(QuizHomeActivity.this, "", "Quiz", "Quiz started");
			} else if (view.getId() == R.id.home_filter) {
				Intent intent = new Intent(getApplicationContext(), ChooseQuizCategoryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		start = findViewById(R.id.home_start);
		filter = findViewById(R.id.home_filter);
		progressBar = findViewById(R.id.progressBar2);
		mBack = findViewById(R.id.back_button);
		start.setOnClickListener(onClickListener);
		filter.setOnClickListener(onClickListener);

		Window window = getWindow();
		window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		category = sharedPrefs.getString(
				getString(R.string.category_key),
				getString(R.string.medium_value)
		);


		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private String getDifficulty() {
		try {
			String lClass = UserDetails.getInstance().getClassName();
			CoexclLogs.errorLog("TAG", "difficulty- class - "+lClass);
			if(lClass != null && !lClass.equals("")) {
				if(lClass.equals("6") || lClass.equals("7") || lClass.equals("9"))  {
					return "easy";
				} else if (lClass.equals("9") || lClass.equals("10"))  {
					return "medium";
				} else {
					return "hard";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "easy";
	}


	private void fetchQuestionCount() {
		int category_value = Integer.valueOf(category);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Api.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		Api api = retrofit.create(Api.class);
		Call<ApiCount> call = api.getQuizQuestions(category_value);
		call.enqueue(new Callback<ApiCount>() {
			@Override
			public void onResponse(Call<ApiCount> call, Response<ApiCount> response) {
				switch (difficulty) {
					case "easy":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalEasyQuestionCount());
						break;
					case "medium":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalMediumQuestionCount());
						break;
					case "hard":
						fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalHardQuestionCount());
						break;
				}
			}

			@Override
			public void onFailure(Call<ApiCount> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
			}
		});
	}

	public void fetchQuestionAPI(int categoryCount) {
		difficulty = getDifficulty();
		CoexclLogs.errorLog("TAG", "difficulty - "+difficulty);
		int category_value = PreferenceHelper.getSharedPreference(QuizHomeActivity.this).getInt("category",0);
		Log.e("Question", "category_value - "+category_value);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Api.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		Api api = retrofit.create(Api.class);
		Call<QuizQuestions> call = api.getQuizQuestions("url3986", categoryCount >= 10 ? 10 : categoryCount - 1, difficulty, category_value);
		call.enqueue(new Callback<QuizQuestions>() {
			@Override
			public void onResponse(Call<QuizQuestions> call, Response<QuizQuestions> response) {

				Log.v("url-----", call.request().url().toString());


				QuizQuestions quizQuestions = response.body();
				Log.v("url-----", quizQuestions.getResults().toString());

				if (quizQuestions.getResponseCode() == 0) {

					q.results = quizQuestions.getResults();

					if (q.results != null) {
						for (Result r : q.results) {
							try {
								q.question.add(java.net.URLDecoder.decode(r.getQuestion(), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							Random random = new Random();
							// For Boolean Type Questions, only 2 possible options (True/False)
							// For multiple choices, 4 options are required.
							int ran = r.getType().equals("boolean")
									? random.nextInt(2)
									: random.nextInt(4);
							setOptions(r, ran);
							q.Answer.add(ran + 1);
						}
						Log.v("answers", q.Answer.toString());
					}
				}
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
				Intent intent = new Intent(QuizHomeActivity.this, QuizActivity.class);
				intent.putExtra("question", q);
				startActivity(intent);
			}

			@Override
			public void onFailure(Call<QuizQuestions> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.INVISIBLE);
				start.setClickable(true);
			}
		});
	}

	void setOptions(Result r, int ran) {
		List<String> wrong;
		switch (ran) {
			case 0:
				try {
					q.optA.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Options C, D are not applicable for Boolean Type Questions.
				if (r.getType().equals("boolean")) {
					q.optC.add("false");
					q.optD.add("false");
					return;
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					q.optB.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Options C, D are not applicable for Boolean Type Questions.
				if (r.getType().equals("boolean")) {
					q.optC.add("false");
					q.optD.add("false");
					return;
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					q.optC.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					q.optD.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				wrong = r.getIncorrectAnswers();
				try {
					q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					q.optC.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		filter.setText(PreferenceHelper.getSharedPreference(QuizHomeActivity.this).getString("categoryName","Choose Category"));
	}
}