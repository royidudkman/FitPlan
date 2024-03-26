package com.example.fitplan.UI.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitplan.repository.AuthRepository

class ProfileViewModel(private val authRep:AuthRepository): ViewModel() {

    fun signOut(){
        authRep.logout()
    }

    fun setUsername(newUserName:String){
       // authRep.setUsername(authRep,newUserName)
    }

    class ProfileViewModelFactory(val authRepo: AuthRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(authRepo) as T
        }
    }

}