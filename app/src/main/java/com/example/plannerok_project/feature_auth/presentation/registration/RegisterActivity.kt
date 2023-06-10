package com.example.plannerok_project.feature_auth.presentation.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.plannerok_project.feature_auth.domain.model.request.UserRegisterRequest
import com.example.plannerok_project.databinding.ActivityRegisterBinding
import com.example.plannerok_project.feature_auth.presentation.authorisation.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterActivity : ComponentActivity(), RegisterViewModelListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(RegisterActivityViewModel::class.java)
        viewModel.setListener(this)

        viewModel.fetchNumber()
        viewModel.myData.observe(this) { phoneNumber ->
            val editablePhoneNumber: Editable =
                Editable.Factory.getInstance().newEditable(phoneNumber)
            binding.etPhoneNumberRegister.text = editablePhoneNumber

            val focusable = phoneNumber.isNullOrEmpty()
            binding.etPhoneNumberRegister.apply {
                isFocusable = focusable
                isClickable = focusable
                isCursorVisible = focusable
            }
        }

        binding.btnRegister.setOnClickListener {
            viewModel.userRegister(UserRegisterRequest(
                binding.etRealNameRegister.text.toString(),
                binding.etPhoneNumberRegister.text.toString(),
                binding.etUsernameRegister.text.toString()
            )
            )
        }

        viewModel.validationErrorUserRegister.observe(this) { error ->
            Log.d(TAG, error)
            Toast.makeText (this, error, Toast.LENGTH_SHORT).show()
        }

    }

    override fun navigateToOtherActivity(activityClass: Class<*>) {
        // Start the other activity here
        val intent = Intent(this@RegisterActivity, activityClass)
        startActivity(intent)
    }
}