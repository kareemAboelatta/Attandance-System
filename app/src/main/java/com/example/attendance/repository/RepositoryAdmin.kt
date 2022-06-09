package com.example.attendance.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.attendance.models.EventAdmin
import com.example.attendance.models.Report
import com.example.attendance.models.User
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

class RepositoryAdmin @Inject constructor(
    private var refDatabase: DatabaseReference,
    private var refStorage: StorageReference,
    private var auth: FirebaseAuth,
    private var context: Context
) {


    //get Current User Data
    private val userLiveData=MutableLiveData<Resource<User>>()
    suspend fun getUser(uid: String ): MutableLiveData<Resource<User>> {
        refDatabase.child(Constants.USERS)
            .child(uid)
            .get().addOnSuccessListener { snapShot->
                val user=snapShot.getValue(User::class.java)
                userLiveData.value= Resource.success(user)
            }.addOnFailureListener {
                userLiveData.value= Resource.error(it.message.toString(),null)
            }
        return userLiveData

    }

    //get accept User
    private val acceptUserLiveData = MutableLiveData<Resource<User>>()
    suspend fun acceptRequest(uid: String): MutableLiveData<Resource<User>> {
        refDatabase.child(Constants.USERS)
            .child(uid)
            .updateChildren(mapOf("admin" to true))
            .addOnSuccessListener {
                acceptUserLiveData.value = Resource.success(null)
            }.addOnFailureListener {
                acceptUserLiveData.value = Resource.error(it.message.toString(), null)
            }
        return acceptUserLiveData

    }



    private val reportLiveData=MutableLiveData<Resource<List<Report>>>()
    suspend   fun getReports(): MutableLiveData<Resource<List<Report>>> {
        var reportList: ArrayList<Report> = ArrayList()
        reportLiveData.value = Resource.loading(null)
        refDatabase.child(Constants.REPORTS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    reportList.clear()
                    snapshot.children.forEach { child ->
                        val report = child.getValue<Report>()
                        reportList.add(report!!)
                    }
                    reportLiveData.value = Resource.success(reportList)
                }

                override fun onCancelled(error: DatabaseError) {
                    reportLiveData.value = Resource.error(error.message, null)

                }
            })
        return reportLiveData
    }

    private val eventLiveData = MutableLiveData<Resource<EventAdmin>>()
    suspend fun uploadEvent(caption: String, priority: Int): MutableLiveData<Resource<EventAdmin>> {
        eventLiveData.value = Resource.loading(null)
        val event = EventAdmin(caption, priority)

        refDatabase.child(Constants.EVENT).setValue(event).addOnSuccessListener {
            eventLiveData.value = Resource.success(event)
        }.addOnFailureListener { e ->
            eventLiveData.value = Resource.error(e.message.toString(), event)
        }

        return eventLiveData
    }


}