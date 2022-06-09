package com.example.attendance.ui.fragment.employee

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.attendance.R
import com.example.attendance.ui.activity.LoginAndSignUpActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_not_verified.*
import javax.inject.Inject


@AndroidEntryPoint
class NotVerifiedFragment : Fragment(R.layout.fragment_not_verified) {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginAndSignUpActivity::class.java))
            requireActivity().finish()
        }
    }


    //get current date
    fun getCurrentDate(): String {
        val c = java.util.Calendar.getInstance().time
        val df = java.text.SimpleDateFormat("yyyy-MM-dd")
        return df.format(c)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.fab?.visibility = View.GONE

    }


    override fun onPause() {
        super.onPause()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
    }

}