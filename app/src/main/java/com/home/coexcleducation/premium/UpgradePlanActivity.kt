package com.home.coexcleducation.premium

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_FULL_PRICE
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.tabs.TabLayout
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.Constants
import com.home.coexcleducation.utils.Helper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.plant_upgrade_activity.*
import java.util.*
import kotlin.collections.ArrayList


class UpgradePlanActivity : AppCompatActivity(), PurchasesUpdatedListener {


    lateinit var mContext: Context
    lateinit var mBillingClient: BillingClient
    var selectedPlan = 0
    var mExistingPlanId : String = ""
    var mNewPlanId : String = ""
    var TAG = "UpgradePlanActivity"
    var mSkuList: List<SkuDetails>? = null
    var mMonthlySKU: SkuDetails? = null
    var mAnnualSKU: SkuDetails? = null
    lateinit var lProgressDialog: Dialog
    lateinit var mAckPurchaseListener: AcknowledgePurchaseResponseListener
    var mAlreadyPurchasedProductList = mutableListOf<Purchase>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.plant_upgrade_activity)
        mContext = this
        mBillingClient = BillingClient.newBuilder(mContext).enablePendingPurchases().setListener(this).build()

        ViewUtils().setWindowBackground(this)

        back.setOnClickListener {
            exit()
        }

        monthlyPlanBox.setOnClickListener {
            mMonthlySKU?.let {
                loadSKU(it)
            }
        }

        yearlyPlanBox.setOnClickListener {
            mAnnualSKU?.let{
                loadSKU(it)
            }
        }



        mAckPurchaseListener = AcknowledgePurchaseResponseListener { billingResult: BillingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                CoexclLogs.errorLog(TAG, "Purchase query")
                queryPurchase();
            }
            CoexclLogs.errorLog(TAG, "Acknowledge billingResult ---- " + billingResult.responseCode)
        }

        downgrade.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions"))
            startActivity(browserIntent)
        }

        if (UserDetails.getInstance().subscribed) {
            name.text = "Premium Member"
            downgrade.visibility = View.VISIBLE
        }

        startConnection()

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

    override fun onBackPressed() {
        exit()
        super.onBackPressed()
    }

    fun exit() {
        ViewUtils().exitActivityToRight(this)
        finish()
    }

    fun loadTermAndCondition() {
//        val lIntent = Intent(this@UpgradePlanActivity, ::class.java)
//        lIntent.putExtra("contenttype", "premiumSupportArticle")
//        lIntent.putExtra("header", mFirebaseConfig.getString("terms_and_condition"))
//        lIntent.putExtra("url", getString(R.string.setmore_new_premium_terms_and_condition_url))
//        startActivity(lIntent)
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
    }


    private fun loadSKU(skuDetails: SkuDetails) {
        if(skuDetails != null) {
            if (mAlreadyPurchasedProductList != null && mAlreadyPurchasedProductList.size != 0 && mAlreadyPurchasedProductList[0].skus.size > 0 && mAlreadyPurchasedProductList[0].skus.contains(skuDetails.sku)) {
                showMessage("Plan is already active", Constants.SUCCESS_MESSEGE_TYPE, "")
            } else {
                if (mBillingClient.isReady) {
                    if (mAlreadyPurchasedProductList != null && mAlreadyPurchasedProductList.size != 0 && mAlreadyPurchasedProductList[0].skus.size > 0 && !mAlreadyPurchasedProductList[0].skus[0].equals(skuDetails.sku, ignoreCase = true)) {
                            var existingPlanParam = BillingFlowParams.SubscriptionUpdateParams.newBuilder().setOldSkuPurchaseToken(mAlreadyPurchasedProductList[0].purchaseToken)
                                    .setReplaceSkusProrationMode(IMMEDIATE_AND_CHARGE_FULL_PRICE).build()
                            val flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).setSubscriptionUpdateParams(existingPlanParam).build()
                            mBillingClient.launchBillingFlow(this, flowParams)

                    } else {
                        CoexclLogs.errorLog(TAG, "Loading the sku")
                        val flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
                        mBillingClient.launchBillingFlow(this@UpgradePlanActivity, flowParams)
                    }
                } else {
                    Toast.makeText(mContext, "Client is not ready", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


     private fun loadShowCaseViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
//        for (i in 0..3) {
            adapter.addFragment("", PremiumDisplayFragment(0))
//        }

        viewpager.adapter = adapter
//        tab_layout_indicator.setupWithViewPager(viewpager, true)
        viewpager.setCurrentItem(0, true)
        loadPlans()

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


     private fun startConnection() {
        CoexclLogs.errorLog(TAG, "Starting connection.")
        mBillingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchase()
                    CoexclLogs.errorLog(TAG, "Google Play Service Setup successful. Querying inventory.")
                } else {
                    CoexclLogs.errorLog(TAG, "Error on fetching products")
                }
            }

            override fun onBillingServiceDisconnected() {
                CoexclLogs.errorLog(TAG, "Google Play Service Disconnected.")
            }
        })
    }

    fun queryPurchase() {
        mBillingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, object : PurchasesResponseListener{
            override fun onQueryPurchasesResponse(billingResult: BillingResult, purchases: MutableList<Purchase>) {
                purchases?.let {
                    mAlreadyPurchasedProductList = purchases
                }
                fetchingPlans()
                CoexclLogs.errorLog(TAG, "purchasesResult ---- " + purchases)
            }
        })
    }

    private fun areSubscriptionsSupported(): Boolean {
        val responseCode = mBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS).responseCode
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            CoexclLogs.warningLog(TAG, "areSubscriptionsSupported() got an error response: $responseCode")
        }
        return responseCode == BillingClient.BillingResponseCode.OK
    }


     fun fetchingPlans() {
        if (areSubscriptionsSupported()) {
            val skuList: MutableList<String> = java.util.ArrayList()

            skuList.add(Constants.MONTHLY_SUBSCRIPTION)
            skuList.add(Constants.YEARLY_SUBSCRIPTION)

            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

            mBillingClient.querySkuDetailsAsync(params.build(), object : SkuDetailsResponseListener {
                override fun onSkuDetailsResponse(billingResult: BillingResult, list: MutableList<SkuDetails>?) {
                    CoexclLogs.errorLog(TAG, "querySkuDetailsAsync     " + list.toString())
                    mSkuList = list
                    runOnUiThread{ loadShowCaseViewPager() }

                }
            })

        }
    }




    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        CoexclLogs.errorLog(TAG, "onPurchasesUpdated --" + billingResult!!.responseCode)
        CoexclLogs.errorLog(TAG, "onPurchasesUpdated --$purchases")

        if (billingResult!!.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            UpgradeToPremium().execute(purchases)
        } else {
            if (billingResult!!.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                showMessage("Subscription Already owned with your Play Store Account", Constants.FAILURE_MESSEGE_TYPE, "")
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//                showMessage("Subscription purchase is cancelled ", Constants.FAILURE_MESSEGE_TYPE, "");
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ERROR) {
                showMessage("Subscription purchase is cancelled ", Constants.FAILURE_MESSEGE_TYPE, "")
//                SendingCancellingCause().execute(billingResult)
            }
        }
    }

    fun loadPlans() {
//        selectedPlan = typeOfPlan
        active_plan_yearly.visibility = View.INVISIBLE
        active_plan_monthly.visibility = View.INVISIBLE
        discountPercentage.visibility = View.VISIBLE
        yearlyPlanBox.background = ContextCompat.getDrawable(mContext, R.drawable.brown_border_white_inside_4r)
        monthlyPlanBox.background = ContextCompat.getDrawable(mContext, R.drawable.brown_border_white_inside_4r)
        var activePlan = if (mAlreadyPurchasedProductList.size > 0)  mAlreadyPurchasedProductList.get(0).skus[0] else ""
        CoexclLogs.errorLog(TAG, "SKU in load plans list = "+mSkuList!!.get(1))
        if (mSkuList != null) {
            for (skuDetails in mSkuList!!) {
                CoexclLogs.errorLog(TAG, "SKU in load plans = "+skuDetails.sku)
                    if (skuDetails.sku == Constants.MONTHLY_SUBSCRIPTION) {
                        mMonthlySKU = skuDetails
                        monthly_pricing.text = getPriceFromMacro(skuDetails.getPriceCurrencyCode(), skuDetails.getPriceAmountMicros()) + "/month"
                        actual_monthly_pricing.text = getPriceFromMacro(skuDetails.getPriceCurrencyCode(), skuDetails.getPriceAmountMicros()) + " / mo"
                        if(activePlan == Constants.MONTHLY_SUBSCRIPTION) {
                            highlightActivePlan("monthly")
                        }
                    } else if (skuDetails.sku == Constants.YEARLY_SUBSCRIPTION) {
                        mAnnualSKU = skuDetails
                        annual_pricing.text = getPriceFromMacro(skuDetails.getPriceCurrencyCode(), skuDetails.getPriceAmountMicros()) + "/year"
                        annual_monthly_pricing.text = getPriceFromMacro(skuDetails.getPriceCurrencyCode(), skuDetails.getPriceAmountMicros() / 12) + " / mo"
                        if(activePlan == Constants.YEARLY_SUBSCRIPTION) {
                            highlightActivePlan("yearly")
                        }
                    }
                    discountPercentage.text = "Save 25%"
                }
        }
    }

    private fun highlightActivePlan(type : String) {
        if(type == "yearly") {
            yearlyPlanBox.background = ContextCompat.getDrawable(mContext, R.drawable.blue_border_white_inside_4r)
            active_plan_yearly.visibility = View.VISIBLE
            discountPercentage.visibility = View.INVISIBLE
        } else {
            monthlyPlanBox.background = ContextCompat.getDrawable(mContext, R.drawable.blue_border_white_inside_4r)
            active_plan_monthly.visibility = View.VISIBLE
        }

    }

    private fun getPriceFromMacro(pCountryCode: String, pMicroPrice: Long): String? {
        return Html.fromHtml("Rs " + (pMicroPrice / 1000000).toString()).toString();
    }

    fun showMessage(message: String?, type: String?, exitType: String?) {
        ViewUtils().displayToast(message, type, this@UpgradePlanActivity, exitType)
    }

    fun showLoader() {
        progress_bar.visibility = View.VISIBLE
    }

    fun acknowledgePurchase(lPurchase: Purchase) {
        if (lPurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!lPurchase.isAcknowledged) {
                mBillingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(lPurchase.purchaseToken).build(), mAckPurchaseListener)
            }
        } else if (lPurchase.purchaseState == Purchase.PurchaseState.PENDING) {
            CoexclLogs.errorLog(TAG, "Subscription not purchased")
        } else {
            CoexclLogs.errorLog(TAG, "Subscription not specified")
        }
    }


    inner class UpgradeToPremium : AsyncTask<List<Purchase>, Void?, String>() {

        private lateinit var lPurchase : Purchase

        override fun onPreExecute() {
            super.onPreExecute()
            showLoader()
        }

        override fun doInBackground(vararg lPurchaseList: List<Purchase>): String {

            try {
                lPurchase = lPurchaseList[0][0]
                val lParamMap = HashMap<String, String>()
                lParamMap["orderId"] = lPurchase.orderId
                lParamMap["packageId"] = mContext.applicationContext.packageName
                lParamMap["productId"] = lPurchase.skus[0]
                lParamMap["userId"] = UserDetails.getInstance().id
                lParamMap["mobile"] = UserDetails.getInstance().mobile
                lParamMap["pushToken"] = lPurchase.purchaseToken

                if (mAlreadyPurchasedProductList.size > 0) {
                    mExistingPlanId = mAlreadyPurchasedProductList[0].skus[0]
                    mNewPlanId = lPurchase.skus[0]
                    lParamMap["productId_old"] = mAlreadyPurchasedProductList[0].skus[0]
                }

                try {
                    lParamMap["device"] = Build.DEVICE
                    lParamMap["versionName"] = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = Helper().constructJson(lParamMap)
                CoexclLogs.infoLog(TAG, "lParamMap: $lParamMap")
                lHttpHelper = PremiumAPI(mContext).upgradeToPremium(lHttpHelper, this@UpgradePlanActivity)
                CoexclLogs.infoLog(TAG, "Upgrade lResponse : " + lHttpHelper.response)

                return lHttpHelper.response

                } catch (e: Exception) {
                    CoexclLogs.printException(e)
                }

            return ""
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            progress_bar.visibility = View.GONE

            val lMapper = ObjectMapper()
            var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                    var data = lResponseObject["data"] as HashMap<String, Any>
                    if (data["status"]?.equals("active") == true) {
                        UserDetails.getInstance().subscribed = true
                    }
                    showMessage("Upgrading Account Success!", Constants.SUCCESS_MESSEGE_TYPE, "")
                    acknowledgePurchase(lPurchase)
            } else {
                showMessage("Upgrading your Account failed!", Constants.FAILURE_MESSEGE_TYPE, "")
            }
        }

    }

    override fun finish() {
//        if(Smartlook.isRecording())
//        Smartlook.stopRecording()
        super.finish()
    }
}
