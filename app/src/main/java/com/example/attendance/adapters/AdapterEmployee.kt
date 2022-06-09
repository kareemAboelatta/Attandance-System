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
import com.example.attendance.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_employee.view.*
import javax.inject.Inject

class AdapterEmployee @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterEmployee.EmployeeViewHolder>(){

    inner class EmployeeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)



    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)



    private var onItemClickListener: ((User) -> Unit)? = null
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_employee,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val   currentUser = differ.currentList[position]

        holder.itemView.apply {
            glide.load(currentUser.image).error(R.drawable.ic_profile).into(user_image)
            user_name.text=currentUser.name
            user_bio.text=currentUser.roll
            user_branch.text=currentUser.branch
            setOnClickListener {
                onItemClickListener?.let {
                    it(currentUser)
                }
            }

        }

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }



}