package Gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by Jesper Sjöquist on 2017-03-30.
 */
const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

val gson: Gson = GsonBuilder().setDateFormat(GSON_DATE_FORMAT).create()