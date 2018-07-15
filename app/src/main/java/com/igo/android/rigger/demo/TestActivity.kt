package com.igo.android.rigger.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Test","Rigger intent:"+intent.getStringExtra("key"))
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().putExtra("key","test"))
        super.finish()
    }
}