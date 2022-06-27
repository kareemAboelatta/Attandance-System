package com.example.attendance.ui.fragment.employee

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.attendance.R
import com.example.attendance.ui.activity.LoginAndSignUpActivity
import com.example.attendance.ui.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_more.*
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more){

    @Inject
    lateinit var auth: FirebaseAuth




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        more_btn_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginAndSignUpActivity::class.java))
            requireActivity().finish()
        }

        more_btn_update_my_data.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_updateDateFragment)

        }


    }

}