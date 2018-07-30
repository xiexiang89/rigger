package com.android.rigger.inject

import android.app.Activity

/**
 * Created by Edgar on 2018/7/27.
 */
 object IntentInject {

    @JvmStatic fun inject(o: Any, activity: Activity) {
        val objectClazz = o::class.java
        val injectClazz = Class.forName("${objectClazz.canonicalName}_inject")
        val constructor = injectClazz.getConstructor(objectClazz,Activity::class.java)
        constructor.isAccessible = true
        constructor.newInstance(o,activity)
    }
}