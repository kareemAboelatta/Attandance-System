package com.example.attendance.ui.fragment.employee

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterAttend
import com.example.attendance.adapters.AdapterDays
import com.example.attendance.models.Day
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragmetn_my_calender.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentCalender : Fragment(R.layout.fragmetn_my_calender) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var auth: FirebaseAuth


    @Inject
    lateinit var adapter: AdapterDays
    lateinit var arrayList: List<Day>

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        arrayList = ArrayList()
        adapter.differ.submitList(arrayList)


        val linearLayout = GridLayoutManager(activity , 5)
        rec_attendance.layoutManager=linearLayout






        viewModel.getAttendance(auth.currentUser?.uid!!)
        viewModel.getAttendanceLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        txt_you_attend.text = "You attend ${it.data.size.toString()} days"
                        Log.e("error", "onViewCreated:${it.data}" )
                        it.data.forEach {day->
                            Log.e("error", "${day}" )
                        }
                        arrayList=getAllDaysForThisMonth(getAllDayes(it.data))
                        adapter.differ.submitList(arrayList)
                        rec_attendance.adapter=adapter
                        txt_you_absent.text = "You absent just ${getAbsentDays(it.data)} days"
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }



    }



    fun getAllDaysForThisMonth(allDays:ArrayList<Int>): ArrayList<Day>{
        var allDaysInMonth = ArrayList<Day>()

        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate = sdf.format(Date())
        var dayInMonth:Int =currentDate.substring(0,2).toInt()
        for(thisDay in 1..dayInMonth){

            if(allDays.contains(thisDay)){
                allDaysInMonth.add(Day("$thisDay" , "green"))
            }else{
                allDaysInMonth.add(Day("$thisDay" , "red"))
            }
        }

        return allDaysInMonth
    }

    fun getAllDayes(list: List<String>):ArrayList<Int>{
        var newList = ArrayList<Int>()
        list.forEach{day->
            var thisDay = day.substring(0,2).toInt()
            newList.add(thisDay)
        }
        return newList
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