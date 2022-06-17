package com.example.attendance.ui.fragment.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterAttend
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.*
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_address
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_age
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_bio
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_branch
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_date_of_start
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_email
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_image_profile
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_job_title
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_name
import kotlinx.android.synthetic.main.fragment_profile_for_admin_side.prof_phone
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentProfileForAdminSide : Fragment(R.layout.fragment_profile_for_admin_side) {


    @Inject
    lateinit var glide :RequestManager



    val args : FragmentProfileForAdminSideArgs  by navArgs()

    var userId:String ="args.user.id"
    var name:String ="args.user.name"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId=args.user.id
        name=args.user.name
        profile_name.text=name

        btn_back_profile.setOnClickListener {
            findNavController().popBackStack()
        }

        val user=args.user
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