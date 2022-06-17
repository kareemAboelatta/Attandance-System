package com.example.attendance.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.transition.Transition
import com.example.attendance.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {

    lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)


        navController = findNavController(R.id.container)
        bottom_menu.setItemSelected(R.id.reports)
        bottom_menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.requestForJoin -> {
                    navController.navigate(R.id.fragmentRequests)
                }
                R.id.reports -> {
                    navController.navigate(R.id.fragmentReports)
                }
                R.id.allUsers -> {
                    navController.navigate(R.id.fragmentAllEmps)
                }
                R.id.add_event -> {
                    navController.navigate(R.id.fragmentAddEvent)

                }R.id.vacation_requests -> {
                    navController.navigate(R.id.fragmentVacationRequests)

                }
            }
            bottom_menu.showBadge(R.id.requestForJoin, 2)
            bottom_menu.showBadge(R.id.reports, 1)


        }






    }


}