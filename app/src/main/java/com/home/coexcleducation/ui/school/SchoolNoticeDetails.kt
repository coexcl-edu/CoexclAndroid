package com.home.coexcleducation.ui.school

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.videos.VideoPlayerYoutube
import com.home.coexcleducation.utils.Constants
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.Helper
import com.home.coexcleducation.utils.Helper.extractYTId
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.school_activity_details.*
import java.text.SimpleDateFormat
import java.util.*

class SchoolNoticeDetails : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener{

    lateinit var mNotice : HashMap<String, String>
    var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var lDateformat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    private var youTubeView: YouTubePlayerView? = null
    private var youTubePlayer: YouTubePlayer? = null
    var videoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.school_activity_details)
        ViewUtils().setWindowBackground(this)

        youTubeView = findViewById(R.id.youtube_view)

        mNotice = intent.getSerializableExtra("notice") as HashMap<String, String>

        back_button.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        setValue()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    private fun setValue() {
        val lDate = inputFormat.parse(mNotice.get("date"))
        date.text = lDateformat.format(lDate)
//        teacher.text = mNotice[""]
        notice_body.text = mNotice["notice"]
        notice_title.text = mNotice["title"]

        var imageUrl = mNotice["imageurl"]
        if (!imageUrl.isNullOrEmpty()) {
            if (imageUrl.contains("youtube")) {
                videoUrl = imageUrl
                youtube_view.visibility = View.VISIBLE
                youTubeView!!.initialize(Constants.APIKEY_YOUTUBE, this)
//                youTubePlayer!!.loadVideo(extractYTId(videoUrl))
            } else {
                youtube_view.visibility = View.GONE
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(notice_image);
            }
        }

    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            youTubePlayer = player
            player!!.loadVideo(extractYTId(videoUrl))
            player!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "SchoolActivity", "Video Played")
        }

    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        errorReason: YouTubeInitializationResult?
    ) {
        if (errorReason!!.isUserRecoverableError) {
            errorReason!!.getErrorDialog(this, 1).show()
        } else {
            val errorMessage = "error"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}