package com.example.attendance.ui.fragment.employee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_check_salary.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CheckSalaryFragment : Fragment(R.layout.fragment_check_salary) {

    @Inject
    lateinit var auth:FirebaseAuth

    private val viewModel by viewModels<MainViewModel>()

    var salaryPerDay: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_back_salary.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.getDataForCurrentUser()
        //data for this user
        viewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    salary_ProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val user = it.data!!
                    salary_month.text = user.salary.toString()
                    salaryPerDay=(user.salary/30).toInt()
                    salary_day.text = (user.salary/30).toString()


                    salary_ProgressBar.visibility = View.GONE


                }
                Status.ERROR -> {
                    salary_ProgressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.getAttendance(auth.currentUser?.uid!!)
        viewModel.getAttendanceLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        days_you_attended_work.text = it.data.size.toString()
                        days_you_absented_work.text = getAbsentDays(it.data).toString()
                        current_salary.text = (salaryPerDay*it.data.size).toString()+"$"
                        deducted_from_you.text = (salaryPerDay*getAbsentDays(it.data)).toString()+"$"
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }




    //get absent days by current date
    fun getAbsentDays(list: List<String>): Int {
        var absentDays = 0
        //get current time
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate = sdf.format(Date())
        var day:Int =currentDate.substring(0,2).toInt()
        absentDays=day - list.size
        return absentDays
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

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

    }










}