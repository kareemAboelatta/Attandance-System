package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterReport
import com.example.attendance.models.Report
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_reports.*
import javax.inject.Inject


@AndroidEntryPoint
class FragmentReports : Fragment(R.layout.fragment_reports) {


    @Inject
    lateinit var reportAdapter: AdapterReport
    private var reportList : ArrayList<Report> = ArrayList()

    private  val viewModel by  viewModels<ViewModelAdmin>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewSetUp()
        viewModel.getReports()

        viewModel.reportsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    reportsProgressBar?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    reportsProgressBar.visibility = View.GONE
                    reportList = it.data as ArrayList<Report>
                    reportAdapter.reports = reportList
                }
                Status.ERROR -> {
                    reportsProgressBar?.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        reportAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("report", it)
            }
            findNavController().navigate(
                R.id.action_fragmentReports_to_reportDetailsFragmentForAdmin,
                bundle
            )
        }


    }


    private fun recyclerViewSetUp(){
        val linearLayout = LinearLayoutManager(activity)
        linearLayout.stackFromEnd = true
        linearLayout.reverseLayout = true
        reports_recycler_view.layoutManager=linearLayout
        reportAdapter.reports=reportList
        reports_recycler_view.adapter=reportAdapter

    }

}