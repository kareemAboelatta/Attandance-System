package com.example.attendance.ui.fragment.employee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sendreport_fragment.*
import javax.inject.Inject


@AndroidEntryPoint
class SendReportFragment : Fragment(R.layout.sendreport_fragment) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var auth: FirebaseAuth


    lateinit var thisUser : User

    private  val viewModel by  viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDataForCurrentUser()
        viewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    thisUser = it.data!!
                    glide.load(thisUser.image).into(publish_my_image)
                    publish_my_name.text = thisUser.name
                    publish_job_title.text = thisUser.roll
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }



        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        publish_btn_publish.setOnClickListener {
            var caption = publish_caption.text.toString()
            if (caption.isEmpty()) {
                Toast.makeText(requireActivity(), "Enter caption", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }


            var report = Report(
                thisUser.id, thisUser.name, thisUser.email, thisUser.image, caption,
                thisUser.roll,"", ""
            )


            viewModel.uploadReport(report)
            viewModel.reportLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Report Sent", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()

                    }
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
    }

    override fun onStop() {
        super.onStop()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE
    }
}