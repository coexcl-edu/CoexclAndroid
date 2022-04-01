package com.home.coexcleducation.ui.fanfacts

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.tabs.TabLayout
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.QuizSchemeJdo
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.ApiUtilty
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.Helper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.fun_fact_activity.*
import kotlinx.android.synthetic.main.onboarding_layout.*
import kotlinx.android.synthetic.main.onboarding_layout.tab_layout_indicator
import kotlinx.android.synthetic.main.onboarding_layout.viewpager

class FunFactsActivity : AppCompatActivity(), ViewPager.PageTransformer {

    lateinit var dialog : Dialog
    lateinit var mContext : Context
    lateinit var listOfMap : List<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fun_fact_activity)

        mContext = this
        val window: Window = window
        val background = resources.getDrawable(R.drawable.app_background_gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(resources.getColor(android.R.color.transparent))
        window.setBackgroundDrawable(background)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        FetchFunFacts().execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    private fun loadShowCaseViewPager(currentScreen: Int) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (map in listOfMap) {
            adapter.addFragment("", FunFactShowCaseFragment(map))
        }

        viewpager.adapter = adapter
        tab_layout_indicator.setupWithViewPager(viewpager, true)

        viewpager.setCurrentItem(currentScreen, true)


        tab_layout_indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Helper().playSound(this@FunFactsActivity)
                viewpager.currentItem = tab!!.position
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    supportFragmentManager.popBackStack()
                }
                ft.commit()
            }
        })

    }

    class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
        var mFragmentList: ArrayList<Pair<String, Fragment>> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position).second
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(title: String, fragment: Fragment) {
            mFragmentList.add(Pair(title, fragment));
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentList.get(position).first
        }

    }

    override fun transformPage(page: View, position: Float) {
        page.pivotX = (if (position < 0f) page.width.toFloat() else 0f)
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position

        if (Math.abs(position) <= 0.5) {
            page.scaleY = Math.max(0.4f, 1 - Math.abs(position))
        } else if (Math.abs(position) <= 1) {
            page.scaleY = Math.max(0.4f, Math.abs(position))
        }
    }

    fun constructRequestPayload(): String {
        var mHashMap = HashMap<String, Any>()
        mHashMap.put("count", 15)
        return Helper().constructJson(mHashMap)
    }



    inner class FetchFunFacts : AsyncTask<String, String, String>() {

        val lMapper = ObjectMapper()

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Loading content...", mContext)
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): String {
            return try {
                CoexclLogs.errorLog("FetchFunFacts", "FetchFunFacts req - " + constructRequestPayload())
                var httphelper = ApiUtilty().fetchFunFacts(this@FunFactsActivity, constructRequestPayload())
                var response = httphelper.response
                CoexclLogs.errorLog("FetchFunFacts", "FetchFunFacts req - " + httphelper.url)
                CoexclLogs.errorLog("FetchFunFacts", "FetchFunFacts req - " + httphelper.payload)
                CoexclLogs.errorLog("FetchFunFacts", "FetchFunFacts response - ${httphelper.response}")

                response
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(dialog != null && dialog.isShowing)
                dialog.dismiss()

            try {
                var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                if(lResponseObject.containsKey("data")) {
                    var lData = lResponseObject.get("data") as HashMap<String, String>
                    listOfMap = lData.get("funfacts") as List<HashMap<String, String>>
                    viewpager.setPageTransformer(true, this@FunFactsActivity)

                    loadShowCaseViewPager(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }

    }

}
