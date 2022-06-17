package com.example.attendance.ui.fragment.employee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterNotifications
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNotifications : Fragment(R.layout.fragment_notification) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var auth: FirebaseAuth
    lateinit var arrayList: List<String>


    @Inject
    lateinit var adapter: AdapterNotifications

    private  val viewModel by  viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arrayList = ArrayList()
        adapter.differ.submitList(arrayList)

        val linearLayout = LinearLayoutManager(activity)
        rec_notifications .layoutManager=linearLayout



        viewModel.getNotifications(auth.currentUser?.uid!!)

        viewModel.getNotificationsLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {

                    adapter.differ.submitList(it.data)
                    rec_notifications.adapter = adapter
                    adapter.notifyDataSetChanged()


                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }





    }






}