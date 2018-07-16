package com.igo.android.rigger

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

class SupportRiggerFragment : Fragment() {

    private val mRiggerPresenter: RiggerPresenter = RiggerPresenter(RiggerCompat.create(this))

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mRiggerPresenter.onAdded()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mRiggerPresenter.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mRiggerPresenter.onActivityResult(requestCode,resultCode,data)
    }

    fun getRiggerPresenter() : RiggerPresenter = mRiggerPresenter

    override fun onDestroy() {
        super.onDestroy()
        mRiggerPresenter.onDestroy()
    }
}