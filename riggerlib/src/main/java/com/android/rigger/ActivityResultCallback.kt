package com.android.rigger

import android.content.Intent

open abstract class ActivityResultCallback {

    /**
     * 返回结果成功回调
     */
    open fun onResult(data:Intent?){}

    /**
     * 取消的回调
     */
    open fun onCanceled(){}
}