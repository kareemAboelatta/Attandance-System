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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

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

    override fun onBindViewHolder(holder: AttendViewHolder, position: Int) {
        val   currentDate = differ.currentList[position]

        holder.itemView.apply {
            notification_text.text=currentDate

        }

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}