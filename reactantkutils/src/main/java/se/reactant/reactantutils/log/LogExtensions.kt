package Log

import Gson.gson
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import se.reactant.reactantutils.BuildConfig

/**
 * Extensions for the Android util Log class
 * Created by Jesper Sjöquist on 2017-04-01.
 */

const val UNKNOWN_CLASS_TAG = "UNKNOWN CLASS"
const val JSON_INDENTATION = 2
private var cachedCallingClassIndex = -1

fun i(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.i(getCaller() + tag, message, throwable)
}

fun d(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.d(getCaller() + tag, message, throwable)
}

fun e(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.e(getCaller() + tag, message, throwable)
}

fun v(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.v(getCaller() + tag, message, throwable)
}

fun w(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.w(getCaller() + tag, message, throwable)
}

fun wtf(message: String? = "", throwable: Throwable? = null, tag: String? = ""): Unit {
    if (BuildConfig.DEBUG) Log.wtf(getCaller() + tag, message, throwable)
}

fun <T>json(obj: T, throwable: Throwable? = null, tag: String? = ""): Unit {
    val json = if (obj is String) obj.trim() else gson.toJson(obj)

    try {
        when (json?.first()) {
            '{' -> Log.d(getCaller() + tag, JSONObject(json).toString(JSON_INDENTATION), throwable)
            '[' -> Log.d(getCaller() + tag, JSONArray(json).toString(JSON_INDENTATION), throwable)
            else -> Log.e(getCaller() + tag, "Invalid Json", throwable)
        }
    } catch (e: JSONException) {
        Log.e(getCaller() + tag, "Invalid Json", e)
    }
}

/**
 * Attempt to find the name of the class who is calling Log and create a
 * tag which the IDE can link to the position of the call
 */
private fun getCaller(): String {
    val stackTrace = Thread.currentThread().stackTrace

    // Find the last instance of the Log class in the current stack trace
    // and look for the calling Class in the next position
    if (cachedCallingClassIndex == -1)
        cachedCallingClassIndex = stackTrace.indexOfLast { it.className == "Log.LogExtensionsKt" } + 1

    // Pull the calling class from the stack trace
    if (cachedCallingClassIndex != -1 && stackTrace.size >= cachedCallingClassIndex) {
        val callingClass = stackTrace[cachedCallingClassIndex]

        if (callingClass.className.split("").isNotEmpty()) {
            return "(" + callingClass.fileName + ":" + callingClass.lineNumber + ")"
        }
    }
    return UNKNOWN_CLASS_TAG
}