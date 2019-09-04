package com.oyacanli.quiz.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;

import java.util.ArrayList;
import java.util.List;

//Taken from the site https://github.com/linfaxin/MultiRowsRadioGroup/blob/master/multirowsradiogroup/src/main/java/com/linfaxin/multirowsradiogroup/MultiRowsRadioGroup.java
// Until I do or find something better
// I made some additions to the class

public class QuizRadioGroup extends RadioGroup {

    List<RadioButton> radioButtons = new ArrayList<>();

    public QuizRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuizRadioGroup(Context context) {
        super(context);
        init();
    }

    private void init(){
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            public void onChildViewRemoved(View parent, View child) {
                if (parent == QuizRadioGroup.this && child instanceof ViewGroup) {
                    for(RadioButton radioButton: getButtonListFromGroup((ViewGroup) child)){
                        radioButton.setOnCheckedChangeListener(null);
                    }
                }
            }
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (parent == QuizRadioGroup.this) {
                    if(child instanceof ViewGroup){
                        for(final RadioButton radioButton : getButtonListFromGroup((ViewGroup) child)){
                            if(radioButton.isChecked()){
                                check(radioButton.getId());
                            }

                            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){
                                        radioButton.setOnCheckedChangeListener(null);
                                        check(buttonView.getId());
                                        radioButton.setOnCheckedChangeListener(this);
                                    }
                                }
                            });
                        }
                    } else if(child instanceof RadioButton){
                        radioButtons.add((RadioButton) child);
                    }
                }
            }
        });
    }

    private ArrayList<RadioButton> getButtonListFromGroup(ViewGroup group){
        if(group==null) return new ArrayList<>();
        ArrayList<RadioButton> list=new ArrayList<>();
        getRadioButtonFromGroup(group, list);
        return list;
    }
    private void getRadioButtonFromGroup(ViewGroup group, ArrayList<RadioButton> list){
        for(int i=0,count=group.getChildCount();i<count;i++){
            View child = group.getChildAt(i);
            if(child instanceof RadioButton){
                list.add((RadioButton) child);
                radioButtons.add((RadioButton) child);

            }else if(child instanceof ViewGroup){
                getRadioButtonFromGroup((ViewGroup) child, list);
            }
        }
    }


    private boolean checking=false;
    @Override
    public void check(int id) {
        if(checking) return;
        checking=true;
        super.check(id);
        checking=false;
    }

    @IdRes
    public int getCheckedRadioButtonId() {
        return super.getCheckedRadioButtonId();
    }

    public void clearCheck() {
        super.clearCheck();
    }

    public RadioButton getRadioButtonAt(int i) {
        int size = radioButtons.size();
        if(size == 0 || size < i){
            return null;
        } else {
            return radioButtons.get(i);
        }
    }

}
