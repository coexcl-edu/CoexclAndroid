package com.home.coexcleducation.ui.onboarding

import android.content.Intent
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
import com.google.android.material.tabs.TabLayout
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.registration.LoginActivity
import com.home.coexcleducation.ui.registration.SignupActivity
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.onboarding_layout.*

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_layout)

        ViewUtils().setWindowBackground(this)


        login.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            finish()
        }

        loadShowCaseViewPager(0)
    }

    private fun loadShowCaseViewPager(currentScreen: Int) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (i in 0..2) {
            adapter.addFragment("", ShowCaseFragment(i))
        }

        viewpager.adapter = adapter
        tab_layout_indicator.setupWithViewPager(viewpager, true)

        viewpager.setCurrentItem(currentScreen, true)

        tab_layout_indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
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


}