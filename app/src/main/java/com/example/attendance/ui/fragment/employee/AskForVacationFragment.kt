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
import com.example.attendance.models.VacationRequest
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ask_vacation_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class AskForVacationFragment : Fragment(R.layout.ask_vacation_fragment) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var auth: FirebaseAuth

    lateinit var thisUser : User

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getDataForCurrentUser()
        viewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    thisUser = it.data!!
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }




        vacation_submit.setOnClickListener {
            var caption = vacation_reason.text.toString()
            var days = vacation_days.text.toString()
            if (caption.isEmpty() && days.isEmpty()) {
                Toast.makeText(requireActivity(), "Enter reason for vacation", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }
            var vacationReqest = VacationRequest(
                thisUser.id, thisUser.name, thisUser.email, thisUser.image, caption,
                days.toInt(),thisUser.roll,"", ""
            )

            viewModel.uploadVacation(vacationReqest)
            viewModel.requestVacationLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        vacation_askingProgressBar.visibility = View.VISIBLE
                        vacation_submit.isEnabled = false
                        vacation_submit.text = "Loading..."
                    }
                    Status.SUCCESS -> {
                        vacation_askingProgressBar.visibility = View.GONE
                        vacation_submit.isEnabled = true
                        Toast.makeText(requireContext(), "Vacation request was sent", Toast.LENGTH_LONG)
                            .show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        vacation_askingProgressBar.visibility = View.GONE
                        vacation_submit.isEnabled = true
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