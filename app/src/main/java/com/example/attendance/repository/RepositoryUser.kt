package com.example.attendance.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.attendance.models.User
import com.example.attendance.utils.Constants
import com.example.attendance.utils.Resource


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_reset_password.*
import javax.inject.Inject

class RepositoryUser @Inject constructor(
    private var refDatabase: DatabaseReference,
    private var refStorage: StorageReference,
    private var auth: FirebaseAuth,
    private var context: Context
) {

    lateinit var  downloadUri : String


    //
    suspend fun createUser(email: String, password: String, uri: Uri, user: User): Boolean  {
        var success = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                uploadUserData(uri,user)

            }.addOnFailureListener {
                Toast.makeText(context, " failure: " + it.message, Toast.LENGTH_SHORT).show()
            }

        return success
    }




    private val createUserLiveData = MutableLiveData<Resource<FirebaseUser>>()
    fun createUser(email: String, password: String) : MutableLiveData<Resource<FirebaseUser>> {
        createUserLiveData.value=Resource.loading(null)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                createUserLiveData.value=Resource.success(auth.currentUser)
            }.addOnFailureListener {
                createUserLiveData.value=Resource.error(it.localizedMessage ,null)

            }
        return createUserLiveData
    }



    private val userPictureLiveData = MutableLiveData<Resource<String>>()
    fun uploadUserPictureOnFireStorage(uri: Uri) :   MutableLiveData<Resource<String>>{
        userPictureLiveData.value=Resource.loading(null)
        auth.currentUser?.uid?.let {  id->
            refStorage.child(Constants.IMAGES).child(id).putFile(uri).
            addOnSuccessListener {
                var uriTask = it.storage.downloadUrl
                while (!uriTask.isSuccessful);
                downloadUri = uriTask.result.toString()
                //Toast.makeText(context, "repo: $downloadUri", Toast.LENGTH_SHORT).show()
                userPictureLiveData.value= Resource.success(downloadUri)

            }.addOnFailureListener {
                userPictureLiveData.value= Resource.error(it.localizedMessage,null)
            }
        }
        return userPictureLiveData
    }


    //
    private fun uploadUserData(uri: Uri, user: User) {
        auth.currentUser?.uid?.let {  id->
            refStorage.child(Constants.IMAGES).child(id).putFile(uri).addOnSuccessListener {
                var uriTask = it.storage.downloadUrl
                while (!uriTask.isSuccessful);
                downloadUri = uriTask.result.toString()
                user.image=downloadUri
                user.id=id

            }.addOnFailureListener {
                Toast.makeText(context, " failure from uploadUserData : " + it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val setUserDataInfoOnDatabaseLiveData = MutableLiveData<Resource<Boolean>>()
    fun setUserDataInfoOnDatabase(user: User): MutableLiveData<Resource<Boolean>> {
        setUserDataInfoOnDatabaseLiveData.value = Resource.loading(null)


        refDatabase.child(Constants.USERS).child(user.id + "").setValue(user)
            .addOnSuccessListener {
                setUserDataInfoOnDatabaseLiveData.value = Resource.success(true)
            }.addOnFailureListener {
                setUserDataInfoOnDatabaseLiveData.value =
                    Resource.error(it.localizedMessage, false)
            }





        return setUserDataInfoOnDatabaseLiveData
    }




    private val successToLoginLiveData = MutableLiveData<Resource<Boolean>>()
    suspend fun signInWithEmailAndPassword(email: String, password: String) : MutableLiveData<Resource<Boolean>> {
        successToLoginLiveData.value=Resource.loading(null)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                successToLoginLiveData.value=Resource.success(true)
            }.addOnFailureListener {
                successToLoginLiveData.value=Resource.error(it.message.toString(),false)

            }
        return successToLoginLiveData
    }


    // reset password
    private val successToResetPasswordLiveData = MutableLiveData<Resource<Boolean>>()
    suspend fun resetPassword(email : String): MutableLiveData<Resource<Boolean>> {
        successToResetPasswordLiveData.value=Resource.loading(null)
        auth.sendPasswordResetEmail (email).addOnCompleteListener { listener->
            if (listener.isSuccessful){
                successToResetPasswordLiveData.value=Resource.success(true)
                Toast.makeText(context, "Check your email now ..", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            successToResetPasswordLiveData.value=Resource.error(it.message.toString(),false)
            Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
        }
        return successToResetPasswordLiveData
    }



}