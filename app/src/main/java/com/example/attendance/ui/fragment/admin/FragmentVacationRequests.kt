package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterPeople
import com.example.attendance.adapters.AdapterVacations
import com.example.attendance.models.User
import com.example.attendance.models.VacationRequest
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.fragment_vacation_requests.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentVacationRequests : Fragment(R.layout.fragment_vacation_requests) {


    @Inject
    lateinit var adapterVacations: AdapterVacations

    lateinit var arrayList: List<VacationRequest>



    private  val viewModel by  viewModels<ViewModelAdmin>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()
        adapterVacations.differ.submitList(arrayList)


        val linearLayout = LinearLayoutManager(activity)
        rec_vacations.layoutManager=linearLayout


        viewModel.getRequestVacations()
        viewModel.requestVacationLiveData.observe(viewLifecycleOwner) {
            when(it.status){
                Status.SUCCESS->{
                    arrayList= it.data as ArrayList<VacationRequest>
                    adapterVacations.differ.submitList(arrayList)
                    rec_vacations.adapter = adapterVacations
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

        adapterVacations.setOnItemClickListenerForAccept { vacationRequest ->
            //accept request
            viewModel.acceptRequest(vacationRequest)
            viewModel.acceptVacationRequestLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val list = ArrayList<VacationRequest>(arrayList)
                        list.remove(vacationRequest)
                        arrayList = list
                        adapterVacations.differ.submitList(list)

                        Toast.makeText(activity, "Request Accepted", Toast.LENGTH_LONG).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }



        }

        adapterVacations.setOnItemClickListenerForRefused { vacationRequest ->
            val list = ArrayList<VacationRequest>(arrayList)
            list.remove(vacationRequest)
            arrayList = list
            adapterVacations.differ.submitList(list)
            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
        }

/*
        adapterVacations.setOnItemClickListener {user->
            //accept request
            viewModel.acceptRequest(user.id)
            val list = ArrayList<User>(arrayList)
            list.remove(user)
            arrayList = list
            adapterVacations.differ.submitList(list)
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

        adapterVacations.setOnItemClickListener2 {user->
            val list = ArrayList<VacationRequest>(arrayList)
            list.remove(user)
            arrayList = list
            adapterVacations.differ.submitList(list)
            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()

        }
*/


    }




}