package com.example.plannerok_project.feature_auth.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannerok_project.core.LocalData.LocalData
import com.example.plannerok_project.core.LocalData.LocalData.Companion.PHONE
import com.example.plannerok_project.core.utils.Resource
import com.example.plannerok_project.feature_auth.domain.model.request.UserRegisterRequest
import com.example.plannerok_project.feature_auth.domain.repository.AuthRepository
import com.example.plannerok_project.feature_profile.presentation.user_profile.UserProfileActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterActivityViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val localData: LocalData
) : ViewModel() {

    private val _myData = MutableLiveData<String>()
    val myData: LiveData<String> = _myData

    private var listener: RegisterViewModelListener? = null


    fun fetchNumber() {
        viewModelScope.launch {
            val result = localData.retrieveUserData(PHONE)
            _myData.postValue(result[PHONE])
        }
    }

    fun userRegister(request: UserRegisterRequest) {
        viewModelScope.launch {
            val response = repository.userRegister(request)
            if (response is Resource.Success) {
                val dataPairs = arrayOf(
                    LocalData.REFRESH_TOKEN to LocalData.PreferenceValue.StringValue(response.data!!.refresh_token),
                    LocalData.ACCESS_TOKEN to LocalData.PreferenceValue.StringValue(response.data.access_token),
                    LocalData.USER_ID to LocalData.PreferenceValue.IntValue(response.data.user_id)
                )
                localData.updateUserData(*dataPairs)
                listener?.navigateToOtherActivity(UserProfileActivity::class.java)
            }
        }
    }

    fun setListener(listener: RegisterViewModelListener) {
        this.listener = listener
    }
}

interface RegisterViewModelListener {
    fun navigateToOtherActivity(activityClass: Class<*>)
}