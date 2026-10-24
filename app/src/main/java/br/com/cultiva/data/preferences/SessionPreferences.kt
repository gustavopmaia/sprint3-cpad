package br.com.cultiva.data.preferences

import android.content.Context

class SessionPreferences(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("cultiva_session", Context.MODE_PRIVATE)

    var loggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()

    var lastEmail: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    fun clear() = prefs.edit().clear().apply()

    private companion object {
        const val KEY_LOGGED_IN = "logged_in"
        const val KEY_EMAIL = "last_email"
    }
}
