package com.example.android.quiz.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import javax.inject.Inject

@Parcelize
class HalfJoker @Inject constructor(
        var isActive : Boolean,
        var isUsed : Boolean,
        var optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)
) : Parcelable{

    fun getRandomOptionsToErase(question: Question): ArrayList<Option> {
        //Index of the correct option
        val indexOfCorrectOption = question.correctOption.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)

        return optionsToErase
    }

}