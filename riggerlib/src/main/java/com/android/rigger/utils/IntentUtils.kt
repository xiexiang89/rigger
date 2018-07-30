package com.android.rigger.utils

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import java.io.Serializable

/**
 * Created by Edgar on 2018/7/30.
 */
object IntentUtils {

    @JvmStatic
    private val TAG: String = "IntentUtils"

    @JvmStatic fun getString(intent: Intent,name: String,default: String = ""): String {
        return try {
            intent.extras.getString(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getString fail.",e)
            default
        }
    }

    @JvmStatic fun getStringArray(intent: Intent,name: String): Array<String>? {
        return try {
            intent.extras.getStringArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getStringArray fail.",e)
            null
        }
    }

    @JvmStatic fun getStringArrayList(intent: Intent,name: String): ArrayList<String>? {
        return try {
            intent.extras.getStringArrayList(name)
        } catch (e: Exception) {
            Log.e(TAG,"getStringArrayList fail.",e)
            null
        }
    }

    @JvmStatic fun getInt(intent: Intent,name: String,default: Int = 0): Int {
        return try {
            intent.getIntExtra(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getInt fail.",e)
            default
        }
    }

    @JvmStatic fun getIntArray(intent: Intent,name: String): IntArray? {
        return try {
            intent.extras.getIntArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getIntArray fail.",e)
            null
        }
    }

    @JvmStatic fun getIntArrayList(intent: Intent,name: String): ArrayList<Int>? {
        return try {
            intent.extras.getIntegerArrayList(name)
        } catch (e: Exception) {
            Log.e(TAG,"getIntArrayList fail.",e)
            null
        }
    }

    @JvmStatic fun getByte(intent: Intent,name: String,default: Byte = 0x0): Byte {
        return try {
            intent.extras.getByte(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getByte fail.",e)
            default
        }
    }

    @JvmStatic fun getByteArray(intent: Intent,name: String): ByteArray? {
        return try {
            intent.extras.getByteArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getByteArray fail.",e)
            null
        }
    }

    @JvmStatic fun getShort(intent: Intent,name: String,default: Short = 0): Short {
        return try {
            intent.extras.getShort(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getShort fail.",e)
            default
        }
    }

    @JvmStatic fun getShortArray(intent: Intent,name: String): ShortArray? {
        return try {
            intent.extras.getShortArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getShortArray fail.",e)
            null
        }
    }

    @JvmStatic fun getLong(intent: Intent,name: String,default: Long = 0): Long {
        return try {
            intent.extras.getLong(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getLong fail.",e)
            default
        }
    }

    @JvmStatic fun getLongArray(intent: Intent,name: String): LongArray? {
        return try {
            intent.extras.getLongArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getLongArray fail.",e)
            null
        }
    }

    @JvmStatic fun getFloat(intent: Intent,name: String,default: Float = 0.0f): Float {
        return try {
            intent.extras.getFloat(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getFloat fail.",e)
            default
        }
    }

    @JvmStatic fun getFloatArray(intent: Intent,name: String): FloatArray? {
        return try {
            intent.extras.getFloatArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getFloatArray",e)
            null
        }
    }

    @JvmStatic fun getDouble(intent: Intent,name: String,default: Double = 0.0): Double {
        return try {
            intent.extras.getDouble(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getDouble fail.",e)
            default
        }
    }

    @JvmStatic fun getDoubleArray(intent: Intent,name: String): DoubleArray? {
        return try {
            intent.extras.getDoubleArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getDoubleArray",e)
            null
        }
    }

    @JvmStatic fun getBoolean(intent: Intent,name: String,default: Boolean = false): Boolean {
        return try {
            intent.extras.getBoolean(name,default)
        } catch (e: Exception) {
            Log.e(TAG,"getBoolean fail",e)
            default
        }
    }

    @JvmStatic fun getBooleanArray(intent: Intent,name: String): BooleanArray? {
        return try {
            intent.extras.getBooleanArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getBooleanArray fail",e)
            null
        }
    }

    @JvmStatic fun <T : Parcelable> getParcelable(intent: Intent,name: String): T? {
        return try {
            intent.extras.getParcelable<T>(name)
        } catch (e: Exception) {
            Log.e(TAG,"getParcelable fail.",e)
            null
        }
    }

    @JvmStatic fun <T : Parcelable> getParcelableArray(intent: Intent,name: String): Array<Parcelable>? {
        return try {
            intent.extras.getParcelableArray(name)
        } catch (e: Exception) {
            Log.e(TAG,"getParcelableArray fail.",e)
            null
        }
    }

    @JvmStatic fun <T : Parcelable> getParcelableArrayList(intent: Intent,name: String): ArrayList<T>? {
        return try {
            intent.extras.getParcelableArrayList(name)
        } catch (e: Exception) {
            Log.e(TAG,"getParcelableArrayList fail.",e)
            null
        }
    }

    @JvmStatic fun <T : Serializable> getSerializable(intent: Intent,name: String): T? {
        return try {
            intent.extras.getSerializable(name) as T
        } catch (e: Exception) {
            Log.e(TAG,"getSerializable fail.",e)
            null
        }
    }

    @JvmStatic fun  getCharSequence(intent: Intent,name: String,default: CharSequence = ""): CharSequence{
        return try {
            intent.extras.getCharSequence(name,default)
        } catch (e: Exception) {
            Log.d(TAG,"getCharSequence fail",e)
            default
        }
    }

    @JvmStatic fun getCharSequenceArray(intent: Intent,name: String): Array<CharSequence>? {
        return try {
            intent.extras.getCharSequenceArray(name)
        } catch (e: Exception) {
            Log.d(TAG,"getCharSequence fail",e)
            null
        }
    }

    @JvmStatic fun getCharSequenceArrayList(intent: Intent,name: String): ArrayList<CharSequence>? {
        return try {
            intent.extras.getCharSequenceArrayList(name)
        } catch (e: Exception) {
            Log.d(TAG,"getCharSequence fail",e)
            null
        }
    }

    @JvmStatic fun getChar(intent: Intent,name: String,default: Char = 0.toChar()): Char {
        return try {
            intent.extras.getChar(name,default)
        } catch (e: Exception) {
            Log.d(TAG,"getChar fail",e)
            default
        }
    }

    @JvmStatic fun getCharArray(intent: Intent,name: String): CharArray? {
        return try {
            intent.extras.getCharArray(name)
        } catch (e: Exception) {
            Log.d(TAG,"getCharArray fail",e)
            null
        }
    }
}