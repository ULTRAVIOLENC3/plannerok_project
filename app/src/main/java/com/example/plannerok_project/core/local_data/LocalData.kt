package com.example.plannerok_project.core.local_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocalData(private val context: Context) {

    companion object {
        val IS_CACHED = booleanPreferencesKey("isCached")

        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")

        val PHONE = stringPreferencesKey("phone")
        val AVATAR = stringPreferencesKey("avatar")
        val BIG_AVATAR = stringPreferencesKey("bigAvatar")
        val MINI_AVATAR = stringPreferencesKey("miniAvatar")
        val BIRTHDAY = stringPreferencesKey("birthday")
        val CITY = stringPreferencesKey("city")
        val COMPLETED_TASK = intPreferencesKey("completedTask")
        val CREATED = stringPreferencesKey("created")
        val USER_ID = intPreferencesKey("id")
        val INSTAGRAM = stringPreferencesKey("instagram")
        val LAST = stringPreferencesKey("last")
        val NAME = stringPreferencesKey("name")
        val ONLINE = booleanPreferencesKey("online")
        val STATUS = stringPreferencesKey("status")
        val USERNAME = stringPreferencesKey("username")
        val VK = stringPreferencesKey("vk")
        val BIO = stringPreferencesKey("bio")
    }

    sealed class PreferenceValue<out T> {
        data class StringValue(val value: String?) : PreferenceValue<String>()
        data class IntValue(val value: Int?) : PreferenceValue<Int>()
        data class BooleanValue(val value: Boolean?) : PreferenceValue<Boolean>()
    }

    suspend fun updateUserData(vararg keyValuePairs: Pair<Preferences.Key<*>, PreferenceValue<*>>) {
        context.dataStore.edit { preferences ->
            for ((key, value) in keyValuePairs) {
                when (value) {
                    is PreferenceValue.StringValue -> preferences[key as Preferences.Key<String>] = value.value ?: ""
                    is PreferenceValue.IntValue -> preferences[key as Preferences.Key<Int>] = value.value ?: 0
                    is PreferenceValue.BooleanValue -> preferences[key as Preferences.Key<Boolean>] = value.value ?: false
                }
            }
        }
    }


    suspend fun <T> retrieveUserData(vararg keys: Preferences.Key<T>): Map<Preferences.Key<T>, T?> {
        val preferences = context.dataStore.data.first()
        val result = mutableMapOf<Preferences.Key<T>, T?>()
        for (key in keys) {
            result[key] = preferences[key]
        }
        return result
    }


    suspend fun retrieveAccessToken(): String? {
        val preferencesMap = retrieveUserData(ACCESS_TOKEN)
        val accessToken: String? = preferencesMap[ACCESS_TOKEN]
        return accessToken
    }



}