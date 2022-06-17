package com.example.attendance.ui.fragment.admin

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterEmployee
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_all_emp.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentAllEmps : Fragment(R.layout.fragment_all_emp) {

    @Inject
    lateinit var employeeAdapter: AdapterEmployee

    lateinit var arrayList: List<User>


    private  val viewModel by  viewModels<ViewModelAdmin>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()
        employeeAdapter.differ.submitList(arrayList)


        val linearLayout = LinearLayoutManager(activity)
        rec_employees.layoutManager=linearLayout

        viewModel.employee.observe(viewLifecycleOwner) { employees ->
            arrayList= employees as ArrayList<User>
            employeeAdapter.differ.submitList(arrayList)
            rec_employees.adapter = employeeAdapter
        }
        
        employeeAdapter.setOnItemClickListener {user->
            val bundle = Bundle().apply {
                putParcelable("user", user)
            }

            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialg)
            val emp_attendance = dialog.findViewById(R.id.emp_attendance) as Button
            val emp_salary = dialog.findViewById(R.id.emp_salary) as Button
            val emp_profile = dialog.findViewById(R.id.emp_profile) as Button
            val emp_update = dialog.findViewById(R.id.emp_update) as Button
            val emp_close_dioalg = dialog.findViewById(R.id.emp_close_dioalg) as TextView

            emp_attendance.setOnClickListener {
                findNavController().navigate(
                    R.id.action_fragmentAllEmps_to_fragmentAttendanceForAdminSide,
                    bundle
                )
                dialog.dismiss()
            }
            emp_salary.setOnClickListener {
                findNavController().navigate(
                    R.id.action_fragmentAllEmps_to_fragmentSalaryForAdminSide,
                    bundle
                )
                dialog.dismiss()
            }
            emp_profile.setOnClickListener {
                findNavController().navigate(
                    R.id.action_fragmentAllEmps_to_fragmentProfileForAdminSide,
                    bundle
                )
                dialog.dismiss()
            }

            emp_update.setOnClickListener {
                findNavController().navigate(
                    R.id.action_fragmentAllEmps_to_fragmentUpdateForAdminSide,
                    bundle
                )
                dialog.dismiss()
            }


            emp_close_dioalg.setOnClickListener { dialog.dismiss() }
            dialog.show()



        }





    }




}