package com.home.coexcleducation.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.coexcleducation.R;
import com.home.coexcleducation.httphelper.HttpHelper;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.quiz.question.Question;
import com.home.coexcleducation.ui.registration.SignUpUtils;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl;
import com.home.coexcleducation.utils.Utilty;
import com.home.coexcleducation.utils.ViewUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuizActivity extends AppCompatActivity {

	Question qAndA = new Question();

	int ques, score, ans, nextC;
	float percentage;
	boolean submit;
	Button submitButton;
	TextView mTimer;

	ArrayList<Integer> Answers;


	String q_nos;

	@BindView(R.id.q_numbers)
	TextView q_no;

	@BindView(R.id.question)
	TextView questions;

	@BindView(R.id.optionA)
	RadioButton opA;

	@BindView(R.id.optionB)
	RadioButton opB;

	@BindView(R.id.optionC)
	RadioButton opC;

	@BindView(R.id.optionD)
	RadioButton opD;

	@BindView(R.id.options)
	RadioGroup optionsGroup;

	@BindView(R.id.prev)
	Button prevButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		submitButton = findViewById(R.id.submit);
		mTimer = findViewById(R.id.timer);

		ButterKnife.bind(this);
		Intent i = getIntent();
		qAndA = (Question) i.getSerializableExtra("question");
		q_nos = "Question: " + 1 + "/" + qAndA.question.size();
		questions = findViewById(R.id.question);
		questions.setText("Quiz");
		prevButton.setVisibility(View.GONE);
		Answers = new ArrayList<>();

		ques = -1;
		score = 0;
		ans = 0;
		nextC = 0;
		submit = true;

//		submitButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				clickSubmit(v);
//			}
//		});
		goNext();
		counter();
	}

	public void counter() {
		CountDownTimer lTimer = new CountDownTimer(5*60000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				mTimer.setText(String.format("%02d : %02d ",
						TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
						TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
								TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " mins");

			}

			@Override
			public void onFinish() {
				submit(submitButton);
			}
		};
		lTimer.start();
	}

	public void goNext() {
		try {
			ques++;

			if (ques >= qAndA.question.size()) {
				ques = qAndA.question.size() - 1;
			}

			q_no.setVisibility(View.VISIBLE);
			opA.setVisibility(View.VISIBLE);
			opB.setVisibility(View.VISIBLE);

			if (qAndA.results.get(ques).getType().equals("boolean")) {
				opC.setVisibility(View.GONE);
				opD.setVisibility(View.GONE);
			} else {
				opC.setVisibility(View.VISIBLE);
				opD.setVisibility(View.VISIBLE);
			}

			q_nos = "Question: " + (ques + 1) + "/" + qAndA.question.size();
			q_no.setText(q_nos);
			questions.setText(qAndA.question.get(ques));
			opA.setText(qAndA.optA.get(ques));
			opB.setText(qAndA.optB.get(ques));
			opC.setText(qAndA.optC.get(ques));
			opD.setText(qAndA.optD.get(ques));
			try {
				if (Answers.get(ques) == 1) {
					opA.setChecked(true);
				} else if (Answers.get(ques) == 2) {
					opB.setChecked(true);
				} else if (Answers.get(ques) == 3) {
					opC.setChecked(true);
				} else if (Answers.get(ques) == 4) {
					opD.setChecked(true);
				} else {
					optionsGroup.clearCheck();
				}
			} catch (Exception e) {
				optionsGroup.clearCheck();
			}
		} catch (Exception e) {
			Toast.makeText(QuizActivity.this,"Something went wrong, please choose some other topic", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
		}
	}

	public void clickPrev(View view) {

		int selectedId = optionsGroup.getCheckedRadioButtonId();
		switch (selectedId) {
			case R.id.optionA:
				ans = 1;
				break;
			case R.id.optionB:
				ans = 2;
				break;
			case R.id.optionC:
				ans = 3;
				break;
			case R.id.optionD:
				ans = 4;
				break;
			default:
				ans = 0;
		}
		if (ques >= 0) {
			try {
				Answers.set(ques, ans);
			} catch (Exception e) {
				Answers.add(ques, ans);
			}
		}
		if (ques < qAndA.question.size() - 1) {
			goPrev();
		} else if (ques == qAndA.question.size() - 1) {
			Button button = findViewById(R.id.next);
			button.setVisibility(View.VISIBLE);
			button = findViewById(R.id.submit);
			button.setVisibility(View.INVISIBLE);
			optionsGroup.clearCheck();
			goPrev();
		}
		if (ques == 0)
			prevButton.setVisibility(View.GONE);
		nextC--;
		ans = 0;
	}

	public void goPrev() {
		ques--;

		q_no.setVisibility(View.VISIBLE);
		opA.setVisibility(View.VISIBLE);
		opB.setVisibility(View.VISIBLE);

		if (qAndA.results.get(ques).getType().equals("boolean")) {
			opC.setVisibility(View.GONE);
			opD.setVisibility(View.GONE);
		} else {
			opC.setVisibility(View.VISIBLE);
			opD.setVisibility(View.VISIBLE);
		}

		q_nos = "Question: " + (ques + 1) + "/" + qAndA.question.size();
		q_no.setText(q_nos);
		questions.setText(qAndA.question.get(ques));
		opA.setText(qAndA.optA.get(ques));
		opB.setText(qAndA.optB.get(ques));
		opC.setText(qAndA.optC.get(ques));
		opD.setText(qAndA.optD.get(ques));
		try {
			if (Answers.get(ques) == 1) {
				opA.setChecked(true);
			} else if (Answers.get(ques) == 2) {
				opB.setChecked(true);
			} else if (Answers.get(ques) == 3) {
				opC.setChecked(true);
			} else if (Answers.get(ques) == 4) {
				opD.setChecked(true);
			} else {
				optionsGroup.clearCheck();
			}
		} catch (Exception e) {
			optionsGroup.clearCheck();

		}
	}

	public void clickSubmit(final View view) {
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		alertConfirm.setTitle("Confirm Submission");
		alertConfirm.setMessage("Do you want to submit quiz?");
		alertConfirm.setCancelable(true);
		alertConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertConfirm.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				submit(view);
			}
		});
		AlertDialog dialog = alertConfirm.create();
		dialog.show();
	}

	public void submit(View view) {
		clickNext(view);
		if (submit)
			checkScore();
		submit = false;
		percentage = (float) (score * 100) / qAndA.question.size();
		prevButton.setVisibility(View.INVISIBLE);
		opA.setClickable(false);
		opB.setClickable(false);
		opC.setClickable(false);
		opD.setClickable(false);
		LayoutInflater inflater = getLayoutInflater();
		View alertLayout = inflater.inflate(R.layout.alert_dialog, null);
		final ProgressBar progressBar = alertLayout.findViewById(R.id.circularProgressbar);
		final TextView textView = alertLayout.findViewById(R.id.tv);
		Drawable drawable = getResources().getDrawable(R.drawable.circular);
		progressBar.setMax(qAndA.question.size());
		progressBar.setSecondaryProgress(qAndA.question.size());
		progressBar.setProgress(score);
		progressBar.setProgressDrawable(drawable);
		textView.setText((int) percentage + "%");

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("RESULT");
		alert.setMessage("You scored " + score + " out of " + qAndA.question.size() + " questions.");
		alert.setView(alertLayout);
		alert.setCancelable(false);

		alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		alert.setPositiveButton("View Solutions", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clickSolutions();
			}
		});
		AlertDialog dialog = alert.create();
		dialog.show();
		new UpdateQuizInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Quiz", "Quiz submitted");
	}

	public void clickNext(View view) {

		int selectedId = optionsGroup.getCheckedRadioButtonId();
		switch (selectedId) {
			case R.id.optionA:
				ans = 1;
				break;
			case R.id.optionB:
				ans = 2;
				break;
			case R.id.optionC:
				ans = 3;
				break;
			case R.id.optionD:
				ans = 4;
				break;
			default:
				ans = 0;
		}

		if (ques >= 0) {
			try {
				Answers.set(ques, ans);
			} catch (Exception e) {
				Answers.add(ques, ans);
			}
		}
		if (ques < qAndA.question.size() - 2) {
			optionsGroup.clearCheck();
			goNext();
		} else if (ques == qAndA.question.size() - 2) {
			Button button = findViewById(R.id.next);
			button.setVisibility(View.INVISIBLE);
			button = findViewById(R.id.submit);
			button.setVisibility(View.VISIBLE);
			optionsGroup.clearCheck();
			goNext();
		}
		if (ques > 0)
			prevButton.setVisibility(View.VISIBLE);

		nextC++;
		ans = 0;
	}

	public void checkScore() {
		if (ques != -1)
			for (int i = 0; i < Answers.size(); i++) {
				if (qAndA.Answer.get(i).equals(Answers.get(i))) {
					score++;
				}
			}
	}

	public void clickSolutions() {
		Intent solutions = new Intent(this, SolutionActivity.class);
		solutions.putIntegerArrayListExtra("Answer", Answers);
		solutions.putStringArrayListExtra("Question", (ArrayList<String>) qAndA.question);
		solutions.putStringArrayListExtra("optA", (ArrayList<String>) qAndA.optA);
		solutions.putStringArrayListExtra("optB", (ArrayList<String>) qAndA.optB);
		solutions.putStringArrayListExtra("optC", (ArrayList<String>) qAndA.optC);
		solutions.putStringArrayListExtra("optD", (ArrayList<String>) qAndA.optD);
		solutions.putIntegerArrayListExtra("Answers", (ArrayList<Integer>) qAndA.Answer);
		startActivity(solutions);
		finish();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Back Press is not allowed", Toast.LENGTH_LONG).show();
	}

	private String constructParams() {
		try {
			StringWriter lWrite = new StringWriter();
			HashMap mReqMap = new HashMap<String, Object>();


			HashMap mQuizMap = new HashMap<String, Object>();

			mQuizMap.put("lastattempted" , new Date());
			mQuizMap.put("percent" , percentage);


			mReqMap.put("userid", UserDetails.getInstance().getID());
			mReqMap.put("quizInfo", mQuizMap);

			new ObjectMapper().writeValue(lWrite, mReqMap);
			return lWrite.toString();
		} catch (Exception e) {
			CoexclLogs.printException(e);
			return "";
		}
	}

	class UpdateQuizInfo extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... voids) {
			HttpHelper lHelper = new HttpHelper();
			lHelper.setPayload(constructParams());
			CoexclLogs.errorLog("TAG", "Req from Quiz info - " + lHelper.getPayload());
			lHelper = new SignUpUtils().updateProfile(QuizActivity.this, lHelper);
			CoexclLogs.errorLog("TAG", "Response from Quiz info - " + lHelper.getResponse());
			return lHelper.getResponse();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				if (result != null && !result.isEmpty()) {
					ObjectMapper lMapper = new ObjectMapper();
					HashMap lResponseObject = lMapper.readValue(result, HashMap.class);
					if (lResponseObject.containsKey("response") && lResponseObject.get("response").equals(true)) {
						new Utilty().insertLoginData(result);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
