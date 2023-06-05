package com.example.plannerok_project.feature_auth.presentation.authorisation

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.plannerok_project.databinding.ActivityLoginBinding
import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "MainActivity"

@AndroidEntryPoint
class LoginActivity : ComponentActivity(), LoginViewModelListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    private lateinit var ccp: CountryCodePicker
    private lateinit var editPhoneNumber: EditText
    private var phoneNumber: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        viewModel.setListener(this)


        // Country flag, code, formatting. Retrieving full number with plus.
        ccp = binding.countryCodePickerLogin
        editPhoneNumber = binding.etPhoneNumberLogin
        ccp.registerCarrierNumberEditText(editPhoneNumber)

        binding.btnSendSMSLogin.setOnClickListener {
            phoneNumber = ccp.fullNumberWithPlus
            viewModel.sendAuthCode(SendAuthCodeRequest(phoneNumber))
        }


        // После получения кода пользователем, если is_user_exist == true, авторизация, иначе регистрация.
        binding.btnLogin.setOnClickListener {
            val code = binding.etCodeLogin.text.toString()
            viewModel.checkAuthCode(CheckAuthCodeRequest(phoneNumber, code))
        }
    }

    override fun navigateToOtherActivity(activityClass: Class<*>) {
        // Start the other activity here
        val intent = Intent(this@LoginActivity, activityClass)
        startActivity(intent)
    }
}