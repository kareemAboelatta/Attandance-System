package com.example.attendance.ui.fragment.admin

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterComment
import com.example.attendance.models.Comment
import com.example.attendance.models.Report
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.fragment_report_details_admin.*
import kotlinx.android.synthetic.main.fragment_report_details_admin.btn_back
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_ProgressBar_comments
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_pTimeIv
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_pTitleIv
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_post_read_btn
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_rec_comments
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_uNameIv
import kotlinx.android.synthetic.main.fragment_report_details_admin.det_userPictureIv
import kotlinx.android.synthetic.main.fragment_report_details_admin.report_text_roll
import kotlinx.android.synthetic.main.fragment_report_details_employee.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ReportDetailsFragmentForAdmin :  Fragment(R.layout.fragment_report_details_admin) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var mycontext: Context

    @Inject
    lateinit var commentAdapter: AdapterComment

    val args : ReportDetailsFragmentForAdminArgs by navArgs()

    var reportId:String ="args.report.postId"

    var t1: TextToSpeech? = null

    private val viewModel by viewModels<ViewModelAdmin>()
    private val mainViewModel by viewModels<MainViewModel>()

    lateinit var report: Report

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.bottom_menu?.visibility = View.GONE

        report=args.report

        reportId=args.report.reportId

        t1 = TextToSpeech(activity) { status ->
            if (status != TextToSpeech.ERROR) {
                t1!!.language = Locale.ENGLISH
            }
        }

        //user data in report
        det_uNameIv.text=report.userName
        report_text_roll.text=report.userRoll
        glide.load(report.userImage).into(det_userPictureIv)


        //post data
        //get time from timestamp
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.timeInMillis = report.reportTime.toLong()

        val time = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()
        det_pTimeIv.text=time
        det_pTitleIv.text=report.caption

        det_post_read_btn.setOnClickListener {
            t1?.speak(report.caption, TextToSpeech.QUEUE_FLUSH, null)
        }

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        det_btn_comment.setOnClickListener {
            postComment()
        }

        loadComments()



    }


    private fun postComment() {

        //get data from comment edit text
        val comment= det_commentEt.text.toString()
        //validate
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(activity, "Comment is Empty...", Toast.LENGTH_SHORT).show()
            return
        }
        val timeStamp = System.currentTimeMillis().toString()
        //each post will have a child "Comments " tha will conten comments of the post

        val myComment=Comment(timeStamp, comment, timeStamp)

        //put this data in DB :
        viewModel.postComment(report,myComment)
        det_commentEt.setText("")
    }



    private fun loadComments() {
        //layout (linear) for recycleview

        val layoutManager = LinearLayoutManager(activity)
        det_rec_comments.layoutManager=layoutManager

        mainViewModel.loadComments(report.reportId)
        mainViewModel.commentsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    det_ProgressBar_comments?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    det_ProgressBar_comments.visibility = View.GONE
                    commentAdapter.differ.submitList(it.data)
                    det_rec_comments.adapter = commentAdapter
                }
                Status.ERROR -> {
                    det_ProgressBar_comments?.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.bottom_menu?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        activity?.bottom_menu?.visibility = View.VISIBLE
        t1?.stop()

    }

    override fun onStop() {
        super.onStop()
        activity?.bottom_menu?.visibility = View.VISIBLE
        t1?.stop()


    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottom_menu?.visibility = View.VISIBLE
        t1?.stop()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.bottom_menu?.visibility = View.VISIBLE

        t1?.stop()

    }



}