package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_event.*

@AndroidEntryPoint
class FragmentAddEvent : Fragment(R.layout.fragment_add_event) {

    private  val viewModel by  viewModels<ViewModelAdmin>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_priority.maxValue=3
        event_priority.minValue=1

        event_publish.setOnClickListener {
            if(event_caption.text.toString().isNotEmpty() ){
                viewModel.addEvent(event_caption.text.toString(), event_priority.value.toInt())
                observeViewModel()
            }else{
                Toast.makeText(requireActivity(),"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }
        }


    }



    private fun observeViewModel(){
        viewModel.uploadEventLiveData.observe(viewLifecycleOwner) {
            when(it.status){
                Status.LOADING -> {
                    event_progress.visibility = View.VISIBLE
                    event_publish.isEnabled = false
                    event_publish.text = "Publishing..."
                }
                Status.SUCCESS->{
                    event_progress.visibility = View.GONE
                    event_caption.text.clear()
                    event_priority.value = 1
                    event_publish.isEnabled = true
                    event_publish.text = "Publish"
                    Toast.makeText(requireActivity(), "Announcement published", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR->{
                    event_progress.visibility = View.GONE
                    event_publish.text = "Publish"
                    event_publish.isEnabled = true
                    Toast.makeText(requireActivity(), ""+it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}