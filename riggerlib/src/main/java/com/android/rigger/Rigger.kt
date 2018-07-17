package com.android.rigger

import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager as SupportFragmentManager

import com.android.rigger.utils.Utils

import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.*

/**
 *  * 1.权限请求封装,通过接口回调方式处理权限请求结果
 *  * 2.简化requestCode处理.
 */
class Rigger private constructor(activity: Activity) {

    private var mContextRef: WeakReference<Context> = WeakReference(activity)
    private var mRiggerPresenter: RiggerPresenter

    private var mPermissions: Array<String>? = null
    private var mRequestCode: Int = 0
    private var mTargetClazz: Class<*>? = null
    private var mAction: String? = null
    private var mParams: Bundle? = null

    init {
        if (activity is FragmentActivity) {
            mRiggerPresenter = getSupportRiggerPresenter(activity.supportFragmentManager)
        } else {
            mRiggerPresenter = getRiggerPresenter(activity.fragmentManager)
        }
    }

    private fun getRiggerPresenter(fm: FragmentManager): RiggerPresenter {
        var riggerFragment = fm.findFragmentByTag(RIGGER_FRAGMENT_TAG) as RiggerFragment?
        if (riggerFragment == null) {
            riggerFragment = RiggerFragment()
            val transaction = fm.beginTransaction()
            transaction.add(riggerFragment, RIGGER_FRAGMENT_TAG)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                transaction.commitNowAllowingStateLoss()
            } else {
                transaction.commitAllowingStateLoss()
            }
        }
        return riggerFragment.getRiggerPresenter()
    }

    private fun getSupportRiggerPresenter(fm: SupportFragmentManager): RiggerPresenter {
        var riggerFragment = fm.findFragmentByTag(RIGGER_FRAGMENT_TAG) as SupportRiggerFragment?
        if (riggerFragment == null) {
            riggerFragment = SupportRiggerFragment()
            val transaction = fm.beginTransaction()
            transaction.add(riggerFragment, RIGGER_FRAGMENT_TAG)
            transaction.commitNowAllowingStateLoss()
        }
        return riggerFragment.getRiggerPresenter()
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
        val z = mPermissions?.size?:0
        for ( i in 0 until z) {
            val permission = mPermissions!![i]
            if (ActivityCompat.checkSelfPermission(mContextRef.get()!!, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions = Utils.append(requestPermissions, permission, true)
            } else {
                mRiggerPresenter.deliverPermissionGranted(callback, permission)
            }
        }
        if (requestPermissions?.isNotEmpty() == true) {
            //6.0+才去动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mRiggerPresenter.isAdded()) {
                    mRiggerPresenter.addPermissionCallback(mRequestCode, callback)
                    mRiggerPresenter.requestPermissions(requestPermissions, mRequestCode)
                } else {
                    mRiggerPresenter.setOnFragmentAddedListener(object : OnFragmentAddedListener{
                        override fun onAdded() {
                            mRiggerPresenter.addPermissionCallback(mRequestCode, callback)
                            mRiggerPresenter.requestPermissions(requestPermissions, mRequestCode)
                        }
                    })
                }
            } else {
                val N = requestPermissions.size
                for ( i in 0 until N) {
                    val permission = requestPermissions[i]
                    mRiggerPresenter.deliverPermissionDenied(callback, permission)
                }
                mRiggerPresenter.deliverPermissionsDenied(callback,requestPermissions)
            }
        } else {
            mRiggerPresenter.deliverPermissionsGranted(callback, mPermissions)
        }
    }

    fun targetActivity(clazz: Class<*>): Rigger {
        mTargetClazz = Utils.requireNonNull(clazz)
        return this
    }

    fun targetAction(action: String): Rigger {
        mAction = Utils.requireNonNull(action)
        return this
    }

    fun start(callback: ActivityResultCallback?) {
        val intent = makeIntent()
        if (callback == null) {
            if (mRiggerPresenter.isAdded()) {
                mRiggerPresenter.startActivity(intent)
            } else {
                mRiggerPresenter.setOnFragmentAddedListener(object : OnFragmentAddedListener {
                    override fun onAdded() {
                        mRiggerPresenter.startActivity(intent)
                    }
                })
            }
        } else {
            if (mRiggerPresenter.isAdded()) {
                mRiggerPresenter.addResultCallback(mRequestCode, callback)
                mRiggerPresenter.startActivityForResult(intent, mRequestCode)
            } else {
                mRiggerPresenter.setOnFragmentAddedListener(object : OnFragmentAddedListener {
                    override fun onAdded() {
                        mRiggerPresenter.addResultCallback(mRequestCode, callback)
                        mRiggerPresenter.startActivityForResult(intent, mRequestCode)
                    }
                })
            }
        }
    }

    private fun makeIntent(): Intent {
        var intent: Intent? = null
        if (mTargetClazz != null) {
            intent = Intent(mContextRef.get()!!,mTargetClazz)
        }
        if (intent == null && mAction?.isEmpty() == false) {
            intent = Intent(mAction)
        }
        if (intent == null) {
            throw IllegalArgumentException("invalid intent target,please config targetClass or action")
        }
        if (mParams != null) {
            intent.putExtras(mParams!!)
        }
        return intent
    }

    fun put(key: String, value: String?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putString(key, value)
        return this
    }

    fun put(key: String, value: Array<String>?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putStringArray(key, value)
        return this
    }

    fun putStringArrayList(key: String, value: ArrayList<String>?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putStringArrayList(key, value)
        return this
    }

    fun put(key: String, value: CharSequence?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putCharSequence(key, value)
        return this
    }

    fun put(key: String, value: Array<CharSequence>?): Rigger {
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

    fun put(key: String, value: IntArray?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putIntArray(key, value)
        return this
    }

    fun putIntegerArrayList(key: String, value: ArrayList<Int>?): Rigger {
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

    fun put(key: String, value: ShortArray?): Rigger {
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

    fun put(key: String, value: ByteArray?): Rigger {
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

    fun put(key: String, value: BooleanArray?): Rigger {
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

    fun put(key: String, value: LongArray?): Rigger {
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

    fun put(key: String, value: FloatArray?): Rigger {
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

    fun put(key: String, value: DoubleArray?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putDoubleArray(key, value)
        return this
    }

    fun put(key: String, value: Parcelable?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelable(key, value)
        return this
    }

    fun put(key: String, value: Array<Parcelable>?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelableArray(key, value)
        return this
    }

    fun putParcelableArrayList(key: String, value: ArrayList<Parcelable>?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putParcelableArrayList(key, value)
        return this
    }

    fun put(key: String, value: Serializable?): Rigger {
        if (mParams == null) {
            mParams = Bundle()
        }
        mParams!!.putSerializable(key, value)
        return this
    }

    companion object {

        private val RIGGER_FRAGMENT_TAG = "Rigger"

        @JvmStatic fun on(activity: Activity): Rigger {
            return Rigger(activity)
        }
    }
}