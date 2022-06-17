package com.example.attendance.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.ViewModelSignUser
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_reset_password.*
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {



    private  val viewModel by  viewModels<ViewModelSignUser>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reset_btn_reset.setOnClickListener {
            var email = reset_email.text.toString()
            if (email.isNotEmpty()) {
                viewModel.resetPassword(email)
                viewModel.resetPasswordLiveData.observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.LOADING -> {
                            progress.visibility = View.VISIBLE
                        }
                        Status.SUCCESS -> {
                            progress.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                "Success to reset password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Status.ERROR -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, "" + it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else
                Toast.makeText(
                    requireActivity(),
                    "Enter Your email which is we will reset",
                    Toast.LENGTH_LONG
                ).show()

        }


    }



}