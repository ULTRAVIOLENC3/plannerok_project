package com.example.plannerok_project.feature_auth.presentation.authorisation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.plannerok_project.databinding.ActivityLoginBinding
import com.example.plannerok_project.feature_auth.domain.model.request.CheckAuthCodeRequest
import com.example.plannerok_project.feature_auth.domain.model.request.SendAuthCodeRequest
import com.google.android.material.snackbar.Snackbar
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG = "LoginActivity"

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
            if (ccp.isValidFullNumber) {
            phoneNumber = ccp.fullNumberWithPlus
            viewModel.sendAuthCode(SendAuthCodeRequest(phoneNumber))
            } else {
                val error = "Неверный формат номера."
                Log.d(TAG, error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // After receiving the code, if is_user_exist == true, log in, else navigate to registration.
        binding.btnLogin.setOnClickListener {
            val code = binding.etCodeLogin.text.toString()
            viewModel.checkAuthCode(CheckAuthCodeRequest(phoneNumber, code))
        }

        viewModel.validationErrorCheckAuthCode.observe(this) { error ->
            Log.d(TAG, error)
            Toast.makeText (this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateToOtherActivity(activityClass: Class<*>) {
        val intent = Intent(this@LoginActivity, activityClass)
        startActivity(intent)
    }
}