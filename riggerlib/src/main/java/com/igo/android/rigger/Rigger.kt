package com.igo.android.rigger

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

import com.igo.android.rigger.utils.Utils

import java.io.Serializable
import java.util.*

/**
 *  * 1.权限请求封装,通过接口回调方式处理权限请求结果
 *  * 2.简化requestCode处理.
 */
class Rigger private constructor(activity: FragmentActivity) {

    private val mFragmentManager: FragmentManager
    private var mRiggerFragment: RiggerFragment? = null

    private var mPermissions: Array<String>? = null
    private var mRequestCode: Int = 0
    private var mTargetClazz: Class<*>? = null
    private var mParams: Bundle? = null

    init {
        mFragmentManager = activity.supportFragmentManager
        addRiggerFragment()
    }

    private fun addRiggerFragment() {
        mRiggerFragment = mFragmentManager.findFragmentByTag(makeFragmentTag()) as RiggerFragment?
        if (mRiggerFragment == null) {
            mRiggerFragment = RiggerFragment()
            val transaction = mFragmentManager.beginTransaction()
            transaction.add(mRiggerFragment, makeFragmentTag())
            transaction.commitNowAllowingStateLoss()
        }
    }

    fun requestCode(requestCode: Int): Rigger {
        mRequestCode = requestCode
        return this
    }

    fun permissions(permissions:Array<String>): Rigger {
        mPermissions = Utils.requestNonNull<String>(permissions)
        return this
    }

    fun request(callback: PermissionCallback) {
        var requestPermissions: Array<String>? = null
        var i = 0
        val z = mPermissions!!.size
        while (i < z) {
            val permission = mPermissions!![i]
            if (ActivityCompat.checkSelfPermission(mRiggerFragment!!.context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions = Utils.append(requestPermissions, permission, true)
            } else {
                mRiggerFragment!!.deliverPermissionResult(callback, permission)
            }
            i++
        }
        if (!Utils.isEmpty(requestPermissions)) {
            mRiggerFragment!!.addPermissionCallback(mRequestCode, callback)
            mRiggerFragment!!.requestPermissions(requestPermissions!!, mRequestCode)
        }
    }

    fun targetActivity(clazz: Class<*>): Rigger {
        mTargetClazz = Utils.requireNonNull(clazz)
        return this
    }

    fun start(callback: ActivityResultCallback?) {
        if (callback == null) {
            mRiggerFragment!!.startActivity(makeIntent())
        } else {
            mRiggerFragment!!.addResultCallback(mRequestCode, callback)
            mRiggerFragment!!.startActivityForResult(makeIntent(), mRequestCode)
        }
    }

    private fun makeIntent(): Intent {
        val intent = Intent(mRiggerFragment!!.context, mTargetClazz)
        if (mParams != null) {
            intent.putExtras(mParams!!)
        }
        return intent
    }

    fun put(key: String, value: String): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putString(key, value)
        return this
    }

    fun put(key: String, value: Array<String>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putStringArray(key, value)
        return this
    }

    fun putStringArrayList(key: String, value: ArrayList<String>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putStringArrayList(key, value)
        return this
    }

    fun put(key: String, value: CharSequence): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putCharSequence(key, value)
        return this
    }

    fun put(key: String, value: Array<CharSequence>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putCharSequenceArray(key, value)
        return this
    }

    fun put(key: String, value: Int): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putInt(key, value)
        return this
    }

    fun put(key: String, value: IntArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putIntArray(key, value)
        return this
    }

    fun putIntegerArrayList(key: String, value: ArrayList<Int>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putIntegerArrayList(key, value)
        return this
    }

    fun put(key: String, value: Short): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putShort(key, value)
        return this
    }

    fun put(key: String, value: ShortArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putShortArray(key, value)
        return this
    }

    fun put(key: String, value: Byte): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putByte(key, value)
        return this
    }

    fun put(key: String, value: ByteArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putByteArray(key, value)
        return this
    }

    fun put(key: String, value: Boolean): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putBoolean(key, value)
        return this
    }

    fun put(key: String, value: BooleanArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putBooleanArray(key, value)
        return this
    }

    fun put(key: String, value: Long): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putLong(key, value)
        return this
    }

    fun put(key: String, value: LongArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putLongArray(key, value)
        return this
    }

    fun put(key: String, value: Float): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putFloat(key, value)
        return this
    }

    fun put(key: String, value: FloatArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putFloatArray(key, value)
        return this
    }

    fun put(key: String, value: Double): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putDouble(key, value)
        return this
    }

    fun put(key: String, value: DoubleArray): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putDoubleArray(key, value)
        return this
    }

    fun put(key: String, value: Parcelable): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelable(key, value)
        return this
    }

    fun put(key: String, value: Array<Parcelable>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelableArray(key, value)
        return this
    }

    fun putParcelableArrayList(key: String, value: ArrayList<Parcelable>): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelableArrayList(key, value)
        return this
    }

    fun put(key: String, value: Serializable): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putSerializable(key, value)
        return this
    }

    companion object {

        private val TAG = "Rigger"

        fun on(activity: FragmentActivity): Rigger {
            return Rigger(activity)
        }

        private fun makeFragmentTag(): String {
            return "Fragment#" + RiggerFragment::class.java.canonicalName
        }
    }
}