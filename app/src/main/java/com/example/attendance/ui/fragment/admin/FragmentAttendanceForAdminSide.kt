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
import com.example.attendance.adapters.AdapterEmployee
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.fragment_attendance_for_admin_side.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentAttendanceForAdminSide : Fragment(R.layout.fragment_attendance_for_admin_side) {


    @Inject
    lateinit var adapter: AdapterAttend
    lateinit var arrayList: List<String>


    val args :FragmentAttendanceForAdminSideArgs by navArgs()

    var userId:String ="args.user.id"
    var name:String ="args.user.name"

    private  val viewModel by  viewModels<MainViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId=args.user.id
        name=args.user.name
        attendance_emp_name.text=name

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        arrayList = ArrayList()
        adapter.differ.submitList(arrayList)


        val linearLayout = LinearLayoutManager(activity)
        rec_attendance.layoutManager=linearLayout


        viewModel.getAttendance(userId)
        viewModel.getAttendanceLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        txt_you_attend.text = "${name} attend ${it.data.size.toString()} days"
                        adapter.differ.submitList(it.data)
                        rec_attendance.adapter = adapter
                        adapter.notifyDataSetChanged()
                        txt_you_absent.text = "${name} absent just ${getAbsentDays(it.data)} days"
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