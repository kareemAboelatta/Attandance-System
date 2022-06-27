package com.example.attendance.repository

import android.content.Context
import android.net.Uri
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

        val ref = refDatabase.child(Constants.VACATION_REQUESTS)
        ref.child(timeStamp).setValue(vacationRequest).addOnSuccessListener {
            requestVacationLiveData.value = Resource.success(true)
        }.addOnFailureListener { e ->
            requestVacationLiveData.value = Resource.error(e.message.toString(), false)
        }

        return requestVacationLiveData
    }




    // get attendance for this user
    private val attendanceLiveData = MutableLiveData<Resource<List<String>>>()
    suspend fun getAttendance(id:String): MutableLiveData<Resource<List<String>>> {
        refDatabase.child(Constants.USERS)
            .child(id).child(Constants.ATTENDANCE)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null && snapshot.exists()) {
                        var attendances = snapshot.value as HashMap<String, Any>
                        var list = ArrayList<String>()
                        attendances.forEach {
                            list.add(it.key)
                        }
                        attendanceLiveData.value = Resource.success(list)
                    }else
                        attendanceLiveData.value=Resource.success(emptyList())



                }

                override fun onCancelled(error: DatabaseError) {
                    attendanceLiveData.value = Resource.error(error.message, null)

                }
            })
        return attendanceLiveData
    }

    // get notifications for this user
    private val NotificatiosLiveData = MutableLiveData<Resource<List<Notification>>>()
    suspend fun getNotificatios(id:String): MutableLiveData<Resource<List<Notification>>> {
        var notificationList = ArrayList<Notification>()
      var x=  refDatabase.child(Constants.USERS)
            .child(id).child(Constants.NOTIFICATION)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationList.clear()
                    snapshot.children.forEach { child ->
                        val notification = child.getValue<Notification>()
                            notificationList.add(notification!!)
                    }
                    NotificatiosLiveData.value = Resource.success(notificationList)
                }
                override fun onCancelled(error: DatabaseError) {
                    NotificatiosLiveData.value = Resource.error(error.message, null)
                }
            })
        return NotificatiosLiveData
    }



    private val commentsLiveData=MutableLiveData<Resource<List<Comment>>>()
    suspend  fun loadComments(postId:String): MutableLiveData<Resource<List<Comment>>> {
        var commentsList: ArrayList<Comment> = ArrayList()
        commentsLiveData.value = Resource.loading(null)
        refDatabase.child(Constants.REPORTS).child(postId).child(Constants.COMMENTS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentsList.clear()
                    snapshot.children.forEach { child ->
                        val comment = child.getValue<Comment>()
                        commentsList.add(comment!!)
                    }
                    commentsLiveData.value = Resource.success(commentsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    commentsLiveData.value = Resource.error(error.message, null)

                }
            })
        return commentsLiveData
    }

    //get Current User Data
    private val getReportByIDLiveData= MutableLiveData<Resource<Report>>()
    suspend fun getReportByID(reportId: String): MutableLiveData<Resource<Report>> {
        getReportByIDLiveData.postValue(Resource.loading(null))
        refDatabase.child(Constants.REPORTS)
            .child(reportId)
            .get().addOnSuccessListener { snapShot->
                val report=snapShot.getValue(Report::class.java)
                getReportByIDLiveData.value= Resource.success(report)
            }.addOnFailureListener {
                getReportByIDLiveData.value= Resource.error(it.message.toString(),null)
            }
        return getReportByIDLiveData
    }

}