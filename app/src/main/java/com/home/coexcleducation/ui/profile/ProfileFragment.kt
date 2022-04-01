package com.home.coexcleducation.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.ui.registration.ChangePasswordActivity
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.profile_chart.*
import kotlinx.android.synthetic.main.profile_update_view.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileLayout: LinearLayout
    private lateinit var mMenuImportPlace: View
    private lateinit var mEditProfile: RelativeLayout
    lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        profileLayout = root.findViewById(R.id.profile_layout);
        mMenuImportPlace = root.findViewById(R.id.menuPlace)
        mEditProfile = root.findViewById(R.id.popupLayout)

        val window: Window = requireActivity().window
        val background = resources.getDrawable(R.drawable.appcolour_gradient_for_background)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(resources.getColor(android.R.color.transparent))
        window.setBackgroundDrawable(background)

        root.changePassword.setOnClickListener{
            var lIntent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            lIntent.putExtra("action", "changePassword")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }


        mEditProfile.setOnClickListener(View.OnClickListener {
            val popup = PopupMenu(requireActivity(), mMenuImportPlace)
//            popup.getMenuInflater().inflate(R.menu.profile_edit, popup.getMenu());
            popup.menu.add(Menu.NONE, R.id.edit_profile, Menu.NONE, "Edit profile")
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_profile -> {
                        val lCustomerImportIntent = Intent(
                            activity,
                            ProfileUpdateActivity::class.java
                        )
                        startActivity(lCustomerImportIntent)
                        requireActivity().overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_left
                        )
                    }
                }
                true
            }
            popup.show()
        })


//        var imageUrl = UserDetails.getInstance().profileImage
//        Glide.with(requireActivity())
//            .load(imageUrl)
//            .centerCrop()
//            .circleCrop()
//            .placeholder(R.drawable.kid_place_holder)
//            .into(root.front_profile);
//
//
//        Glide.with(requireActivity())
//            .load(imageUrl)
//            .centerCrop()
//            .placeholder(R.drawable.kid_full)
//            .into(root.back_profile);



        ViewUtils().moveViewToUp(profileLayout, 500)
        FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "ProfileView")
        return root
    }

    override fun onResume() {
        super.onResume()
        setValues()
    }

    private fun setValues() {
        var lUserDetails = UserDetails.getInstance()
        root.name.text  = if(!lUserDetails.name.isNullOrEmpty()) lUserDetails.name else "N.A"
        root.email.text  = if(!lUserDetails.email.isNullOrEmpty()) lUserDetails.email else "N.A"
        root.phone.text  = if(!lUserDetails.mobile.isNullOrEmpty()) lUserDetails.mobile else "N.A"
        root.school_class.text  = "Class "+if(!lUserDetails.className.isNullOrEmpty()) lUserDetails.className else "N.A"
        root.blood_group.text = if(!lUserDetails.bloodGroup.isNullOrEmpty()) lUserDetails.bloodGroup else "N.A" +" group"
        root.fav_sport.text = if(!lUserDetails.favSports.isNullOrEmpty()) lUserDetails.favSports else "N.A"
        root.gender.text  = if(!lUserDetails.gender.isNullOrEmpty()) lUserDetails.gender else "N.A"
        root.hobby.text  = if(!lUserDetails.hobby.isNullOrEmpty()) lUserDetails.hobby else "N.A"

        root.school_name.text  = if(!lUserDetails.schoolName.isNullOrEmpty()) lUserDetails.schoolName else "N.A"
        root.school_id.text  = if(!lUserDetails.schoolCode.isNullOrEmpty())  "########" +lUserDetails.schoolCode.substring(lUserDetails.schoolCode.length - 2) else "N.A"
        root.school_address.text  = if(!lUserDetails.city.isNullOrEmpty()) lUserDetails.city else "N.A" + ", " +  lUserDetails.state

        root.parent_name.text  = if(!lUserDetails.fatherName.isNullOrEmpty()) lUserDetails.fatherName else "N.A"
        root.parent_phone.text  = if(!lUserDetails.parentMobile.isNullOrEmpty()) lUserDetails.parentMobile else "N.A"
        root.mother_name.text  = if(!lUserDetails.motherName.isNullOrEmpty()) lUserDetails.motherName else "N.A"

        root.premium.text =  if(lUserDetails.subscribed)  "Premium Account"  else "Free User"

        Glide.with(this)
            .load(UserDetails.getInstance().profileAvatar)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.avatar2)
            .into(root.front_profile);
        Glide.with(this)
            .load(UserDetails.getInstance().profileAvatar)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.avatar2)
            .into(root.back_profile);

        calculateProfilePercentage()
    }

    private fun calculateProfilePercentage() {

        try {
            var lUserDetails = UserDetails.getInstance()
            var countOfAddedNotDataItem = 0

            if (lUserDetails.name != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.email != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.mobile != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.bloodGroup != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.className != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.favSports != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.favSubject != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.hobby != "") {
                countOfAddedNotDataItem++
            }
            if (lUserDetails.gender != "") {
                countOfAddedNotDataItem++
            }
            var personalPercentage = 11 * countOfAddedNotDataItem + 1
            CoexclLogs.errorLog(
                "ProfileFragment",
                "Personal Percentage  countOfAddedNotDataItem - $countOfAddedNotDataItem"
            )
            CoexclLogs.errorLog("ProfileFragment", "Personal Percentage - $personalPercentage")

            var countOfParentItem = 0

            if (!lUserDetails.parentMobile.isNullOrEmpty() && lUserDetails.parentMobile != "null") {
                countOfParentItem++
            }
            if (lUserDetails.fatherName != "") {
                countOfParentItem++
            }
            if (lUserDetails.motherName != "") {
                countOfParentItem++
            }

            var parentalPercentage = (33 * countOfParentItem) + 1
            CoexclLogs.errorLog("ProfileFragment", "parental Percentage - $countOfParentItem")
            CoexclLogs.errorLog("ProfileFragment", "parental Percentage - $parentalPercentage")

            chart.setProgress(personalPercentage.toFloat(), true)
            parentChart.setProgress(parentalPercentage.toFloat(), true)
            chartSchool.setProgress(100f, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}