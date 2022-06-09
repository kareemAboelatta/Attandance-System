package com.example.attendance.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.attendance.models.EventAdmin
import com.example.attendance.models.Report
import com.example.attendance.models.User
import com.example.attendance.models.VacationRequest
import com.example.attendance.utils.Constants
import com.example.attendance.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class Repository @Inject constructor(
    private var refDatabase: DatabaseReference,
    private var refStorage: StorageReference,
    private var auth: FirebaseAuth,
    private var context: Context
) {



    //get Current User Data
    private val currentUserLiveData= MutableLiveData<Resource<User>>()
    suspend fun getCurrentUserData(): MutableLiveData<Resource<User>> {
        currentUserLiveData.postValue(Resource.loading(null))
        refDatabase.child(Constants.USERS)
            .child(auth.currentUser?.uid!!)
            .get().addOnSuccessListener { snapShot->
                val user=snapShot.getValue(User::class.java)
                currentUserLiveData.value= Resource.success(user)
            }.addOnFailureListener {
                currentUserLiveData.value= Resource.error(it.message.toString(),null)
            }
        return currentUserLiveData
    }


    //get last event data
    private val lastEventLiveData= MutableLiveData<Resource<EventAdmin>>()
    suspend fun getLastEvent(): MutableLiveData<Resource<EventAdmin>> {
        refDatabase.child(Constants.EVENT)
            .get().addOnSuccessListener { snapShot->
                val event=snapShot.getValue(EventAdmin::class.java)
                lastEventLiveData.value= Resource.success(event)
            }.addOnFailureListener {
                lastEventLiveData.value= Resource.error(it.message.toString(),null)
            }
        return lastEventLiveData
    }

    //get last event data
    private val getNewLastEvent= MutableLiveData<Resource<EventAdmin>>()
    suspend fun getNewLastEvent(): MutableLiveData<Resource<EventAdmin>> {
        refDatabase.child(Constants.EVENT)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val event=snapshot.getValue(EventAdmin::class.java)
                    getNewLastEvent.value = Resource.success(event)
                }

                override fun onCancelled(error: DatabaseError) {
                    getNewLastEvent.value = Resource.error(error.message, null)

                }
            })
        return getNewLastEvent
    }



    private val reportLiveData = MutableLiveData<Resource<Boolean>>()
    suspend fun uploadReport(report: Report): MutableLiveData<Resource<Boolean>> {
        val timeStamp = System.currentTimeMillis().toString()
        report.reportTime = timeStamp
        report.reportId = timeStamp

        val ref = refDatabase.child(Constants.REPORTS)
        ref.child(timeStamp).setValue(report).addOnSuccessListener {
            reportLiveData.value = Resource.success(true)
        }.addOnFailureListener { e ->
            reportLiveData.value = Resource.error(e.message.toString(), false)
        }

        return reportLiveData
    }
    private val requestVacationLiveData = MutableLiveData<Resource<Boolean>>()
    suspend fun uploadrequestVacationLiveData(vacationRequest: VacationRequest): MutableLiveData<Resource<Boolean>> {
        requestVacationLiveData.value = Resource.loading(true)
        val timeStamp = System.currentTimeMillis().toString()
        vacationRequest.requestTime = timeStamp
        vacationRequest.vacationId = timeStamp

        val ref = refDatabase.child(Constants.vacationRequests)
        ref.child(timeStamp).setValue(vacationRequest).addOnSuccessListener {
            requestVacationLiveData.value = Resource.success(true)
        }.addOnFailureListener { e ->
            requestVacationLiveData.value = Resource.error(e.message.toString(), false)
        }

        return requestVacationLiveData
    }




    // get attendance for this user
    private val attendanceLiveData = MutableLiveData<Resource<List<String>>>()
    suspend fun getAttendance(): MutableLiveData<Resource<List<String>>> {
        refDatabase.child(Constants.USERS)
            .child(auth.currentUser?.uid!!).child(Constants.ATTENDANCE)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var attendances = snapshot.value as HashMap<String, Any>
                    var list = ArrayList<String>()
                    attendances.forEach {
                        list.add(it.key)
                    }
                    attendanceLiveData.value = Resource.success(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    attendanceLiveData.value = Resource.error(error.message, null)

                }
            })
        return attendanceLiveData
    }






}