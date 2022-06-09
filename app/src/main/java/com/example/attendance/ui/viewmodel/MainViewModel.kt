package com.example.attendance.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendance.models.EventAdmin
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.models.VacationRequest
import com.example.attendance.repository.Repository
import com.example.attendance.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
private val repository: Repository,
val context: Context,
val auth: FirebaseAuth,
) : ViewModel() {



    private var _currentUserLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
    val currentUserLiveData: LiveData<Resource<User>> get() = _currentUserLiveData
    fun getDataForCurrentUser() {
        viewModelScope.launch {
            _currentUserLiveData= repository.getCurrentUserData()
        }
    }


    var reportLiveData = MutableLiveData<Resource<Boolean>>()
    fun uploadReport(report: Report){
        viewModelScope.launch {
            reportLiveData=repository.uploadReport(report)
        }
    }
    var requestVacationLiveData = MutableLiveData<Resource<Boolean>>()
    fun uploadVacation(request: VacationRequest){
        viewModelScope.launch {
            requestVacationLiveData=repository.uploadrequestVacationLiveData(request)
        }
    }


    private var _getLastEventLiveData: MutableLiveData<Resource<EventAdmin>> = MutableLiveData()
    val getLastEventLiveData: LiveData<Resource<EventAdmin>> get() = _getLastEventLiveData
    fun getLastEvent() {
        viewModelScope.launch {
            _getLastEventLiveData=repository.getNewLastEvent()
        }

    }
    private var _getAttendanceLiveData : MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val getAttendanceLiveData: LiveData<Resource<List<String>>> get() = _getAttendanceLiveData
    fun getAttendance() {
        viewModelScope.launch {
            _getAttendanceLiveData=repository.getAttendance()
        }

    }



    init {
        getLastEvent()
    }
}