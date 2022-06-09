package com.example.attendance.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name:String="",
    var bio:String="", //biography
    var email:String="", // email
    var id:String="", //firebase id
    var image:String="", // url of image
    var phone:String="", // phone number
    var age: String = "25",     //age of user
    var address :String="", // address of user
    var roll:String="project manager",  //roll in company
    var branch:String="technology",  //branch in company
    var dateOfStratWorking:String= "2020",  //date of start working
    var salary: Int = 4000, //salary
    var admin: Boolean =false

): Parcelable