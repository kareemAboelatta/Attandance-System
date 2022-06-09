package com.example.attendance.ui.fragment.employee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfile : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var glide:RequestManager

    @Inject
    lateinit var auth: FirebaseAuth


    private  val viewModel by  viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.getDataForCurrentUser()
        //data for this user
        viewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    prof_ProgressBar.visibility=View.GONE
                    val user=it.data!!
                    glide.load(user.image).error(R.drawable.ic_profile).into(prof_image_profile)
                    prof_name.text=user.name
                    prof_bio.text=user.bio
                    prof_job_title.text=user.roll
                    prof_branch.text=user.branch
                    prof_email.text=user.email
                    prof_age.text=user.age
                    prof_phone.text=user.phone
                    prof_address.text=user.address
                    prof_date_of_start.text=user.dateOfStratWorking
                }
                Status.ERROR -> {
                    prof_ProgressBar.visibility=View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }






}