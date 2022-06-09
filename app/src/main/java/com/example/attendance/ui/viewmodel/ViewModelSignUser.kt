package com.example.attendance.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendance.repository.RepositoryUser
import com.example.attendance.models.User
import com.example.attendance.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelSignUser @Inject constructor(
    private val repository: RepositoryUser,
    val context: Context
) : ViewModel() {

    val user = MutableLiveData<Resource<User>>()

    var successToLoginLiveData = MutableLiveData<Resource<Boolean>>()
    fun signInWithEmailAndPassword(email: String, password: String){
        viewModelScope.launch {
            successToLoginLiveData=repository.signInWithEmailAndPassword(email, password)
        }
    }





    var createUserLiveData = MutableLiveData<Resource<FirebaseUser>>()
    fun createUser(email: String, password: String){
        viewModelScope.launch {
            createUserLiveData=repository.createUser(email, password)
        }
    }

    //download Uri
    var userPictureLiveData = MutableLiveData<Resource<String>>()
    fun uploadUserPictureOnFireStorage(uri: Uri){
        viewModelScope.launch {
            userPictureLiveData=repository.uploadUserPictureOnFireStorage(uri)
        }
    }

    var setUserDataInfoOnDatabaseLiveData = MutableLiveData<Resource<Boolean>>()
    fun setUserDataInfoOnDatabase(user: User){
        viewModelScope.launch {
            setUserDataInfoOnDatabaseLiveData=repository.setUserDataInfoOnDatabase(user)
        }
    }



}