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
import com.example.attendance.models.Day
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_attend.view.*
import kotlinx.android.synthetic.main.item_day.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterDays @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterDays.AttendViewHolder>(){

    inner class AttendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)



    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Day>() {

            override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean =
                oldItem.day == newItem.day

            override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean =
                oldItem.day == newItem.day

        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendViewHolder {
        return AttendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_day,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AttendViewHolder, position: Int) {
        val   currentDate = differ.currentList[position]

        holder.itemView.apply {
            day_text.text=currentDate.day

            when(currentDate.backgroundColor){
                "red"->{
                    day_background.background = resources.getDrawable(R.color.red)
                }
                "green"->{
                    day_background.background = resources.getDrawable(R.color.colorGreen)
                }
            }

            val sdf = SimpleDateFormat("dd-MMM-yyyy")
            val current = sdf.format(Date())
            var dayInMonth:Int =current.substring(0,2).toInt()
            if (currentDate.day == dayInMonth.toString()){
            day_background.background = resources.getDrawable(R.color.yellow)
            }


        }

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}