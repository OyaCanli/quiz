package com.example.android.quiz;

/**
 * Created by Oya on 14-12-17.
 */

public class QuestionLists {

    public static final String LIT_Q1 = "Which of the followings is not a novel by Charles Dickens?";
    public static final String LIT_H1 = "The answer is a novel written by Ernest Hemingway, set during the Italian Campaign of WWII";
    public static final String LIT_Q1_OPTA = "A Tale Of Two Cities";
    public static final String LIT_Q1_OPTB = "Oliver Twist";
    public static final String LIT_Q1_OPTC = "Bleak House";
    public static final String LIT_Q1_OPTD = "A Farewell To Arms";
    public static final String LIT_Q2 = "Which of the followings is not a novel by Dostoyevski?";
    public static final String LIT_H2 = "The answer is a novel written by Tolstoy, narrating the history of a married aristocratic woman who has a love affair";
    public static final String LIT_Q2_OPTA = "Crime and Punishment";
    public static final String LIT_Q2_OPTB = "Anna Karenina";
    public static final String LIT_Q2_OPTC = "Notes From Underground";
    public static final String LIT_Q2_OPTD = "The Brothers Karamazov";
    public static final String LIT_Q3 = "Which of the followings is not a novel by Balzac?";
    public static final String LIT_H3 = "The answer is a novel written by Jane Austen, narrating the story of two lovers who cannot surrender themselves to love because of their pride";
    public static final String LIT_Q3_OPTA = "The Black Sheep";
    public static final String LIT_Q3_OPTB = "Father Goriot";
    public static final String LIT_Q3_OPTC = "Pride and Prejudice";
    public static final String LIT_Q3_OPTD = "The Lily of the Valley";

    public static final String CIN_Q1 = "Which of the following is not a film by Michael Haneke?";
    public static final String CIN_H1 = "The answer is a film starring Adrien Bordy, set during Nazi invasion of Poland.";
    public static final String CIN_Q1_OPTA = "The Pianist";
    public static final String CIN_Q1_OPTB = "Funny Games";
    public static final String CIN_Q1_OPTC = "Hidden";
    public static final String CIN_Q1_OPTD = "Amour";
    public static final String CIN_Q2 = "Which of the following is not a film by Emir Kusturica?";
    public static final String CIN_H2 = "";
    public static final String CIN_Q2_OPTA = "Time of The Gypsies";
    public static final String CIN_Q2_OPTB = "Arizona Dream";
    public static final String CIN_Q2_OPTC = "Underground";
    public static final String CIN_Q2_OPTD =
    public static final String CIN_Q3 = "Which of the following is not a film by Lars Von Trier?";
    public static final String CIN_H3 = "";
    public static final String CIN_Q3_OPTA = "Antichrist";
    public static final String CIN_Q3_OPTB = "12 Angry Men";
    public static final String CIN_Q3_OPTC = "Dancer in the Dark";
    public static final String CIN_Q3_OPTD = "Dogville";


    public static final String[][] literatureQuestions = {
            {LIT_Q1, LIT_H1, LIT_Q1_OPTA, LIT_Q1_OPTB, LIT_Q1_OPTC, LIT_Q1_OPTD},
            {LIT_Q2, LIT_H2, LIT_Q2_OPTA, LIT_Q2_OPTB, LIT_Q2_OPTC, LIT_Q2_OPTD},
            {LIT_Q3, LIT_H3, LIT_Q3_OPTA, LIT_Q3_OPTB, LIT_Q3_OPTC, LIT_Q3_OPTD}
    };

    public static final int[] literatureCorrectAnswers = {
            R.id.optionD, R.id.optionB, R.id.optionC
    };

    public static final int[][] literatureWrongAnswers = {
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD}
    };

}
