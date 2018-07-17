package com.android.rigger

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.util.SparseArrayCompat
import android.util.Log
import com.android.rigger.utils.Utils

/**
 * Created by Edgar on 2018/7/16.
 */
class RiggerPresenter(fragmentCompat: RiggerCompat.FragmentCompat) {

    private val mPermissionCallbacks: SparseArrayCompat<PermissionCallback> = SparseArrayCompat()
    private val mActivityResultCallbacks: SparseArrayCompat<ActivityResultCallback> = SparseArrayCompat()
    private val mFragmentCompat: RiggerCompat.FragmentCompat = fragmentCompat
    private var mFragmentAddedListener: OnFragmentAddedListener? = null

    companion object {
        @JvmStatic val TAG : String = "RiggerPresenter"
    }

    fun addPermissionCallback(requestCode: Int,callback: PermissionCallback) {
        mPermissionCallbacks.put(requestCode, callback)
    }

    private fun getPermissionCallback(requestCode: Int): PermissionCallback? = mPermissionCallbacks.get(requestCode)

    fun deliverPermissionResult(callback: PermissionCallback?, permission: String) {
        callback?.onRequestPermissionSuccess(permission)
    }

    fun deliverPermissionDenied(callback: PermissionCallback?, permission: String) {
        callback?.onDenied(permission)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG,"Rigger request permission result.")
        if (!Utils.isEmpty(permissions) && !Utils.isEmpty(grantResults)) {
            val callback = getPermissionCallback(requestCode)
            for (i in 0 until permissions.size) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    deliverPermissionResult(callback,permissions[i])
                } else {
                    deliverPermissionDenied(callback,permissions[i])
                }
            }
        }
        mPermissionCallbacks.remove(requestCode)
    }

    fun addResultCallback(requestCode: Int, callback: ActivityResultCallback) {
        mActivityResultCallbacks.put(requestCode, callback)
    }

    private fun deliverResult(requestCode: Int, data: Intent?) {
        val callback = mActivityResultCallbacks.get(requestCode)
        callback?.onResult(data)
        mActivityResultCallbacks.remove(requestCode)
    }

    private fun deliverCanceled(requestCode: Int) {
        val callback = mActivityResultCallbacks.get(requestCode)
        callback?.onCanceled()
        mActivityResultCallbacks.remove(requestCode)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG,"Rigger activity result successful.")
            deliverResult(requestCode,data)
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Log.d(TAG,"Rigger activity result canceled.")
            deliverCanceled(requestCode)
        }
    }

    fun onAdded(){
        mFragmentAddedListener?.onAdded()
        mFragmentAddedListener = null
    }

    fun setOnFragmentAddedListener(listener: OnFragmentAddedListener?) {
        mFragmentAddedListener = listener
    }

    fun isAdded():Boolean = mFragmentCompat.isAdded()

    fun onDestroy() {
        mActivityResultCallbacks.clear()
        mPermissionCallbacks.clear()
    }

    fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        mFragmentCompat.requestPermissions(permissions,requestCode)
    }

    fun startActivity(intent: Intent){
        mFragmentCompat.startActivity(intent)
    }

    fun startActivityForResult(intent: Intent, requestCode: Int){
        mFragmentCompat.startActivityForResult(intent,requestCode)
    }
}