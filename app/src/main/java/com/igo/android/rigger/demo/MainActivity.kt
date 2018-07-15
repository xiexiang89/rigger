package com.igo.android.rigger.demo

import android.Manifest.permission.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.igo.android.rigger.ActivityResultCallback
import com.igo.android.rigger.PermissionCallback
import com.igo.android.rigger.Rigger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val REQUEST_CODE = 0x100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test.setOnClickListener {
            Rigger.on(this@MainActivity)
                    .requestCode(REQUEST_CODE)
                    .permissions(arrayOf(WRITE_EXTERNAL_STORAGE, CALL_PHONE))
                    .request(object : PermissionCallback() {
                        override fun onRequestPermissionSuccess(permission: String) {
                            Toast.makeText(this@MainActivity,"请求"+permission+"成功",Toast.LENGTH_LONG).show()
                        }

                        override fun onDenied(permission: String) {
                            super.onDenied(permission)
                            Toast.makeText(this@MainActivity,permission+"取消",Toast.LENGTH_LONG).show()
                        }
                    })
        }
        jump.setOnClickListener {
            Rigger.on(this@MainActivity)
                    .requestCode(REQUEST_CODE)
                    .targetActivity(TestActivity::class.java)
                    .put("key","main")
                    .start(object : ActivityResultCallback() {
                        override fun onResult(data: Intent?) {
                            super.onResult(data)
                            Toast.makeText(this@MainActivity,"result:"+data?.getStringExtra("key"),
                                    Toast.LENGTH_LONG).show()
                        }
                    })
        }
    }
}