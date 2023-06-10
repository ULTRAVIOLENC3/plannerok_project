package com.example.plannerok_project.feature_profile.presentation.edit_user_profile

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannerok_project.core.local_data.LocalData
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.feature_profile.domain.model.request.Avatar
import com.example.plannerok_project.feature_profile.domain.model.request.UpdateUserRequest
import com.example.plannerok_project.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

// #TODO: Засунуть в эмулятор картинку и проверить функционал загрузки аватарки.

@HiltViewModel
class EditUserProfileActivityViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val localData: LocalData,
) : ViewModel() {


    private lateinit var accessToken: String

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

    private val _userBio = MutableLiveData<String?>()
    val userBio: LiveData<String?>
        get() = _userBio

    private val _avatar = MutableLiveData<String?>()
    val avatar: LiveData<String?>
        get() = _avatar

    private val _instagram = MutableLiveData<String?>()
    val instagram: LiveData<String?>
        get() = _instagram

    private val _vk = MutableLiveData<String?>()
    val vk: LiveData<String?>
        get() = _vk

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?>
        get() = _status

    private val _avatarTest = MutableLiveData<String?>()
    val avatarTest: LiveData<String?>
        get() = _avatarTest


    var processedImage: String? = ""
    var selectedImageName: String? = ""
    var avatarForTest: String? = ""


    fun fetchUserData() {
        viewModelScope.launch {
            val userDataMap = localData.retrieveUserData(
                LocalData.CITY,
                LocalData.BIRTHDAY,
                LocalData.USERNAME,
                LocalData.PHONE,
                LocalData.BIO,
                LocalData.AVATAR,
                LocalData.AVATAR_TEST,

                LocalData.INSTAGRAM,
                LocalData.VK,
                LocalData.STATUS
            )

            val city = userDataMap[LocalData.CITY]
            val birthday = userDataMap[LocalData.BIRTHDAY]
            val username = userDataMap[LocalData.USERNAME]
            val phoneNumber = userDataMap[LocalData.PHONE]
            val userBio = userDataMap[LocalData.BIO]
            val avatar = userDataMap[LocalData.AVATAR]
            val avatarTest = userDataMap[LocalData.AVATAR_TEST]

            val instagram = userDataMap[LocalData.INSTAGRAM]
            val vk = userDataMap[LocalData.VK]
            val status = userDataMap[LocalData.STATUS]

            // Data for layout or/and request
            _city.value = city
            _birthday.value = birthday
            _username.value = username
            _phoneNumber.value = phoneNumber
            _userBio.value = userBio
            _avatar.value = avatar
            _avatarTest.value = avatarTest

            // Data for request only
            _instagram.value = instagram
            _vk.value = vk
            _status.value = status
        }
    }


    fun editUserData() {
        viewModelScope.launch {
            val dataPairs = arrayOf(
                LocalData.BIRTHDAY to LocalData.PreferenceValue.StringValue(_birthday.value),
                LocalData.CITY to LocalData.PreferenceValue.StringValue(_city.value),
                LocalData.BIO to LocalData.PreferenceValue.StringValue(_userBio.value),
            )
            localData.updateUserData(*dataPairs)
            fetchUserData()

            val userDataMap = localData.retrieveUserData(LocalData.ACCESS_TOKEN)
            accessToken = userDataMap[LocalData.ACCESS_TOKEN] ?: ""

            val avatar = Avatar(
                base_64 = processedImage,
                filename = selectedImageName,
            )
            val request = UpdateUserRequest(
                avatar = avatar,
                birthday = birthday.value,
                city = city.value,
                instagram = instagram.value,
                name = username.value,
                status = status.value,
                username = username.value,
                vk = status.value
            )
            val response = repository.updateUser(accessToken, request)
            if (response is Resource.Success) {
                val dataPairs = arrayOf(
                    LocalData.AVATAR to LocalData.PreferenceValue.StringValue(response.data?.avatars?.avatar),
                    LocalData.BIG_AVATAR to LocalData.PreferenceValue.StringValue(response.data?.avatars?.bigAvatar),
                    LocalData.MINI_AVATAR to LocalData.PreferenceValue.StringValue(response.data?.avatars?.miniAvatar),
                )
                localData.updateUserData(*dataPairs)
                val avatarTest =
                    LocalData.AVATAR_TEST to LocalData.PreferenceValue.StringValue(avatarForTest)
                localData.updateUserData(avatarTest)
            }
        }
    }

    fun updateViewModelData(city: String?, birthday: String?, bio: String?){
        _city.value = city
        _birthday.value = birthday
        _userBio.value = bio
    }


    fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(displayNameColumnIndex)
            }
        }
        cursor?.close()
        return fileName
    }

    fun encodeBitmapIntoBase64(bitmap: Bitmap?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun encodeImageIntoBitmap(imageUri: Uri, contentResolver: ContentResolver): Bitmap? {
        return try {
            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun decodeBase64IntoBitmap(base64String: String?): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

}