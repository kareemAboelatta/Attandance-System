package com.example.attendance.adapters

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.models.Report
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_report.view.*
import java.util.*
import javax.inject.Inject

class AdapterReport  @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager,
) :  RecyclerView.Adapter<AdapterReport.ReportViewHolder>(){

    inner class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    var reports: List<Report> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):   ReportViewHolder {
        return   ReportViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_report, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reports.size
    }


    override fun onBindViewHolder(holder:   ReportViewHolder, position: Int) {
        val   report = reports[position]
        holder.itemView.apply {

            //user data
            post_userName.text=report.userName
            glide.load(report.userImage).into(post_userPicture)
            //post data
            //get time from timestamp
            val cal = Calendar.getInstance(Locale.getDefault())
            cal.timeInMillis = report.reportTime.toLong()

            val time = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()
            post_TimeIv.text=time
            post_caption.text=report.caption

            post_text_anyone.text=report.userRoll







/*

            post_userPicture.setOnClickListener {
                onItemClickListenerForGoingtoOwner?.let { it(post) }
            }
            post_userName.setOnClickListener {
                onItemClickListenerForGoingtoOwner?.let { it(post) }
            }

            post_comment_btn.setOnClickListener {
                onItemClickListener?.let { it(post) }
            }
*/



        }

    }








}