package com.oyacanli.quiz.common

interface BasePresenter<in V: BaseView> {
    fun subscribe(view: V)
    fun unsubscribe()
}

interface BaseView