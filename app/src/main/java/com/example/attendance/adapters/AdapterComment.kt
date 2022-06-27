package com.example.attendance.adapters

import android.app.AlertDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.models.Comment
import com.example.attendance.models.Report
import com.example.attendance.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*
import javax.inject.Inject

class AdapterComment @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterComment.CommentViewHolder>() {



    inner class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
     return   CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val   curComment = differ.currentList[position]

        val cal = Calendar.getInstance(Locale.getDefault())
        if (curComment.timeStamp != null) {
            cal.timeInMillis = curComment.timeStamp!!.toLong()
        }
        val commentTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()

        holder.itemView.apply {
            item_comment.text=curComment.comment
            item_comment_timeTv.text=commentTime

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}