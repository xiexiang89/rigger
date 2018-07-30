package com.igo.android.rigger.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.rigger.inject.IntentInject
import com.rigger.android.annotation.*

class TestActivity : AppCompatActivity() {

    @IntentValue(name = "key")
    @JvmField
    var text: String? = null

    @IntentValue(name = "key2",fieldType = FieldType.CharSequence)
    @JvmField
    var charTest: CharSequence = ""

    @IntentValue(name = "testInt",defInt = 10)
    @JvmField
    var teInt: Int = 0

    @IntentValue(name = "testBean",fieldType = FieldType.Parcelable)
    @JvmField
    var testBean: TestBean? = null

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