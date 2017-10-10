package com.example.notifier

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_new.view.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

/**
 * Created by akshat on 4/10/17.
 */

class NewFragment :Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_new,container,false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view!!.send_button.setOnClickListener { v ->

            val message = view!!.notification_text.toString().trim()
            val description = view!!.description_text.toString().trim()
            if( message != "" && description != "") {
                //sendNotification(message,description)
                updateDB(message,description)
                view!!.notification_text.setText("")
                view!!.description_text.setText("")
            } else {
                Toast.makeText(activity,"Either title or description is empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendNotification(message: String, description: String) {
        doAsync {
            try {
                val GCM_API_KEY = R.string.GCM_KEY
                val JSON: MediaType = MediaType.parse("application/json; charset=utf-8");

                val client = OkHttpClient()

                val bodyString = "{\n" +
                        "  \"to\": \"/topics/all\",\n" +
                        "   \"priority\": \"high\"" +
                        "  \"notification\": {\n" +
                        "    \"title\": " + message + ","  +
                        "    \"message\": " + description + "," +
                        "   }\n" +
                        "}"

                val body = RequestBody.create(JSON,bodyString)

                val request = Request.Builder()
                        .url("https://fcm.googleapis.com/fcm/send")
                        .header("Content-Type","application/json")
                        .header("Authorization", "key=" + GCM_API_KEY)
                        .post(body)
                        .build()
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    uiThread {
                        Toast.makeText(activity,"Unsuccessful " + response.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: Exception) {
                uiThread {
                    Toast.makeText(activity,"Exception",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateDB(message: String,description: String){
        var act = (activity) as MainActivity
        doAsync {
            val JSON: MediaType = MediaType.parse("application/json; charset=utf-8");

            val client = OkHttpClient()

            val bodyString = "{\"description\":" + description + ",\"senderName\":"+ act.userName +",\"timestamp\":1265498468,\"title\":" +message + "}"

            val body = RequestBody.create(JSON,bodyString)
            try {
                val request = Request.Builder()
                        .url("https://effervescence-17.firebaseio.com/notifications.json")
                        .post(body)
                        .build()
                client.newCall(request).execute()
            } catch (e : Exception) {
                uiThread {
                    Log.d("akshat",e.toString())
                }
            }
        }
    }
}