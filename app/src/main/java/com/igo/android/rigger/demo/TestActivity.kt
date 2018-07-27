package com.igo.android.rigger.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.rigger.inject.IntentInject
import com.rigger.android.annotation.StringParams

class TestActivity : AppCompatActivity() {

    @StringParams(key = "key")
    @JvmField
    var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntentInject.inject(this,this)
        Log.d("Test","Rigger intent:$text")
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().putExtra("key","test"))
        super.finish()
    }
}