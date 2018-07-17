package com.android.rigger

abstract class PermissionCallback {

    /**
     * 请求权限成功
     * @param permission 权限集合
     */
    open fun onRequestPermissionSuccess(permission: String){}

    open fun onDenied(permission: String){}
}