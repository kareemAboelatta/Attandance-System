package com.example.attendance.ui.fragment.employee

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.ViewModelSignUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private  val viewModel by  viewModels<ViewModelSignUser>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        home_report_container.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sendReportFragment)
        }

        home_vacation_container.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_askForVacationFragment)
        }

        home_attendance_container.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_attendanceFragment)
        }

        home_salary_container.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_checkSalaryFragment)
        }




    }
}