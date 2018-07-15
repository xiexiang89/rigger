package com.igo.android.rigger

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.util.SparseArrayCompat
import android.util.Log
import com.igo.android.rigger.utils.Utils
import java.lang.ref.WeakReference

class RiggerFragment : Fragment() {

    private val mPermissionCallbacks: SparseArrayCompat<WeakReference<PermissionCallback>> = SparseArrayCompat()
    private val mActivityResultCallbacks: SparseArrayCompat<WeakReference<ActivityResultCallback>> = SparseArrayCompat()

    companion object {
        @JvmStatic val TAG : String = "RiggerFragment"
    }

    fun addPermissionCallback(requestCode: Int,callback: PermissionCallback) {
        mPermissionCallbacks.put(requestCode, WeakReference(callback))
    }

    private fun getPermissionCallback(requestCode: Int):PermissionCallback? = mPermissionCallbacks.get(requestCode).get()

    fun deliverPermissionResult(callback: PermissionCallback?, permission: String) {
        callback?.onRequestPermissionSuccess(permission)
    }

    private fun deliverPermissionDenied(callback: PermissionCallback?,permission: String) {
        callback?.onDenied(permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        mActivityResultCallbacks.put(requestCode, WeakReference(callback))
    }

    private fun deliverResult(requestCode: Int, data: Intent?) {
        val callback = mActivityResultCallbacks.get(requestCode)
        callback.get()?.onResult(data)
        mActivityResultCallbacks.remove(requestCode)
    }

    private fun deliverCanceled(requestCode: Int) {
        val callback = mActivityResultCallbacks.get(requestCode)
        callback.get()?.onCanceled()
        mActivityResultCallbacks.remove(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG,"Rigger activity result successful.")
            deliverResult(requestCode,data)
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Log.d(TAG,"Rigger activity result canceled.")
            deliverCanceled(requestCode)
        }
    }

    override fun onDestroy() {
        mActivityResultCallbacks.clear()
        mPermissionCallbacks.clear()
        super.onDestroy()
    }
}