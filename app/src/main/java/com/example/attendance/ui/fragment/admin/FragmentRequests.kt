package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterPeople
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_requests.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRequests : Fragment(R.layout.fragment_requests) {


    @Inject
    lateinit var peopleAdapter: AdapterPeople

    lateinit var arrayList: List<User>



    private  val viewModel by  viewModels<ViewModelAdmin>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()
        peopleAdapter.differ.submitList(arrayList)


        val linearLayout = LinearLayoutManager(activity)
        rec_requests.layoutManager=linearLayout

        viewModel.peopleAll.observe(viewLifecycleOwner) { people ->
            arrayList= people as ArrayList<User>
            peopleAdapter.differ.submitList(arrayList)
            rec_requests.adapter = peopleAdapter
        }

        peopleAdapter.setOnItemClickListener {user->
            //accept request
            viewModel.acceptRequest(user.id)
            val list = ArrayList<User>(arrayList)
            list.remove(user)
            arrayList = list
            peopleAdapter.differ.submitList(list)
            viewModel.acceptUserLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(activity, "Request Accepted", Toast.LENGTH_LONG).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        peopleAdapter.setOnItemClickListener2 {user->
            val list = ArrayList<User>(arrayList)
            list.remove(user)
            arrayList = list
            peopleAdapter.differ.submitList(list)
            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()

        }


    }




}