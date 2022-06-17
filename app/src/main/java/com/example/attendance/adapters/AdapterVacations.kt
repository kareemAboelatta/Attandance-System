package com.example.attendance.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.models.VacationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_vacation.view.*
import java.util.*
import javax.inject.Inject

class AdapterVacations  @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager,
) :  RecyclerView.Adapter<AdapterVacations.VacationViewHolder>(){

    inner class VacationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VacationRequest>() {

            override fun areItemsTheSame(oldItem: VacationRequest, newItem: VacationRequest) =
                oldItem.vacationId == newItem.vacationId

            override fun areContentsTheSame(oldItem: VacationRequest, newItem: VacationRequest) =
                oldItem.vacationId == newItem.vacationId

        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    private var onItemClickListenerForAccept: ((VacationRequest) -> Unit)? = null
    fun setOnItemClickListenerForAccept(listener: (VacationRequest) -> Unit) {
        onItemClickListenerForAccept = listener
    }

    private var onItemClickListenerForRefused:  ((VacationRequest) -> Unit)? = null
    fun setOnItemClickListenerForRefused(listener: (VacationRequest) -> Unit) {
        onItemClickListenerForRefused= listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):   VacationViewHolder {
        return   VacationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_vacation
                , parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder:   VacationViewHolder, position: Int) {
        val   vacationRequest = differ.currentList[position]
        holder.itemView.apply {

            //user data
            post_userName.text=vacationRequest.userName
            glide.load(vacationRequest.userImage).into(post_userPicture)
            //post data
            //get time from timestamp
            val cal = Calendar.getInstance(Locale.getDefault())
            cal.timeInMillis = vacationRequest.requestTime.toLong()

            val time = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()
            post_TimeIv.text=time
            post_caption.text=vacationRequest.caption
            post_days.text="I need just ${vacationRequest.days} days"
            post_text_anyone.text = vacationRequest.userRoll



            vacation_accept.setOnClickListener {
                onItemClickListenerForAccept?.let { it(vacationRequest) }
            }

            vacation_cancle.setOnClickListener {
                onItemClickListenerForRefused?.let { it(vacationRequest) }
            }



        }

    }








}