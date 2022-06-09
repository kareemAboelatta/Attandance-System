package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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



    }




}