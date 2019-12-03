package com.oyacanli.quiz


import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


fun buttonWithDrawableId(@DrawableRes resourceId: Int): Matcher<View> {
    return object : BoundedMatcher<View, RadioButton>(RadioButton::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has drawable resource $resourceId")
        }

        override fun matchesSafely(radioButton: RadioButton): Boolean {
            val expectedDrawable : Drawable = radioButton.context.resources.getDrawable(resourceId)
            val actualDrawable : Drawable = radioButton.background as Drawable
            return expectedDrawable.constantState == actualDrawable.constantState
        }
    }
}

fun textWithDrawableId(@DrawableRes resourceId: Int): Matcher<View> {
    return object : BoundedMatcher<View, TextView>(TextView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has drawable resource $resourceId")
        }

        override fun matchesSafely(textView: TextView): Boolean {
            val expectedDrawable : Drawable = textView.context.resources.getDrawable(resourceId)
            val actualDrawable : Drawable = textView.background as Drawable
            return expectedDrawable.constantState == actualDrawable.constantState
        }
    }
}

fun withBackgroundColor(@ColorRes colorId: Int): Matcher<View> {
    return object : BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has background color $colorId")
        }

        override fun matchesSafely(view: View): Boolean {
            val actualColor : Int = (view.background as ColorDrawable).color
            val expectedColor  = view.context.getColor(colorId)
            return actualColor == expectedColor
        }
    }
}

fun lessThen(upperLimit: Int): Matcher<View> {
    return object : BoundedMatcher<View, TextView>(TextView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("should have less than $upperLimit")
        }

        override fun matchesSafely(textView: TextView): Boolean {
            val actualText = textView.text.toString().trim()
            val actualNumber = Integer.valueOf(actualText)
            return actualNumber < upperLimit
        }
    }
}