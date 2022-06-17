package com.example.attendance.ui.fragment.employee

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterAttend
import com.example.attendance.adapters.AdapterEmployee
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Constants
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_attendance.*
import java.text.SimpleDateFormat

import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendanceFragment : Fragment(R.layout.fragment_attendance) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var ref:DatabaseReference

    @Inject
    lateinit var adapter: AdapterAttend
    lateinit var arrayList: List<String>

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        arrayList = ArrayList()
        adapter.differ.submitList(arrayList)


        val linearLayout = LinearLayoutManager(activity)
        rec_attendance.layoutManager=linearLayout


        viewModel.getAttendance(auth.currentUser?.uid!!)
        viewModel.getAttendanceLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        txt_you_attend.text = "You attend ${it.data.size.toString()} days"
                        adapter.differ.submitList(it.data)
                        rec_attendance.adapter = adapter
                        adapter.notifyDataSetChanged()
                        txt_you_absent.text = "You absent just ${getAbsentDays(it.data)} days"
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

/*         ref.child(Constants.USERS).child(auth.currentUser?.uid!!)
            .child(Constants.ATTENDANCE).get()
            .addOnSuccessListener {
            if (it.exists()) {
                var attendances = it.value as HashMap<String, Any>
                var list = ArrayList<String>()
                attendances.forEach {
                    list.add(it.key)
                }
                //get current time
                val sdf = SimpleDateFormat("dd-MMM-yyyy")
                val currentDate = sdf.format(Date())
                txt_you_absent.text=currentDate

            }

        }*/



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