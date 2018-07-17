package com.android.rigger.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log

import java.io.File

/**
 * Created by Edgar on 2018/7/17.
 */
object FileProviderUtils {

    private val TAG = "Rigger:FileProvider"

    @JvmStatic fun getUriForFile(context: Context, path: String): Uri? {
        return getUriForFile(context, File(Utils.requireNonNull(path)))
    }

    @JvmStatic fun getUriForFile(context: Context, file: File?): Uri? {
        var data: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pm = context.packageManager
            try {
                val providerInfo = pm.getProviderInfo(ComponentName(context, FileProvider::class.java), 0)
                if (providerInfo != null) {
                    data = FileProvider.getUriForFile(context, context.packageName + ".provider", Utils.requireNonNull(file))
                } else {
                    Log.d(TAG, "android.support.v4.content.FileProvider not found.")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                val message = "请在AndroidManifest.xml中配置\n<provider\n" +
                        "     android:name=\"android.support.v4.content.FileProvider\"\n" +
                        "     android:authorities=\"{${context.packageName}}.provider\"\n" +
                        "     android:grantUriPermissions=\"true\"\n" +
                        "     android:exported=\"false\">\n" +
                        "         <meta-data\n" +
                        "              android:name=\"android.support.FILE_PROVIDER_PATHS\"\n" +
                        "              android:resource=\"@xml/file_paths\" />\n" +
                        "</provider>\n"
                Log.e(TAG, message, e)
            }

        } else {
            data = Uri.fromFile(file)
        }
        return data
    }

    /**
     * Add intent file uri
     * @param context 上下文
     * @param intent intent
     * @param file file
     * @param dataType 数据类型
     */
    @JvmStatic fun addIntentFileUri(context: Context, intent: Intent, file: File?, dataType: String) {
        val data = getUriForFile(context, file) ?: return
        intent.setDataAndType(data, dataType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}