package com.oyacanli.quiz.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Joker (
        var isActive : Boolean = false,
        var isUsed : Boolean = false
) : Parcelable