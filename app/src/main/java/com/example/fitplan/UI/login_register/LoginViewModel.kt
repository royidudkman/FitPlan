package com.example.fitplan.UI.login_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitplan.model.User
import com.example.fitplan.repository.AuthRepository

import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val authRep:AuthRepository) : ViewModel() {

    private val _userSignInStatus = MutableLiveData<Resource<User>>()
    val userSignInStatus: LiveData<Resource<User>> = _userSignInStatus

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser:LiveData<Resource<User>> = _currentUser

    init {
        viewModelScope.launch {
            _currentUser.postValue(Resource.Loading())
            _currentUser.postValue(authRep.currentUser())
        }
    }

    fun signInUser(userEmail: String, userPassword:String){
        if(userEmail.isEmpty() || userPassword.isEmpty())
            _userSignInStatus.postValue(Resource.Error("Empty email or password"))
        else{
            _userSignInStatus.postValue(Resource.Loading())
            viewModelScope.launch {
                val loginResult = authRep.login(userEmail,userPassword)
                _userSignInStatus.postValue(loginResult)
            }
        }
    }

    class LoginViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(repo) as T
        }
    }
}



