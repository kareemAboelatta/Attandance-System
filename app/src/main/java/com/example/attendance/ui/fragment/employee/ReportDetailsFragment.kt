package com.example.attendance.ui.fragment.employee

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.Gravity
import com.example.attendance.R
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.adapters.AdapterComment
import com.example.attendance.models.Report
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_report_details_employee.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ReportDetailsFragment :  Fragment(R.layout.fragment_report_details_employee) {

    @Inject
    lateinit var glide:RequestManager

/*
    @Inject
    lateinit var refDatabase: DatabaseReference

    @Inject
    lateinit var refStorage: StorageReference

    @Inject
    lateinit var auth: FirebaseAuth
*/

    @Inject
    lateinit var mycontext: Context

    @Inject
    lateinit var commentAdapter: AdapterComment

    val args : ReportDetailsFragmentArgs by navArgs()

    var reportId:String ="args.report.postId"

    var t1: TextToSpeech? = null

    private val viewModel by viewModels<MainViewModel>()

    lateinit var report: Report

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.bottomNavigationView?.visibility = View.GONE

        var notification=args.notification

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getReportByID(notification.reportId)
        viewModel.getReportByIDLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.LOADING -> {
                    reportDetailsProgressBar.visibility=View.VISIBLE
                }
                Status.SUCCESS -> {
                    reportDetailsProgressBar.visibility=View.GONE

                    report= it.data!!

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
                    loadComments()

                }
                Status.ERROR -> {
                    reportDetailsProgressBar.visibility=View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }







    }


    private fun loadComments() {
        //layout (linear) for recycleview

        val layoutManager = LinearLayoutManager(activity)
        det_rec_comments.layoutManager=layoutManager

        viewModel.loadComments(report.reportId)
        viewModel.commentsLiveData.observe(viewLifecycleOwner) {
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
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.next_event_text?.visibility = View.GONE
        activity?.fab?.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

        t1?.stop()
    }

    override fun onStop() {
        super.onStop()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

        t1?.stop()

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE
        t1?.stop()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE


        t1?.stop()


    }



}