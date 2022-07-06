package com.home.coexcleducation.premium

import android.content.Context
import android.os.AsyncTask
import com.android.billingclient.api.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.Helper

/**
 * Created by Abhishek on 2022-07-11.
 */
class InAppBillingUtil(val context: Context) : PurchasesUpdatedListener {

    private val TAG = javaClass.canonicalName
    private var billingClient : BillingClient
    private val mPurchaseItemList : ArrayList<Purchase> = ArrayList()
    private var mAckPurchaseListener: AcknowledgePurchaseResponseListener
    private var planStatus = "inactive"

    init {
            billingClient =
                BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        CoexclLogs.errorLog(
                            TAG,
                            "Google Play Service Setup successful. Querying inventory."
                        )
                        if(UserDetails.getInstance().schoolCode == "COX2100001") {
                            queryPurchase()
                        }
                    } else {
                        CoexclLogs.errorLog(TAG, "Error on fetching products")
                    }
                }

                override fun onBillingServiceDisconnected() {
                    CoexclLogs.errorLog(TAG, "Google Play Service Disconnected.")
                }
            })

            mAckPurchaseListener =
                AcknowledgePurchaseResponseListener { billingResult: BillingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        CoexclLogs.errorLog(TAG, "Purchase query")
                    }
                    CoexclLogs.errorLog(
                        TAG,
                        "Acknowledge billingResult ---- " + billingResult.responseCode
                    )
                }

    }


    fun queryPurchase() {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { _, purchases ->
            CoexclLogs.errorLog(TAG, "purchasesResult ---- $purchases")
                if (purchases.isNullOrEmpty()) {
                    planStatus = "inactive"
                } else {
                    mPurchaseItemList.addAll(purchases)
                    planStatus = "active"
                }
            UpdatePlanStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        }

    }

    fun getPurchasedItemList() : List<Purchase>{
        return mPurchaseItemList
    }


    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {}

    inner class UpdatePlanStatus : AsyncTask<List<Purchase>, Void?, String>() {


        override fun doInBackground(vararg lPurchaseList: List<Purchase>): String {

            try {
                val lParamMap = HashMap<String, String>()
                lParamMap["mobile"] = UserDetails.getInstance().mobile
                lParamMap["status"] = planStatus

                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = Helper().constructJson(lParamMap)
                CoexclLogs.errorLog(TAG, "lParamMap: $lParamMap")
                lHttpHelper = PremiumAPI(context).updatePlanStatus(lHttpHelper, context)
                CoexclLogs.errorLog(TAG, "Status lResponse : " + lHttpHelper.response)

                return lHttpHelper.response

            } catch (e: Exception) {
                CoexclLogs.printException(e)
            }

            return ""
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            try {
                val lMapper = ObjectMapper()
                var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                    if (lResponseObject.containsKey("data")) {
                        var data = lResponseObject["data"] as HashMap<String, Any>
                        UserDetails.getInstance().subscribed =
                            data["status"]?.equals("active") == true
                    } else {
                        UserDetails.getInstance().subscribed = false
                    }
                }
            } catch (e :Exception) {
                e.printStackTrace()
            }
        }

    }
}