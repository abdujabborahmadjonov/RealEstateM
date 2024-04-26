package uz.sultonbek1547.hackathonproject2024_innovatex.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import uz.sultonbek1547.my_school_library.models.User


object MySharedPreference {
    private const val NAME = "cache_final"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var deviceToken: String?
        get() = preferences.getString("token", "")
        set(value) = preferences.edit {
            if (value != null) {
                it.putString("token", value)
            }
        }


    var isUserAuthenticated: Boolean?
        get() = preferences.getBoolean("userState", false)
        set(value) = preferences.edit {
            if (value != null) {
                it.putBoolean("userState", value)
            }
        }


    var user: User?
        get() {
            val jsonString = preferences.getString("user", null)
            return if (jsonString != null) {
                val gson = Gson()
                gson.fromJson(jsonString, User::class.java)
            } else {
                null
            }
        }
        set(value) {
            preferences.edit {
                val gson = Gson()
                val jsonString = gson.toJson(value)
                it.putString("user", jsonString)
            }
        }
}