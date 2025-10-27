package com.example.inventure

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.authDataStore by preferencesDataStore(name = "auth")

class AuthManager(private val context: Context) {
    private val USERS_KEY = stringPreferencesKey("users")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val CURRENT_USER_EMAIL_KEY = stringPreferencesKey("current_user_email")

    val isLoggedIn = context.authDataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    suspend fun getCurrentUserEmail(): String? {
        val preferences = context.authDataStore.data.first()
        return preferences[CURRENT_USER_EMAIL_KEY]
    }

    suspend fun signUp(fullName: String, email: String, password: String): Boolean {
        val users = getUsers()

        if (users.any { it.email == email }) {
            return false
        }

        val newUser = User(fullName, email, password)
        users.add(newUser)
        saveUsers(users)
        return true
    }

    suspend fun login(email: String, password: String): Boolean {
        val users = getUsers()
        val isValid = users.any { it.email == email && it.password == password }

        if (isValid) {
            context.authDataStore.edit { preferences ->
                preferences[IS_LOGGED_IN_KEY] = true
                preferences[CURRENT_USER_EMAIL_KEY] = email
            }
        }
        return isValid
    }

    //LOGOUT FUNCTION
    suspend fun logout() {
        context.authDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = false
            preferences.remove(CURRENT_USER_EMAIL_KEY)
        }
    }

    private suspend fun getUsers(): MutableList<User> {
        val preferences = context.authDataStore.data.first()
        val usersJson = preferences[USERS_KEY] ?: return mutableListOf()

        val users = mutableListOf<User>()
        val jsonArray = JSONArray(usersJson)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            users.add(
                User(
                    fullName = obj.getString("fullName"),
                    email = obj.getString("email"),
                    password = obj.getString("password")
                )
            )
        }
        return users
    }

    private suspend fun saveUsers(users: List<User>) {
        val jsonArray = JSONArray()
        users.forEach { user ->
            val obj = JSONObject().apply {
                put("fullName", user.fullName)
                put("email", user.email)
                put("password", user.password)
            }
            jsonArray.put(obj)
        }

        context.authDataStore.edit { preferences ->
            preferences[USERS_KEY] = jsonArray.toString()
        }
    }

    data class User(
        val fullName: String,
        val email: String,
        val password: String
    )
}