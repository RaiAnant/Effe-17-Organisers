package com.example.notifier

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_old.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

/**
 * Created by akshat on 4/10/17.
 */
class Old_fragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_old,container,false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchLatestData(view!!)
    }

    private fun fetchLatestData(view: View){
        doAsync {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("https://effervescence-17.firebaseio.com/notifications.json")
                    .build()
            val response = client.newCall(request).execute()
            try {
                if (response.isSuccessful) {
                    val updatesList: ArrayList<Notification> = ArrayList()
                    val body = JSONObject(response.body()?.string())
                    val keys = body.keys()

                    while (keys.hasNext()) {
                        val key = keys.next().toString()
                        val childObj = body.getJSONObject(key)
                        Log.d("akshat", childObj.toString())
                        if (childObj != null) {
                            val newNotification = Notification()
                            newNotification.description = childObj.getString("description")
                            newNotification.senderName = childObj.getString("senderName")
                            newNotification.timestamp = childObj.getLong("timestamp")
                            newNotification.title = childObj.getString("title")
                            updatesList.add(newNotification)
                        }
                    }
                    uiThread {
                        progressBar?.visibility = View.GONE
                        sentNotifications?.visibility = View.VISIBLE
                        var notificationAdapter = NotificationsAdapter(updatesList)
                        sentNotifications?.adapter = notificationAdapter
                        sentNotifications?.layoutManager = LinearLayoutManager(activity,LinearLayout.VERTICAL,false)
                    }
                }
            }catch (e: Exception) {
                uiThread {
                    progressBar?.visibility = View.VISIBLE
                    sentNotifications?.visibility = View.GONE
                }
            }


        }
    }
}