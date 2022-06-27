package com.example.attendance.ui.fragment.employee

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_update.*

@AndroidEntryPoint
class UpdateDateFragment : Fragment(R.layout.fragment_update) {

    lateinit var prog: ProgressDialog


    lateinit var userId:String

    private  val viewModel by  viewModels<ViewModelAdmin>()
    private  val myViewModel by  viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myViewModel.getDataForCurrentUser()
        myViewModel.currentUserLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.LOADING -> {
                    update_progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val user = it.data!!
                    update_progress_bar.visibility = View.GONE
                    update_name.text=user.name
                    update_text.text="Hey ${user.name.split(" ")[0]} What you want to update ?"
                    userId=user.id
                }
                Status.ERROR -> {
                    update_progress_bar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }





        btn_back_update.setOnClickListener {
            findNavController().popBackStack()
        }

        prog= ProgressDialog(activity)
        prog.setMessage("Wait a minute...")

        btn_update_name.setOnClickListener {
            showUpdateNameBioDialog("name")
        }

        btn_update_bio.setOnClickListener {
            showUpdateNameBioDialog("bio")

        }

        btn_update_roll.setOnClickListener {
            showUpdateNameBioDialog("roll")

        }

        btn_update_branch.setOnClickListener {
            showUpdateNameBioDialog("branch")
        }


    }




    private fun showUpdateNameBioDialog(key: String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Update $key")
        builder.setCancelable(false)
        val linearLayout = LinearLayout(activity)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(10, 10, 10, 10)


        val editText = EditText(activity)
        if (key == "name"){
            editText.hint = "Enter new name"
        }else if(key == "bio"){
            editText.hint="Enter new bio"
            editText.inputType= InputType.TYPE_CLASS_NUMBER
        }else if(key == "roll"){
            editText.hint="Enter new roll"
        }else if(key == "branch"){
            editText.hint="Enter new branch"
        }
        editText.setHintTextColor(resources.getColor(R.color.colorGray))
        linearLayout.addView(editText)
        builder.setView(linearLayout)


        builder.setPositiveButton("Update") { _, _ ->
            val value=editText.text.toString()
            if (value==null){
                Toast.makeText(activity, "Where's Your new $key", Toast.LENGTH_SHORT).show()
            }else{
                //update key
                viewModel.changeNameOrBio(userId,value,key)
                prog.show()
                viewModel.changeNameOrBioLiveData.observe(viewLifecycleOwner){
                    when(it.status){
                        Status.SUCCESS->{
                            prog.dismiss()
                            Toast.makeText(activity, "$key changed", Toast.LENGTH_SHORT).show()
                        }
                        Status.ERROR->{
                            prog.dismiss()
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


        }

        builder.setNegativeButton("Cancel") { _, _ ->


        }

        val myAlertDialog: AlertDialog = builder.create()
        myAlertDialog.setOnShowListener {
            myAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorGreen));
            myAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.red));
            myAlertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.red));
        }
        myAlertDialog.show()

    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.bottomAppBar?.visibility = View.GONE
        activity?.next_event_text?.visibility = View.GONE
        activity?.fab?.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.bottomAppBar?.visibility = View.VISIBLE
        activity?.fab?.visibility = View.VISIBLE
        activity?.next_event_text?.visibility = View.VISIBLE

    }





}