package com.example.fitplan.UI.login_register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitplan.model.User
import com.example.fitplan.repository.AuthRepository
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel(){

    private val _userRegistrationStatus = MutableLiveData<Resource<User>>()
    val userRegistrationStatus : LiveData<Resource<User>> = _userRegistrationStatus

    fun createUser(userEmail: String,userName:String, userPassword:String) {

        val error = if (userEmail.isEmpty() || userPassword.isEmpty())
            "One or more fields are empty"
        else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            "Email is not valid"
        } else null
        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
        }
        _userRegistrationStatus.postValue(Resource.Loading())
        viewModelScope.launch {
            val registrationResult = repository.createUser(userEmail,userName,userPassword)
            _userRegistrationStatus.postValue(registrationResult)
        }
    }

    class RegisterViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(repo) as T
        }
    }

}