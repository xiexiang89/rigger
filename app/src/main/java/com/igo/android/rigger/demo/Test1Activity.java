package com.igo.android.rigger.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.rigger.inject.IntentInject;
import com.rigger.android.annotation.IntentValue;

/**
 * Created by Edgar on 2018/7/30.
 */
public class Test1Activity extends AppCompatActivity{

    @IntentValue(name = "intKey",defInt = 20)
    int testInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentInject.inject(this,this);
        Toast.makeText(this,"value:"+testInt,Toast.LENGTH_SHORT).show();
    }
}
