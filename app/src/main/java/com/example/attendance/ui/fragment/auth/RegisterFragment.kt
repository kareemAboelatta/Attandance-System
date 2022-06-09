package com.example.attendance.ui.fragment.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendance.R
import com.example.attendance.helpers.MyValidation
import com.example.attendance.models.User
import com.example.attendance.ui.viewmodel.ViewModelSignUser
import com.example.attendance.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    var uri : Uri ?=null

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var mycontext: Context



    private val viewModel by viewModels<ViewModelSignUser>()





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        reg_btn_register.setOnClickListener {
            val name: String = inputTextLayoutName.editText!!.text.toString()
            val bio: String = inputTextLayoutBio.editText!!.text.toString()
            val email: String = inputTextLayoutEmail.editText!!.text.toString()

            val phone: String = inputTextLayoutPhone.editText!!.text.toString()
            val age: String = inputTextLayoutAge.editText!!.text.toString()
            val address: String = inputTextLayoutAddress.editText!!.text.toString()
            val roll: String = inputTextLayoutRoll.editText!!.text.toString()
            val branch: String = inputTextLayoutBranch.editText!!.text.toString()
            val date: String = inputTextLayoutDate.editText!!.text.toString()


            val password: String = inputTextLayoutPassword.editText!!.text.toString()

            if (valid()) {
                var user = User(
                    name,
                    bio,
                    email, "", "", phone, age , address ,roll,branch,date)


                viewModel.createUser(email, password)
                viewModel.createUserLiveData.observe(viewLifecycleOwner){
                    when(it.status){
                        Status.LOADING->{
                            progress.visibility = View.VISIBLE
                        }
                        Status.SUCCESS->{



                            // success to create account in firebase authentication
                            progress.visibility = View.INVISIBLE
                            user.id= auth.currentUser?.uid!!
                            viewModel.uploadUserPictureOnFireStorage(uri!!)
                            viewModel.userPictureLiveData.observe(requireActivity()){
                                when(it.status){
                                    Status.LOADING->{
                                        progress.visibility = View.VISIBLE
                                    }
                                    Status.SUCCESS->{
                                        // now it = image download url
                                        // get image download url and store it with data
                                        user.image= it.data.toString()
                                        progress.visibility = View.INVISIBLE
                                        viewModel.setUserDataInfoOnDatabase(user)
                                        viewModel.setUserDataInfoOnDatabaseLiveData.observe(viewLifecycleOwner) {
                                            when (it.status) {

                                                Status.SUCCESS -> {
                                                    // now success with storing the data in database
                                                    Toast.makeText(context, "Registration success ", Toast.LENGTH_SHORT)
                                                        .show()
                                                    progress.visibility = View.INVISIBLE
                                                    findNavController().navigate(
                                                        R.id.action_registerFragment_to_loginFragment
                                                    )
                                                }
                                                Status.ERROR -> {
                                                    progress.visibility = View.INVISIBLE
                                                    Toast.makeText(activity, "" + it.message, Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }

                                    Status.ERROR->{
                                        progress.visibility = View.INVISIBLE
                                        Toast.makeText(activity, ""+it.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }



                        }
                        Status.ERROR->{
                            progress.visibility = View.INVISIBLE
                            Toast.makeText(activity, ""+it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }



            }



        }



        //for image
        reg_image.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type="image/*"
                startActivityForResult(it, 0)
            }
        }



        reg_backtologin.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment
            )
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode == 0){
            uri=data?.data
            reg_image.setImageURI(uri)
        }
    }

    private fun valid():Boolean{
        val bio: String = inputTextLayoutBio.editText!!.text.toString()
        val phone: String = inputTextLayoutPhone.editText!!.text.toString()
        val age: String = inputTextLayoutAge.editText!!.text.toString()
        val address: String = inputTextLayoutAddress.editText!!.text.toString()
        val roll: String = inputTextLayoutRoll.editText!!.text.toString()
        val branch: String = inputTextLayoutBranch.editText!!.text.toString()
        val date: String = inputTextLayoutDate.editText!!.text.toString()

        if (!MyValidation.validateName(requireContext(),inputTextLayoutName)){
            return false
        }else if (bio.isEmpty()){
            inputTextLayoutBio.isHelperTextEnabled = true
            inputTextLayoutBio.helperText = "Require*"
            return false
        }else if (!MyValidation.isValidEmail(requireContext(),inputTextLayoutEmail)){
            return false
        }else if (bio.isEmpty()){
            inputTextLayoutBio.isHelperTextEnabled = true
            inputTextLayoutBio.helperText = "Require*"
            return false
        }else if (phone.isEmpty()){
            inputTextLayoutPhone.isHelperTextEnabled = true
            inputTextLayoutPhone.helperText = "Require*"
            return false
        }else if (age.isEmpty()){
            inputTextLayoutAge.isHelperTextEnabled = true
            inputTextLayoutAge.helperText = "Require*"
            return false
        }else if (address.isEmpty()){
            inputTextLayoutAddress.isHelperTextEnabled = true
            inputTextLayoutAddress.helperText = "Your address require*"
            return false
        }else if (roll.isEmpty()){
            inputTextLayoutRoll.isHelperTextEnabled = true
            inputTextLayoutRoll.helperText = "Roll is required*"
            return false
        }else if (branch.isEmpty()){
            inputTextLayoutBranch.isHelperTextEnabled = true
            inputTextLayoutBranch.helperText = "Require*"
            return false
        }else if (date.isEmpty()){
            inputTextLayoutDate.isHelperTextEnabled = true
            inputTextLayoutDate.helperText = "Require*"
            return false
        }else if (!MyValidation.validatePass(requireContext(),inputTextLayoutPassword)){
            return false
        }else if (uri==null){
            Toast.makeText(activity, "Select image !!", Toast.LENGTH_LONG).show()
            return false
        }else{
            return true
        }
    }


}