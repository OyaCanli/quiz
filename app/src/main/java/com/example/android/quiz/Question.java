package com.example.android.quiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oya on 04-03-18.
 */

public class Question implements Parcelable {

    private String mQuestion;
    private String mHint;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mOption4;
    private int mCorrectOption;

    Question(String[] question_array, int correctOption) {
        mQuestion = question_array[0];
        mHint = question_array[1];
        mOption1 = question_array[2];
        mOption2 = question_array[3];
        mOption3 = question_array[4];
        mOption4 = question_array[5];
        mCorrectOption = correctOption;
    }

    String getQuestion() {
        return mQuestion;
    }

    String getHint(){
        return mHint;
    }

    String getOption1() {
        return mOption1;
    }

    String getOption2() {
        return mOption2;
    }

    String getOption3() {
        return mOption3;
    }

    String getOption4() {
        return mOption4;
    }

    int getCorrectAnswer() {
        return mCorrectOption;
    }

    /*
    * Implementation of Parcelable interface
    */

    private Question(Parcel in) {
        mQuestion = in.readString();
        mOption1 = in.readString();
        mOption2 = in.readString();
        mOption3 = in.readString();
        mOption4 = in.readString();
        mCorrectOption = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestion);
        dest.writeString(mOption1);
        dest.writeString(mOption2);
        dest.writeString(mOption3);
        dest.writeString(mOption4);
        dest.writeInt(mCorrectOption);
    }

}
