package com.example.plannerok_project.feature_auth.presentation.authorisation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannerok_project.core.local_data.LocalData
import com.example.plannerok_project.core.utils.Constants.Companion.CODE_LENGTH
import com.example.plannerok_project.core.utils.Constants.Companion.TEST_SMS_CODE
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.repository.AuthRepository
import com.example.plannerok_project.feature_auth.presentation.registration.RegisterActivity
import com.example.plannerok_project.feature_profile.presentation.user_profile.UserProfileActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val localData: LocalData
) : ViewModel() {

    private var listener: LoginViewModelListener? = null


    private val _validationErrorCheckAuthCode = MutableLiveData<String>()
    val validationErrorCheckAuthCode: LiveData<String> get() = _validationErrorCheckAuthCode



    fun sendAuthCode(request: SendAuthCodeRequest) {
        // Validation is not necessary here since it's being done in the activity with CCP, but I still leave it.
        if (!request.phone.isNullOrEmpty()) {
            viewModelScope.launch {
                val response = repository.sendAuthCode(request)
                val phoneNumber =
                    LocalData.PHONE to LocalData.PreferenceValue.StringValue(request.phone)
                localData.updateUserData(phoneNumber)
            }
        }
    }

    fun checkAuthCode(request: CheckAuthCodeRequest) {
        if (!request.phone.isNullOrEmpty() && !request.code.isNullOrEmpty() && request.code.length == CODE_LENGTH){
        viewModelScope.launch {
            val response = repository.checkAuthCode(request)
            if (response is Resource.Success && response.data?.is_user_exists == true && request.code == TEST_SMS_CODE) {
                val dataPairs = arrayOf(
                    LocalData.REFRESH_TOKEN to LocalData.PreferenceValue.StringValue(response.data.refresh_token),
                    LocalData.ACCESS_TOKEN to LocalData.PreferenceValue.StringValue(response.data.access_token),
                    LocalData.USER_ID to LocalData.PreferenceValue.IntValue(response.data.user_id),
                )
                localData.updateUserData(*dataPairs)
                listener?.navigateToOtherActivity(UserProfileActivity::class.java)
            } else {
                listener?.navigateToOtherActivity(RegisterActivity::class.java)
            }
        }
        }
        _validationErrorCheckAuthCode.value = "Неправильно введен номер или код СМС."
    }


    fun setListener(listener: LoginViewModelListener) {
        this@LoginActivityViewModel.listener = listener
    }

}

interface LoginViewModelListener {
    fun navigateToOtherActivity(activityClass: Class<*>)
}