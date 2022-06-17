package com.example.attendance.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendance.models.EventAdmin
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.models.VacationRequest
import com.example.attendance.repository.Repository
import com.example.attendance.repository.RepositoryAdmin
import com.example.attendance.utils.Constants
import com.example.attendance.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAdmin @Inject constructor(
    private val repository: RepositoryAdmin,
    val context: Context,
    val auth: FirebaseAuth,
    val ref : DatabaseReference
) : ViewModel() {




    private val _peopleAll: MutableLiveData<List<User>> = MutableLiveData()
    val peopleAll: LiveData<List<User>> get() = _peopleAll

    private fun getPeople(){
        val friendsList= mutableListOf<User>()
        ref.child(Constants.USERS)
            .get().addOnSuccessListener { friends->
                val data=friends.children
                data.forEach { friend ->
                    val x= friend.getValue<User>()
                    if (x?.admin == false)
                        friendsList.add(x!!)
                }
                _peopleAll.value=friendsList
            }
    }

    private val _employee: MutableLiveData<List<User>> = MutableLiveData()
    val employee: LiveData<List<User>> get() = _employee

    private fun getEmployee(){
        val friendsList= mutableListOf<User>()
        ref.child(Constants.USERS)
            .get().addOnSuccessListener { friends->
                val data=friends.children
                data.forEach { friend ->
                    val x= friend.getValue<User>()
                    if (x?.admin == true)
                        friendsList.add(x!!)
                }
                _employee.value=friendsList
            }
    }

    var acceptUserLiveData = MutableLiveData<Resource<User>>()
    fun acceptRequest(id: String) {
        viewModelScope.launch {
            acceptUserLiveData= repository.acceptRequest(id)
        }
    }


    var acceptVacationRequestLiveData = MutableLiveData<Resource<VacationRequest>>()
    fun acceptRequest(request: VacationRequest) {
        viewModelScope.launch {
            acceptVacationRequestLiveData= repository.acceptVacationRequest(request)
        }
    }


    init {
        viewModelScope.launch {
            launch {
                getPeople()
            }
            launch {
                getEmployee()
            }

        }

    }




    var reportsLiveData=MutableLiveData<Resource<List<Report>>>()
    fun getReports(){
        viewModelScope.launch {
            reportsLiveData=repository.getReports()
        }
    }

    var requestVacationLiveData=MutableLiveData<Resource<List<VacationRequest>>>()
    fun getRequestVacations(){
        viewModelScope.launch {
            requestVacationLiveData=repository.getRequestsOfVacations()
        }
    }

    private var _uploadEventLiveData: MutableLiveData<Resource<EventAdmin>> = MutableLiveData()
    val uploadEventLiveData: MutableLiveData<Resource<EventAdmin>> get() = _uploadEventLiveData
    fun addEvent(caption: String, priority: Int) {
        viewModelScope.launch {
            _uploadEventLiveData=repository.uploadEvent(caption, priority)
        }
    }


    var changeNameOrBioLiveData= MutableLiveData<Resource<Boolean>>()
    fun changeNameOrBio(id :String ,value: String,key: String){
        viewModelScope.launch {
            changeNameOrBioLiveData=repository.changeNameOrBio(id, value,key)
        }
    }


}