package com.example.attendance.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.attendance.models.*
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



    //get request vacation
    private val acceptVacationRequestLiveData = MutableLiveData<Resource<VacationRequest>>()
    suspend fun acceptVacationRequest(request: VacationRequest): MutableLiveData<Resource<VacationRequest>> {
        refDatabase.child(Constants.VACATION_REQUESTS)
            .child(request.vacationId)
            .updateChildren(mapOf("accepted" to true))
            .addOnSuccessListener {
                sendNotificationForEmployeeForVacation(request.userId,request.userName,request.vacationId)
                acceptVacationRequestLiveData.value = Resource.success(null)
            }.addOnFailureListener {
                acceptVacationRequestLiveData.value = Resource.error(it.message.toString(), null)
            }
        return acceptVacationRequestLiveData
    }

    fun sendNotificationForEmployeeForVacation(userId:String,name:String,vacationId:String){
        val notification=Notification(
            "Hey ${name.split(" ")[0]}, Your request For Vacation had been accepted from admin",
            "vacation",
            vacationId
        )
        refDatabase.child(Constants.USERS)
            .child(userId)
            .child(Constants.NOTIFICATION)
            .child(vacationId)//name = kareem aboelatta
            .setValue(notification)
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


    private val requestVacationLiveData=MutableLiveData<Resource<List<VacationRequest>>>()
      fun getRequestsOfVacations(): MutableLiveData<Resource<List<VacationRequest>>> {
        var vacationList: ArrayList<VacationRequest> = ArrayList()
        requestVacationLiveData.value = Resource.loading(null)
        refDatabase.child(Constants.VACATION_REQUESTS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    vacationList.clear()
                    snapshot.children.forEach { child ->
                        val vacationRequest = child.getValue<VacationRequest>()
                        if (vacationRequest?.isAccepted == false)
                            vacationList.add(vacationRequest!!)
                    }
                    requestVacationLiveData.value = Resource.success(vacationList)
                }

                override fun onCancelled(error: DatabaseError) {
                    requestVacationLiveData.value = Resource.error(error.message, null)

                }
            })


        return requestVacationLiveData
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

    suspend fun changeNameOrBio(
        id: String,
        value: String,
        key: String
    ): MutableLiveData<Resource<Boolean>> {
        var changeNameOrBioLiveData = MutableLiveData<Resource<Boolean>>()

        refDatabase.child(Constants.USERS).child(id).child("" + key).setValue(value)
            .addOnSuccessListener {
                changeNameOrBioLiveData.value = Resource.success(true)

            }.addOnFailureListener {
                changeNameOrBioLiveData.value = Resource.error(it.localizedMessage, false)

            }



        return changeNameOrBioLiveData
    }

    suspend fun changeNameOrBio(
        id: String,
        value: Int,
        key: String
    ): MutableLiveData<Resource<Boolean>> {
        var changeNameOrBioLiveData = MutableLiveData<Resource<Boolean>>()

        refDatabase.child(Constants.USERS).child(id).child("" + key).setValue(value)
            .addOnSuccessListener {
                changeNameOrBioLiveData.value = Resource.success(true)

            }.addOnFailureListener {
                changeNameOrBioLiveData.value = Resource.error(it.localizedMessage, false)

            }



        return changeNameOrBioLiveData
    }



    var mProcessComment = true
    suspend fun postComment(report:Report,comment: Comment){
        //put this data in DB :
        refDatabase.child(Constants.REPORTS).child(report.reportId)
            .child(Constants.COMMENTS).child(comment.timeStamp)
            .setValue(comment).addOnSuccessListener { // added
                Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show()
                mProcessComment = true
                sendNotificationForEmployeeForRepotyReplaying(report.userId, report.reportId)
            }.addOnFailureListener { e -> //failed
                Toast.makeText(context, "" + e.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun sendNotificationForEmployeeForRepotyReplaying(userId:String, reportId:String){
        val notifaction=Notification(
            "Your report had been replayed from admin ",
            "report",
            reportId
        )
        refDatabase.child(Constants.USERS)
            .child(userId)
            .child(Constants.NOTIFICATION)
            .child(reportId) //name = kareem aboelatta
            .setValue(notifaction)
    }


}