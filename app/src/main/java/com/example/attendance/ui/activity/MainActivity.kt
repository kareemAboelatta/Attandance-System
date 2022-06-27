package com.example.attendance.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.attendance.R
import com.example.attendance.ui.viewmodel.MainViewModel
import com.example.attendance.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController


    private  val viewModel by  viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        next_event_text.isSelected = true

        bottomNavigationView.background=null
        bottomNavigationView.menu.getItem(2).isEnabled=false


        navController = findNavController(R.id.container)
        bottomNavigationView.setupWithNavController(navController)

        fab.setOnClickListener {
            navController.navigate(R.id.sendReportFragment)
        }
        viewModel.getDataForCurrentUser()
        observeDataForUser()

        //get last event
        observeLastEvent()




    }

    private fun observeLastEvent() {
        viewModel.getLastEventLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        next_event_text.text = it.data.caption
                        next_event_text.isSelected = true
                        when (it.data.priority) {
                            1 -> {
                                next_event_text.background=resources.getDrawable(R.color.colorPrimary)
                            }
                            2 -> {
                                next_event_text.background=resources.getDrawable(R.color.colorGreen)
                            }
                            else -> {
                                next_event_text.background=resources.getDrawable(R.color.red)
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //observer for the data of the user
    private fun observeDataForUser(){
        viewModel.currentUserLiveData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    mainProgressBar.visibility=View.VISIBLE
                }
                Status.SUCCESS -> {
                    val user=it.data!!
                    if (!user.admin){
                        mainProgressBar.visibility=View.GONE
                        navController.popBackStack()
                        navController.navigate(R.id.notVerifiedFragment)
                    }else
                        mainProgressBar.visibility=View.GONE


                }
                Status.ERROR -> {
                    mainProgressBar.visibility=View.GONE
                    Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}