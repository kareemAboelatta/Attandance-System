package com.example.attendance.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
    var notification: String ="",
    var type: String ="vacation",
    var reportId: String =""
    ) : Parcelable
