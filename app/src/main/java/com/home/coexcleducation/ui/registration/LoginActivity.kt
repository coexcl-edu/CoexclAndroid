package com.home.coexcleducation.ui.registration

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.home.coexcleducation.MainActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        login.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        create_account.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        ViewUtils().moveViewToUp(out_login_layout, 500)
    }

    inner class Login : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

    }
}