package com.example.attendance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.models.Notification
import com.example.attendance.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_attend.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import javax.inject.Inject


class AdapterNotifications @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterNotifications.AttendViewHolder>(){

    inner class AttendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)



    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>() {

            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem.notification == newItem.notification

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem.notification == newItem.notification

        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendViewHolder {
        return AttendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_notification,
                parent,
                false
            )
        )
    }


    private var onItemClickListener: ((Notification) -> Unit)? = null
    fun setOnItemClickListener(listener: (Notification) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: AttendViewHolder, position: Int) {
        val   currentNotification = differ.currentList[position]

        holder.itemView.apply {
            notification_text.text=currentNotification.notification


            if (currentNotification.type == "report"){
                setOnClickListener {
                    onItemClickListener?.let {
                        it(currentNotification)
                    }
                }
            }

        }

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}