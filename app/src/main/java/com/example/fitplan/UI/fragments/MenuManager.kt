package com.example.fitplan.UI.fragments

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.fitplan.R
import com.example.fitplan.UI.fragments.sign_out.SignOutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

object MenuManager {

    fun menuEnable(
        navController: NavController, bottomNavigationView: BottomNavigationView,
        supportFragmentManager:FragmentManager) {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.myWorkout_btn -> {
                    navController.navigate(R.id.myPlansFragment)
                    true
                }
                R.id.planWorkout_btn -> {
                    navController.navigate(R.id.planWorkoutFragment2)
                    true
                }
                R.id.social_btn -> {
                    navController.navigate(R.id.socialFragment)
                    true
                }
                R.id.run_btn -> {
                    navController.navigate(R.id.runFragment)
                    true
                }
                R.id.signOut_btn -> {
                    val signOutDialog = SignOutFragment()
                    signOutDialog.show(supportFragmentManager, "SignOutDialog")
                    true
                }
                else -> false
            }
        }
    }

    fun menuInRunNotEnable(bottomNavigationView: BottomNavigationView, dialog: androidx.appcompat.app.AlertDialog.Builder?){
        bottomNavigationView.setOnItemSelectedListener {
            dialog?.show()
            false
        }
    }

}