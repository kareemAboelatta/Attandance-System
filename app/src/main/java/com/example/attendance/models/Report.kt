package com.example.attendance.models

data class Report(
    var userId:String?="",
    var userName:String?="",
    var userEmail:String?="",
    var userImage:String?="",
    var caption:String="",
    var userRoll:String?="",
    var reportId:String="",
    var reportTime:String="",
    var ismarked:Boolean=false
    )