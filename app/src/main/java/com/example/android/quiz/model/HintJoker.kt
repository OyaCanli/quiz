package com.example.android.quiz.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
class HintJoker @Inject constructor(
        var isActive : Boolean,
        var isUsed : Boolean
) : Parcelable