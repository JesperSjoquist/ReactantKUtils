package Log

import Gson.gson
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Extensions for the Android util Log class
 * Created by Jesper Sjöquist on 2017-04-01.
 */

const val UNKNOWN_CLASS_TAG = "UNKNOWN CLASS"
const val JSON_INDENTATION = 2

fun i(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.i(getCaller() + tag, message, throwable)

fun d(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.d(getCaller() + tag, message, throwable)

fun e(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.e(getCaller() + tag, message, throwable)

fun v(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.v(getCaller() + tag, message, throwable)

fun w(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.w(getCaller() + tag, message, throwable)

fun wtf(message: String? = "", throwable: Throwable? = null, tag: String? = "") =
        Log.wtf(getCaller() + tag, message, throwable)

fun <T> json(obj: T, throwable: Throwable? = null, tag: String? = ""): Int {
    val json = if (obj is String) obj.trim() else gson.toJson(obj)

    try {
        when (json?.first()) {
            '{' -> return Log.d(getCaller() + tag, JSONObject(json).toString(JSON_INDENTATION), throwable)
            '[' -> return Log.d(getCaller() + tag, JSONArray(json).toString(JSON_INDENTATION), throwable)
            else -> return Log.e(getCaller() + tag, "Invalid Json", throwable)
        }
    } catch (e: JSONException) {
        return Log.e(getCaller() + tag, "Invalid Json", e)
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
    val callingClassIndex = stackTrace.indexOfLast { it.className == "Log.LogExtensionsKt" } + 1

    // Pull the calling class from the stack trace
    if (callingClassIndex > 0 && stackTrace.size >= callingClassIndex) {
        val callingClass = stackTrace[callingClassIndex]

        if (callingClass.className.split("").isNotEmpty()) {
            return "(" + callingClass.fileName + ":" + callingClass.lineNumber + ")"
        }
    }
    return UNKNOWN_CLASS_TAG
}