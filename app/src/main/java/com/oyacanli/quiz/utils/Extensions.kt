package com.oyacanli.quiz.utils

import android.os.Bundle

inline fun <reified T : Enum<T>> Bundle.putEnum(key: String, enum: T) = putString(key, enum.name)

inline fun <reified T: Enum<T>> Bundle.getEnum(key: String): T? = getString(key)?.let { enumValueOf<T>(it) }

inline fun <reified T : Enum<T>> Bundle.putEnumList(key: String, enumList: List<T>) {
    val stringList : ArrayList<String> = ArrayList()
    for(enum in enumList){
        stringList.add(enum.name)
    }
    putStringArrayList(key, stringList)
}

inline fun <reified T: Enum<T>> Bundle.getEnumList(key: String): ArrayList<T> {
    val stringList = getStringArrayList(key)
    val enumList : ArrayList<T> = ArrayList()
    if(stringList != null){
        for(string in stringList){
            enumList.add(enumValueOf(string))
        }
    }
    return enumList
}