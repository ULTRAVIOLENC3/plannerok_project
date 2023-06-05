package com.example.plannerok_project.feature_auth.presentation.authorisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannerok_project.core.LocalData.LocalData
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


    fun sendAuthCode(request: SendAuthCodeRequest) {
        viewModelScope.launch{
            val response = repository.sendAuthCode(request)
            val phoneNumber = LocalData.PHONE to LocalData.PreferenceValue.StringValue(request.phone)
            localData.updateUserData(phoneNumber)
        }
    }

    fun checkAuthCode(request: CheckAuthCodeRequest) {
        viewModelScope.launch{
            val response = repository.checkAuthCode(request)
            if (response is Resource.Success && response.data?.is_user_exists == true && request.code == TEST_SMS_CODE){
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

    fun setListener(listener: LoginViewModelListener) {
        this@LoginActivityViewModel.listener = listener
    }

}

interface LoginViewModelListener {
    fun navigateToOtherActivity(activityClass: Class<*>)
}