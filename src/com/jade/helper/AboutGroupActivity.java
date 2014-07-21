package com.jade.helper;

import com.jade.helper.AnimatedActivity;



import android.content.Intent;
import android.os.Bundle;

public class AboutGroupActivity extends AnimatedActivity
{
	public static AboutGroupActivity AboutGroupStack;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        AboutGroupStack = AboutGroupActivity.this;
        
       startChildActivity("AboutGroupActivity", new Intent(this, AboutActivity.class));
    }
} 