package com.android.rigger.utils

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.android.rigger.ActivityResultCallback
import com.android.rigger.PermissionCallback
import com.android.rigger.Rigger

import java.io.File

/**
 * Created by Edgar on 2018/7/17.
 * 系统App工具类
 */
object SystemAppUtils {

    @JvmStatic val TAG = "Rigger:SystemAppUtils";
    @JvmStatic val REQUEST_CODE_CAMERA = 0x0001
    @JvmStatic val REQUEST_CODE_CALL_PHONE = 0x0002

    /**
     * 打开系统相机
     * @param context 当前activity上下文
     * @param outFile 输出文件
     * @param callback 结果回调
     */
    @JvmStatic fun openCamera(context: Activity, outFile: File,callback: ActivityResultCallback?) {
        Utils.requireNonNull(callback)
        if (ActivityCompat.checkSelfPermission(context,CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCameraInternal(context, outFile, callback)
        } else {
            Rigger.on(context)
                    .permissions(arrayOf(CAMERA))
                    .requestCode(REQUEST_CODE_CAMERA)
                    .request(object : PermissionCallback() {
                        override fun onGranted(permission: String) {
                            super.onGranted(permission)
                            openCameraInternal(context, outFile, callback)
                        }
                    })
        }
    }

    @JvmStatic private fun openCameraInternal(context: Activity, outFile: File,callback: ActivityResultCallback?) {
        Rigger.on(context)
                .requestCode(REQUEST_CODE_CAMERA)
                .targetAction(MediaStore.ACTION_IMAGE_CAPTURE)
                .put(MediaStore.EXTRA_OUTPUT, FileProviderUtils.getUriForFile(context, outFile))
                .start(callback)
    }

    /**
     * 启动拨打电话界面
     */
    @JvmStatic fun startCallPhone(activity: Activity, phone: String) {
        Utils.requireNonNull(phone,"Phone number is empty.")
        if (ActivityCompat.checkSelfPermission(activity,CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startCallPhoneInternal(activity,phone)
        } else {
            Rigger.on(activity)
                    .permissions(arrayOf(CALL_PHONE))
                    .requestCode(REQUEST_CODE_CALL_PHONE)
                    .request(object : PermissionCallback(){
                        override fun onGranted(permission: String) {
                            super.onGranted(permission)
                            startCallPhoneInternal(activity,phone)
                        }
                    })
        }
    }

    @SuppressLint("MissingPermission")
    @JvmStatic private fun startCallPhoneInternal(activity: Activity, phone: String) {
        activity.startActivity(Intent(ACTION_CALL, Uri.parse("tel:$phone")))
    }

    /**
     * 启动系统短信Application,后两个参数可选
     * @param context 上下文
     * @param phones 手机号数组,多个手机号
     * @param message 需要发送的消息
     */
    @JvmStatic fun startMessage(context: Context, phones: Array<String> = arrayOf(""), message: String="") {
        val phoneBuilder = StringBuilder()
        if (!phones.isEmpty()) {
            for (phone in phones) {
                phoneBuilder.append(phone).append(";")
            }
        }
        startMessageForUri(context,Uri.parse("smsto:" + phoneBuilder.toString()),message)
    }

    /**
     * 启动系统短信Application,后两个参数可选
     * @param context 上下文
     * @param 手机号Uri 格式: smsto:xxxxxx;xxxxxxx
     * @param message 需要发送的消息
     */
    @JvmStatic fun startMessageForUri(context: Context, smsToUri: Uri= Uri.parse("smsto:"), message: String="") {
        val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        intent.putExtra("sms_body", message)
        context.startActivity(intent)
    }

    /**
     * 启动系统拨号盘
     */
    @JvmStatic fun startDial(activity: Context, phone: String="") {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        activity.startActivity(intent)
    }

    /**
     * 启动系统拨号盘
     */
    @JvmStatic fun startDialForUri(context: Context, telUri: Uri= Uri.parse("tel:")) {
        val intent = Intent(Intent.ACTION_DIAL, telUri)
        context.startActivity(intent)
    }

    /**
     * 打开联系人
     */
    @JvmStatic fun startContact(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), requestCode)
    }

    /**
     * 安装Apk,此方法会启动系统安装界面
     */
    @JvmStatic fun installApk(context: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        FileProviderUtils.addIntentFileUri(context, intent, file, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 卸载App,此方法会启动系统卸载弹窗提示,如果不传packageName,默认值为当前context的packageName
     * @param context 上下文
     * @param packageName 需要卸载应用的包名,可选
     */
    @JvmStatic fun uninstallApp(context: Context, packageName: String=context.packageName) {
        Utils.requireNonNull(packageName)
        val uri = Uri.fromParts("package", packageName, null)
        val intent = Intent(Intent.ACTION_DELETE, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 打开应用详情界面,如果不指定packageName,默认值为context的packageName
     * @param context 上下文
     * @param packageName 应用包名,可选
     */
    @JvmStatic fun startApplicationDetailsSettings(context: Context, packageName: String=context.packageName) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}