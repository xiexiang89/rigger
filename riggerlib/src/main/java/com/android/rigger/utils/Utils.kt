package com.android.rigger.utils

import java.util.*

object Utils {

    @JvmStatic
    fun <T> requireNonNull(obj: T?,message: String?): T {
        if (obj == null)
            throw NullPointerException(message)
        return obj
    }

    @JvmStatic
    fun <T> requireNonNull(obj: T?): T {
        return requireNonNull(obj,"")
    }

    @JvmStatic
    fun <T> requestNonNull(obj: Array<String>?): Array<String> {
        if (obj == null || obj.isEmpty())
            throw NullPointerException()
        return obj
    }

    @JvmStatic
    fun isEmpty(array: Array<out String>?): Boolean {
        return array == null || array.isEmpty()
    }

    @JvmStatic
    fun isEmpty(array: IntArray?): Boolean {
        return array == null || array.isEmpty()
    }

    //copy ArrayUtils
    @JvmStatic
    fun append(cur: Array<String>?, `val`: String,
               allowDuplicates: Boolean): Array<String>? {
        if (cur == null) {
            return arrayOf(`val`)
        }
        val N = cur.size
        if (!allowDuplicates) {
            for (i in 0 until N) {
                if (cur[i] == `val`) {
                    return cur
                }
            }
        }
        val ret = Arrays.copyOf(cur, N + 1)
        ret[N] = `val`
        return ret
    }
}
