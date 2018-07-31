package com.igo.android.rigger.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.rigger.inject.IntentInject
import com.rigger.android.annotation.*

class TestActivity : AppCompatActivity() {

    @IntentValue(name = "keyString",defString = "测试默认值")
    @JvmField
    var text: String? = null

    @IntentValue(name = "keyChar",defChar = 'a')
    @JvmField
    var charTest: Char = '0'

    @IntentValue(name = "keyInt",defInt = 10)
    @JvmField
    var teInt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntentInject.inject(this,this)
        Log.d("Test","Rigger intent String:$text")
        Log.d("Test","Rigger intent Char:$charTest")
        Log.d("Test","Rigger intent Int:$teInt")
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().putExtra("key","test"))
        super.finish()
    }
}