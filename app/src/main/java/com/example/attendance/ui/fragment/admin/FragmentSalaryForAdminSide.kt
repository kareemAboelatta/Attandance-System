package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterAttend
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.fragment_salary_for_admin_side.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentSalaryForAdminSide : Fragment(R.layout.fragment_salary_for_admin_side) {


    @Inject
    lateinit var adapter: AdapterAttend
    lateinit var arrayList: List<String>


    val args :FragmentSalaryForAdminSideArgs by navArgs()

    var userId:String ="args.user.id"
    var name:String ="args.user.name"



    var salaryPerDay: Int = 0
    private  val viewModel by  viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId=args.user.id
        name=args.user.name
        salary_name.text=name

        btn_back_salary.setOnClickListener {
            findNavController().popBackStack()
        }

        btn_back_salary.setOnClickListener {
            requireActivity().onBackPressed()
        }


        val user = args.user
        salary_month.text = user.salary.toString()
        salaryPerDay = (user.salary / 30).toInt()
        salary_day.text = (user.salary / 30).toString()



        viewModel.getAttendance(userId)
        viewModel.getAttendanceLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null && it.data.isNotEmpty()) {
                        days_you_attended_work.text = it.data.size.toString()
                        days_you_absented_work.text = getAbsentDays(it.data).toString()
                        current_salary.text = (salaryPerDay*it.data.size).toString()+"$"
                        deducted_from_you.text = (salaryPerDay*getAbsentDays(it.data)).toString()+"$"
                    }else{
                        home_salary_container.visibility=View.GONE
                        this_employee.visibility=View.VISIBLE
                        salary_pic.setImageResource(R.drawable.bad)
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
        activity?.bottom_menu?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        activity?.bottom_menu?.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        activity?.bottom_menu?.visibility = View.VISIBLE


    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottom_menu?.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.bottom_menu?.visibility = View.VISIBLE

    }


}