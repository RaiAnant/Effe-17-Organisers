package com.example.notifier

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new.view.*
import kotlinx.android.synthetic.main.notification_container.view.*

/**
 * Created by akshat on 1/10/17.
 */

class NotificationsAdapter(private val notificationList: List<Notification>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.notifications.text = notificationList[position].title
        holder.itemView.description.text = notificationList[position].description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_container,parent,false))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}