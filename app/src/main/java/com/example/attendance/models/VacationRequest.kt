package com.example.attendance.models

data class VacationRequest (
    var userId:String?="",
    var userName:String?="",
    var userEmail:String?="",
    var userImage:String?="",
    var caption:String="",
    var days:Int=1,
    var userRoll:String?="",
    var vacationId:String="",
    var requestTime:String="",
    var ismarked:Boolean=false
)