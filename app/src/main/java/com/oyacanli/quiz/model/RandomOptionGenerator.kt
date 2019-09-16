package com.oyacanli.quiz.model

import java.util.*
import javax.inject.Inject

interface IRandomOptionGenerator {
    fun getRandomOptionsToErase(question: Question) : ArrayList<Option>
}

class RandomOptionGenerator @Inject constructor(): IRandomOptionGenerator {

    override fun getRandomOptionsToErase(question: Question) : ArrayList<Option> {
        val optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)
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