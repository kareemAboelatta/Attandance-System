package com.example.attendance.ui.fragment.admin

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.R
import com.example.attendance.adapters.AdapterAttend
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.ui.viewmodel.ViewModelAdmin
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.fragment_update_admin.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentUpdateForAdminSide : Fragment(R.layout.fragment_update_admin) {


    @Inject
    lateinit var adapter: AdapterAttend
    lateinit var arrayList: List<String>
    lateinit var prog: ProgressDialog


    val args : FragmentUpdateForAdminSideArgs by navArgs()

    var userId:String ="args.user.id"
    var name:String ="args.user.name"

    private  val viewModel by  viewModels<ViewModelAdmin>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId=args.user.id
        name=args.user.name
        update_name.text=name

        btn_back_update.setOnClickListener {
            findNavController().popBackStack()
        }

        prog= ProgressDialog(activity)
        prog.setMessage("Wait a minute...")

        btn_update_name.setOnClickListener {
            showUpdateNameBioDialog("name")
        }

        btn_update_salary.setOnClickListener {
            showUpdateNameBioDialog("salary")

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
        }else if(key == "salary"){
            editText.hint="Enter Salary"
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