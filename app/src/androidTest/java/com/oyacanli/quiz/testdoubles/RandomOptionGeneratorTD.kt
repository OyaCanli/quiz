package com.oyacanli.quiz.testdoubles

import com.oyacanli.quiz.model.IRandomOptionGenerator
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import javax.inject.Inject

class RandomOptionGeneratorTD @Inject constructor(): IRandomOptionGenerator {

    override fun getRandomOptionsToErase(question: Question): ArrayList<Option> {
        return arrayListOf(Option.B, Option.D)
    }


}