package com.example.attendance.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.transition.Transition
import com.example.attendance.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin_main.*

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {

    lateinit var navController: NavController


    var changeBounds = ChangeBounds()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        navController = findNavController(R.id.container)
        bottom_menu.setItemSelected(R.id.requestForJoin)
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
            }
            }
            bottom_menu.showBadge(R.id.requestForJoin, 2)
            bottom_menu.showBadge(R.id.reports, 1)


        }

        expand_button.setOnClickListener {
            if (bottom_menu.isExpanded()) {
                TransitionManager.beginDelayedTransition(container_Main, changeBounds)
                bottom_menu.collapse()
                expand_button.setImageResource(R.drawable.ic_arrow_forward)

            } else {
                TransitionManager.beginDelayedTransition(container_Main, changeBounds)
                bottom_menu.expand()
                expand_button.setImageResource(R.drawable.ic_arrow_back)

            }

        }





    }
}