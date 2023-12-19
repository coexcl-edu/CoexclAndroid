package com.home.coexcleducation.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.faq_listing_layout.*

class FAQaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faq_listing_layout)

        ViewUtils().setWindowBackground(this)

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        val list = arrayListOf<String>()
        list.addAll(
                listOf(
                        "Q1. What is DoubtPro all about?",
                        "Q2. How should I join DoubtPro's 24X7 online tutor facility?",
                        "Q3. What type of queries can the DoubtPro Tutor solve?? ",
                        "Q4. The question has a lot of diagrams and hints, so it is difficult to type. Then, how can I put my questions in front of the teacher properly?",
                        "Q5. What language is the tutor comfortable with to understand me better?",
                        "Q6. How many questions can i ask in a day?",
                        "Q7. Can the tutor solve my homework also?",
                        "Q8. What if i have 10-15 doubts at a time?",
                        "Q9. What is the assurance that I will get the exact solution to my problem?",
                        "Q10. What if I asked a few questions and then I left the chat between some doubts without getting a solution?",
                        "Q11. What is average time taken by a tutor to solve a problem?",
                        "Q12. Till what class will I get teacher support from DoubtPro?",
                        "Q13. Can I make a video call to a tutor as well?",
                        "Q14. Is the tutor really a human or a bot?",
                        "Q15. What is included in the premium service and how is it different from the free version ?",
                        "Q16. What is make notes?",
                        "Q17. What is Fun Facts?",
                        "Q18. What is School Activity?",
                        "Q19. What is Live class feature and how is it controlled?",
                        "Q20. What is Daily Quiz?",
                        "Q21. What is Progress Statistics?",
                        "Q22. What is the Source of Video Lesson?",
                        "Q23. Is my Profile data is safe with DoubtPro?"

                )
        )

        val listAnswer = arrayListOf<String>()
        listAnswer.addAll(
                listOf(
                        "Ans: DoubtPro means to connect experts with classes. DoubtPro provides learning application with one of a kind facilities for schools as well as students. We have a really good team in academics which is responsible for solving academic queries of the students and providing good solution to their doubts. Apart from this, the DoubtPro platform also provides many additional features such as study material and virtual schooling, all in one place.",
                        "Ans: To make the application simple and easy to understand for students, we have designed our app in such a way that it is not complicated. The most important features of the app is 24X7 online tutor facility for all its students and that too you can overcome your doubts from IIT'ans. For this purpose we have given a floating chat button in the dashboard (home) or you can also tap on the (ask doubt) option and it will take you directly to the chat platform where you will find that someone is always there to take your questions.\n",
                        "Ans: You can ask your doubts (theory, numerical) related to Physics, Chemistry, Biology, Mathematics and some part of STEM as well.",
                        "Ans: The easiest way to properly put your questions in front of your tutor is that you can click the picture of the problem in good resolution so that it is properly readable. You can then send it directly to the tutor, he will examine and clear the doubts.",
                        "Ans: Teachers are mostly comfortable in English language, to some extent they can explain it in your Hindi language.\n",
                        "Ans: Well, there is no limit to this right now.",
                        "Ans: Well, to learn you have to do your homework yourself. The tutor's job is to help you understand the concepts and solve your doubts. This does not mean that he will do your homework.",
                        "Ans: You can clear all your doubts one by one. There is no problem in this. The teachers are very sociable.",
                        "Ans: The assurance of getting a solution is 100%. As far as the quality of the answer is concerned, you can be assured as it has been solved by the teachers who are IITans.",
                        "Ans: Well, there is no problem in this. Whenever you are online again, you will find your solution.",
                        "Ans: It depends on the complexity of the problem.",
                        "Ans: We have advanced level tutor facilities for classes 6-12.",
                        "Ans: No, in current version we don't support video calls.",
                        "Ans: 100% human. You can also see in diagram cases that you will get handwritten answers. So this is a big proof.",
                        "Ans: All services, including DoubtPro's 24x7 tutor facility, are free until Covid Pandemic ends. However the premium charges are very nominal, and anyone can easily afford such service. For more package details you can visit our website www.doubtpro.com.",
                        "Ans: Make notes is a unique features of DoubtPro where you can make notes whatever you want related to your studies, and then you can save it also for future reference.",
                        "Ans: Fun Facts is another unique features of DoubtPro in which you can explore things beyond your books. You will get updates on Fun Facts every week.",
                        "Ans: School Activity is the list of Notifications which your school has sent for you only. It is a very specific feature and is controlled by your school admin and is directly connected from your school updates. Please note to enjoy this feature, your school should have to be active admin member of DoubtPro.",
                        "Ans: Live Class is another feature which is / can be controlled by your school admin. Here your school or class teacher can conduct Live Classes through zoom meetings, Google Meets etc. and can send notification to you or a specific students of specific class for which the classes are being scheduled.",
                        "Ans: Daily quiz are quiz which are posted by DoubtPro Academics Team on daily basis. It is a specially meant to prepare you for tests and quizzes at school levels. The questions can be mixed from all subjects. The difficulty level of daily quiz increases with class. Also your daily quiz performance can be seen in PROGRESS STATISTICS under SETTINGS option.",
                        "Ans: Progress Statistics is an option under Settings option which measures your Average result Performance on Daily Quiz. Also it shows you top 5 Students in the leader board from your school and who are in your class.",
                        "Ans: In order to see videos of your particular chapter, you need to tap the Lesson Button at bottom of screen (Second to Home Button). \n" +
                                "\n" +
                                "There you will see the Subjects icons , then you need to Tap on the desired subject you wish to study, under that you will find red icons for each chapters , all you need to do is just tap on it and you are there. \n" +
                                "\n" +
                                "Here is the path: \n" +
                                "\n" +
                                "Lesson> select Subject> Select section (Either Notes/ Videos or quiz) > tap on chapters to view.",
                        "Ans: Well, DoubtPro brings you the best of the category video from youtube gallery after applying several quality check Filters. \n" +
                                "\n" +
                                "Note: DoubtPro doesn't claims its Propriotory over the Videos as its meant only for education  purpose and is absolutely. Free.",
                        "Ans: Absolutely Yes. DoubtPro never user your data for any kind of marketing or assessments. Nor your data is reveled anywhere. For details you can see our users Terms and Policy."

                )
        )


        var adapter = ArrayAdapter(this,  R.layout.faq_item_layout,  list)
        faqs.adapter = adapter

        faqs.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val lIntent = Intent(this@FAQaActivity, FaqDetailsActivity::class.java)
            lIntent.putExtra("question", list.get(position))
            lIntent.putExtra("answer", listAnswer.get(position))
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }
}