package com.igo.android.rigger

import android.app.Fragment
import android.content.Intent
import android.os.Build

import android.support.v4.app.Fragment as SupportFragment

/**
 * Created by Edgar on 2018/7/16.
 */
class RiggerCompat {

    companion object {
        @JvmStatic fun create(fragment: Fragment): FragmentCompat {
            return FragmentV11Compat(fragment)
        }

        @JvmStatic fun create(fragment: SupportFragment): FragmentCompat {
            return SupportFragmentCompat(fragment)
        }
    }

    interface FragmentCompat {
        fun requestPermissions(permissions: Array<String>, requestCode: Int)
        fun startActivity(intent: Intent)
        fun startActivityForResult(intent: Intent, requestCode: Int)
        fun isAdded(): Boolean
    }

    private class SupportFragmentCompat(fragment: SupportFragment) : FragmentCompat {
        private val mSupportFragment: SupportFragment = fragment

        override fun startActivityForResult(intent: Intent, requestCode: Int) {
            mSupportFragment.startActivityForResult(intent, requestCode)
        }

        override fun startActivity(intent: Intent) {
            mSupportFragment.startActivity(intent)
        }

        override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
            mSupportFragment.requestPermissions(permissions,requestCode)
        }

        override fun isAdded(): Boolean {
            return mSupportFragment.isAdded
        }
    }

    private class FragmentV11Compat(fragment: android.app.Fragment) : FragmentCompat {

        private val mFragment: android.app.Fragment = fragment

        override fun startActivity(intent: Intent) {
            mFragment.startActivity(intent)
        }

        override fun startActivityForResult(intent: Intent, requestCode: Int) {
            mFragment.startActivityForResult(intent, requestCode)
        }

        override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFragment.requestPermissions(permissions, requestCode)
            }
        }

        override fun isAdded(): Boolean {
            return mFragment.isAdded
        }
    }
}