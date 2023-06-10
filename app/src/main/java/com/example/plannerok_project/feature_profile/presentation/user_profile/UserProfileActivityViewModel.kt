package com.example.plannerok_project.feature_profile.presentation.user_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannerok_project.core.local_data.LocalData
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


// #TODO: Не допускать повторных API коллов при возвращении в профиль, если уже закешированы данные.

@HiltViewModel
class UserProfileActivityViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val localData: LocalData,
) : ViewModel() {

    private lateinit var accessToken: String
    private var isDataFetched = false

    private val _city = MutableLiveData<String?>()
    val city: LiveData<String?>
        get() = _city

    private val _birthday = MutableLiveData<String?>()
    val birthday: LiveData<String?>
        get() = _birthday

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _phoneNumber = MutableLiveData<String?>()
    val phoneNumber: LiveData<String?>
        get() = _phoneNumber

    private val _bio = MutableLiveData<String?>()
    val bio: LiveData<String?>
        get() = _bio

   private val _avatar = MutableLiveData<String?>()
    val avatar: LiveData<String?>
        get() = _avatar

   private val _avatarTest = MutableLiveData<String?>()
    val avatarTest: LiveData<String?>
        get() = _avatarTest


// Кэширует данные.
    fun getUserProfileData() {
        if (isDataFetched) {
            fetchUserData()
            isDataFetched = true
        } else {
            getCurrentUser()
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            val userDataMap = localData.retrieveUserData(
                LocalData.CITY,
                LocalData.BIRTHDAY,
                LocalData.USERNAME,
                LocalData.PHONE,
                LocalData.BIO,
                LocalData.AVATAR,
                LocalData.AVATAR_TEST
            )

            val city = userDataMap[LocalData.CITY]
            val birthday = userDataMap[LocalData.BIRTHDAY]
            val username = userDataMap[LocalData.USERNAME]
            val phoneNumber = userDataMap[LocalData.PHONE]
            val bio = userDataMap[LocalData.BIO]
            val avatar = userDataMap[LocalData.AVATAR]
            val avatarTest = userDataMap[LocalData.AVATAR_TEST]


            Log.d(TAG, "PHONE NUMBER: $phoneNumber")

            _city.value = city
            _birthday.value = birthday
            _username.value = username
            _phoneNumber.value = phoneNumber
            _bio.value = bio
            _avatar.value = avatar
            _avatarTest.value = avatarTest
        }
    }


    fun getCurrentUser() {
        viewModelScope.launch {
            val userDataMap = localData.retrieveUserData(LocalData.ACCESS_TOKEN)
            accessToken = userDataMap[LocalData.ACCESS_TOKEN] ?: ""

            val response = repository.getCurrentUser(accessToken)
            if (response is Resource.Success) {
                val dataPairs = arrayOf(
                    // #TODO: Может быть заменить всё это безобразие на привязку респонса к энтити, и уже из энтити обновлять префы.
                    LocalData.AVATAR to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.avatar),
                    LocalData.BIRTHDAY to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.birthday),
                    LocalData.CITY to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.city),
                    LocalData.COMPLETED_TASK to LocalData.PreferenceValue.IntValue(response.data?.profile_data?.completed_task),
                    LocalData.CREATED to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.created),
                    LocalData.USER_ID to LocalData.PreferenceValue.IntValue(response.data?.profile_data?.id),
                    LocalData.INSTAGRAM to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.instagram),
                    LocalData.LAST to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.last),
                    LocalData.NAME to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.name),
                    LocalData.ONLINE to LocalData.PreferenceValue.BooleanValue(response.data?.profile_data?.online),
                    LocalData.PHONE to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.phone),
                    LocalData.STATUS to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.status),
                    LocalData.USERNAME to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.username),
                    LocalData.VK to LocalData.PreferenceValue.StringValue(response.data?.profile_data?.vk),
                )
                localData.updateUserData(*dataPairs)
                fetchUserData()
            }
        }
    }

    fun decodeBase64IntoBitmap(base64String: String?): Bitmap? {
        if (base64String == null || base64String.isEmpty()) {
            return null
        }

        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

}