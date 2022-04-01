package com.home.coexcleducation.ui.quiz

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.*
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.QuizSchemeJdo
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.*
import com.ramijemli.percentagechartview.PercentageChartView
import kotlinx.android.synthetic.main.quiz_activity.*
import java.util.concurrent.TimeUnit

class QuizActivityOld : AppCompatActivity() {

    lateinit var dialog : Dialog
    lateinit var preferences : SharedPreferences
    lateinit var context: Context
    lateinit var desc : String
    var quiz_duration : Int = 1
    var incorrectAnswerCount : Int = 0
    var correctAnswerCount : Int = 0
    var currentQuestion : Int = 0
    var rewardAmount : Int = 0
    var quizStarted : Boolean = false
    var quizReady : Boolean = false
    var answerSheet = HashMap<Int, Boolean>()
    private var lListOfQuiz = arrayListOf<QuizSchemeJdo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)
        context = this
        ViewUtils().setWindowBackground(this)
        preferences = PreferenceHelper.getSharedPreference(context)
        desc = intent.getStringExtra("subject")!!
        ViewUtils().setWindowBackground(this)
        quiz_duration = 15
        quiz_subject.text = desc

        back_button.setOnClickListener{
            exitActivity()
        }

        start_quiz.setOnClickListener{
            if(quizReady) {
                quizStarted = true
                startCounter()
                startQuiz(currentQuestion)
            } else {
                ViewUtils().displayToast("Quiz is not ready, try again!", "failure", this, "")
            }
        }

        previous.setOnClickListener{
            currentQuestion--
            changeQuestion(currentQuestion)
        }

        next.setOnClickListener{
            updateResult(currentQuestion)
            currentQuestion++
            changeQuestion(currentQuestion)
        }

        submitandfinish.setOnClickListener{
            quizStarted = false
            rewardAmount = correctAnswerCount
            showResult()
        }

        FetchQuestion().execute()
    }

    fun startCounter() {
        var quizDurationInMilliSec = lListOfQuiz.size * 60000
        object : CountDownTimer(quizDurationInMilliSec.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.setText(String.format("%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " mins");
            }

            override fun onFinish() {
                timer.text = "Time up"
                quizStarted = false
                rewardAmount = correctAnswerCount
                showResult()
            }
        }.start()
    }

    override fun onBackPressed() {
        exitActivity()
    }

    private fun startQuiz(count: Int) {
        general_instructions.visibility = View.GONE
        quiz_subject.visibility = View.VISIBLE
        submitandfinish.visibility = View.VISIBLE
        question_layout.visibility = View.VISIBLE
        ViewUtils().moveViewToUp(question_layout, 500)
        changeQuestion(count)
    }

    private fun changeQuestion(count: Int) {
        CoexclLogs.errorLog("Quiz", "count ---- $count")
        if(count == 0) {
            previous.visibility = View.GONE
        } else {
            previous.visibility = View.VISIBLE
        }

        if(lListOfQuiz.size == count) {
            next.visibility = View.GONE
        } else {
            next.visibility = View.VISIBLE
        }

        if(count < lListOfQuiz.size) {
            countOfQuestion.text = (count+1).toString()+"/"+lListOfQuiz.size+" Questions"
            setQuestion(count)
        } else {
            next.visibility = View.GONE
            Toast.makeText(this,"Test Completed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateResult(count : Int) {

        var question = lListOfQuiz.get(count)
        var answeredOption = ""
        when (answerGroup.checkedRadioButtonId) {
            answer1.id -> answeredOption = "A"
            answer2.id -> answeredOption = "B"
            answer3.id -> answeredOption = "C"
            answer4.id -> answeredOption = "D"
        }
        CoexclLogs.errorLog("TAG", "Correct -- $answeredOption")
        CoexclLogs.errorLog("TAG", "answeredOption -- " + question.result)
        if(question.result == answeredOption)  {
            correctAnswerCount++
            answerSheet[count] = true
            CoexclLogs.errorLog("TAG", "Correct")
        } else {
            incorrectAnswerCount++
            answerSheet[count] = false
            CoexclLogs.errorLog("TAG", "In Correct")
        }
    }

    private fun setQuestion(count: Int) {
        answerGroup.clearCheck()
        question.text = lListOfQuiz[count].question
        answer1.text = lListOfQuiz[count].answer1
        answer2.text = lListOfQuiz[count].answer2
        answer3.text = lListOfQuiz[count].answer3
        answer4.text = lListOfQuiz[count].answer4
    }

    private fun exitActivity() {
        if(quizStarted) {
            displayAlertDialogOnQuiting("Want to Quit Quiz?", "No", "Yes")
        } else {
            ViewUtils().exitActivityToRight(this as Activity)
            finish()
        }
    }

    private fun displayAlertDialogOnQuiting(alertmessage: String, leftbtntitle: String, rightbtntitle: String) {
        try {
            val dialog = Dialog(this@QuizActivityOld, R.style.DialogCustomTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_popup)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val window = dialog.window
            window?.setGravity(Gravity.CENTER)
            window?.setLayout((getWindow().peekDecorView().width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            val lAlterMessage = dialog.findViewById<TextView>(R.id.Alert_Message)
            val leftBtnText = dialog.findViewById<TextView>(R.id.Dialog_LeftBtn)
            val rightBtnText = dialog.findViewById<TextView>(R.id.Dialog_RightBtn)
            val msg = dialog.findViewById<TextView>(R.id.Alert_Title)
            val lYesButton = dialog.findViewById<LinearLayout>(R.id.ConfirmLayout)
            val lNoButton = dialog.findViewById<LinearLayout>(R.id.CancelLayout)
            lAlterMessage.text = alertmessage
            leftBtnText.text = leftbtntitle
            rightBtnText.text = rightbtntitle
            msg.text = "Quit"
            lNoButton.setOnClickListener {
                dialog.dismiss()
            }
            lYesButton.setOnClickListener {
                dialog.dismiss()
                ViewUtils().exitActivityToRight(this)
                finish()
            }
            dialog.show()
        } catch (e: Exception) {
            CoexclLogs.printException(e);
        }
    }


    fun showResult() {
        try {
            val dialog = Dialog(this@QuizActivityOld, R.style.DialogCustomTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.quiz_result_pop_layout)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val window = dialog.window
            window?.setGravity(Gravity.CENTER)
            window?.setLayout((getWindow().peekDecorView().width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            val lAlterMessage = dialog.findViewById<TextView>(R.id.Alert_Message)
            val leftBtnText = dialog.findViewById<TextView>(R.id.Dialog_LeftBtn)
            val msg = dialog.findViewById<TextView>(R.id.Alert_Title)
            val chart = dialog.findViewById<PercentageChartView>(R.id.percentageChart)
            val lNoButton = dialog.findViewById<LinearLayout>(R.id.CancelLayout)

            var builder = StringBuilder()
            builder.append("$desc").append("\n").append("\n")
            builder.append("Correct : $correctAnswerCount").append("\n")
            builder.append("Incorrect : $incorrectAnswerCount").append("\n")

//            var percentage = (correctAnswerCount/lListOfQuiz.size)*100
            var percentage = (correctAnswerCount.toDouble() / lListOfQuiz.size) * 100

            CoexclLogs.errorLog("QuizActivity" , "percentage - "+percentage)

            chart.setProgress(percentage.toFloat(), true)

            lAlterMessage.text = builder.toString()
            leftBtnText.text = "Dismiss"

            msg.text = "Quiz Result"
            lNoButton.setOnClickListener {
                dialog.dismiss()
                exitActivity()
            }

            dialog.show()
        } catch (e: Exception) {
            CoexclLogs.printException(e);
        }
    }

    fun constructRequestPayload(): String {
        var mHashMap = HashMap<String, Any>()
        mHashMap.put("count", 15)
        mHashMap.put("schoolcode", UserDetails.getInstance().schoolCode)
        return Helper().constructJson(mHashMap)
    }


    inner class FetchQuestion : AsyncTask<String, String, String>() {

        val lMapper = ObjectMapper()

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Loading questions...", context)
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                CoexclLogs.errorLog("QuizeActivity", "Quiz req - " + constructRequestPayload())
                var httphelper = ApiUtilty().fetchQuiz(this@QuizActivityOld, constructRequestPayload())
                var response = httphelper.response
                CoexclLogs.errorLog("QuizeActivity", "Quiz req - " + httphelper.url)
                CoexclLogs.errorLog("QuizeActivity", "Quiz req - " + httphelper.payload)
                CoexclLogs.errorLog("QuizeActivity", "Quiz response - ${httphelper.response}")

                try {
                    var lResponseObject = lMapper.readValue(httphelper.response, List::class.java)
                        var listOfMap = lResponseObject as List<HashMap<String, String>>
                        for (map in listOfMap) {
                            var lJdo = QuizSchemeJdo()
                            var options = map.get("options") as HashMap<String, String>
                            lJdo.answer1 = options.get("A")
                            lJdo.answer2 = options.get("B")
                            lJdo.answer3 = options.get("C")
                            lJdo.answer4 = options.get("D")
                            lJdo.question = map.get("question")
                            lJdo.result = map.get("answer")
                            lListOfQuiz.add(lJdo)
                        }
                } catch (e: Exception) {
                    e.printStackTrace()

                }
                return response
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(dialog != null && dialog.isShowing)
                dialog.dismiss()
            quizReady = true
        }

    }


//    inner class UpdateCreditAmount : AsyncTask<Integer, String, String>() {
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            dialog = ViewUtils().displayProgressDialog("Submitting Quiz...", context)
//            dialog.show()
//        }
//
//        override fun doInBackground(vararg params: Integer?): String {
//            try {
//                val httpHelper = HttpHelper()
//                httpHelper.requestType = HttpMethod.GET.name
//                httpHelper.url = ApiConstant.WALLET_BALANCE_UPDATE_API+"&uid="+preferences.getString(PreferenceHelper.USER_NAME, "")+"&amount="+rewardAmount+"&wtype=wallet01&&btype=credit&&desc=credit_against_quiz"
//                var response = HttpClient(this@QuizActivity).executeHttpRequest(httpHelper).response
//                EPathsalaLogs.errorLog("QuizActivity", "QuizActivity req - " + httpHelper.url)
//                EPathsalaLogs.errorLog("QuizActivity", "QuizActivity response - " + response)
//                return response
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return ""
//            }
//
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            if(dialog != null && dialog.isShowing)
//                dialog.dismiss()
//            showResult()
//        }
//    }
}