package com.android.rigger

abstract class PermissionCallback {

    /**
     * 请求权限成功
     * @param permission 权限集合
     */
    open fun onGranted(permission: String){}
    open fun onGranted(permissions: Array<out String>?){}

    /**
     * 未请求权限
     */
    open fun onDenied(permission: String){}

    /**
     * 未请求的权限集合
     */
    open fun onDenied(permissions: Array<out String>?){}
}