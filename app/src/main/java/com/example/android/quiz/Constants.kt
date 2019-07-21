package com.example.android.quiz

import com.example.android.quiz.model.Option

const val CURRENT_MILLIS = "SavedStateOfCurrentMillis"
const val QUESTIONNUMBER = "SavedStateOfQuestionNumber"
const val HINT_COUNTER = "SavedStateOfHintCounter"
const val HALF_COUNTER = "SavedStateOfHalfCounter"
const val IS_NEXT_ENABLED = "SavedStateOfIsNextEnabled"
const val IS_HALF_ACTIF = "SavedStateOfIsHalfLifeLineActif"
const val CORRECT_OPTION_IS_SHOWN = "SavedStateOfCorrectOptionIsShown"
const val WRONG_OPTION_IS_SHOWN = "SavedStateOfWrongOptionIsShown"
const val IS_PAUSED = "SavedStateOfIsPaused"
const val IS_HINT_VISIBLE = "SavedStateOfIsHintVisible"
const val WRONG_OPTION_ID = "wrongOptionId"

const val SCORE = "score"
const val NAME = "name"
const val CATEGORY = "category"

const val BEST_SCORE_LIT = "bestScoreLiterature"
const val BEST_SCORE_CIN = "bestScoreCinema"
const val BEST_SCORE_SCI = "bestScoreScience"

val literatureCorrectAnswers : ArrayList<Option> = arrayListOf(Option.D, Option.B, Option.C, Option.B, Option.A)
val cinemaCorrectAnswers : ArrayList<Option> = arrayListOf(Option.A, Option.D, Option.B, Option.A, Option.C)
val scienceCorrectAnswers: ArrayList<Option> = arrayListOf(Option.A, Option.A, Option.C, Option.D, Option.B)

