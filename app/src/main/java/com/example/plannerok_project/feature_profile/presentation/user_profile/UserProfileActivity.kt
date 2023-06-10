package com.example.plannerok_project.feature_profile.presentation.user_profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.plannerok_project.databinding.ActivityUserProfileBinding
import com.example.plannerok_project.feature_auth.presentation.authorisation.TAG
import com.example.plannerok_project.feature_profile.presentation.edit_user_profile.EditUserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

const val TAG = "UserProfileActivity"

@AndroidEntryPoint
class UserProfileActivity : ComponentActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserProfileActivityViewModel
    private var birthdayValue: String? = "01-01-0000"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)

        viewModel.getCurrentUser()

        viewModel.avatar.observe(this) { avatar ->
            Glide.with(this)
                .load(avatar)
                .into(binding.civUserPfp)
        }

        viewModel.city.observe(this) { city ->
            updateEditText(binding.etUserCityProfile, city)
        }
        viewModel.birthday.observe(this) { birthday ->
            birthdayValue = birthday
            updateEditText(binding.etUserBirthDateProfile, birthday)
            updateEditText(binding.etUserZodiacSignProfile, getZodiacSign(birthdayValue))
        }

        viewModel.username.observe(this) { username ->
            updateEditText(binding.etUsernameProfile, username)
        }

        viewModel.phoneNumber.observe(this) { phoneNumber ->
            updateEditText(binding.etPhoneNumberProfile, phoneNumber)
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this@UserProfileActivity, EditUserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    // Makes certain EditTextViews uneditable.
    private fun updateEditText(
        editText: EditText,
        value: String?,
        defaultText: String = "",
        focusable: Boolean = true
    ) {
        val editableValue: Editable = Editable.Factory.getInstance().newEditable(value ?: defaultText)
        editText.text = editableValue
        editText.apply {
            isFocusable = focusable
            isClickable = focusable
            isCursorVisible = focusable
        }
    }

    fun getZodiacSign(birthDate: String? = "01-01-0000"): String {
        val defaultZodiacSign = "Unknown"

        // Check if birthDate is null or empty
        if (birthDate.isNullOrEmpty()) {
            return defaultZodiacSign
        }

        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = dateFormat.parse(birthDate)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            return when (month) {
                1 -> if (day < 20) "Козерог" else "Водолей"
                2 -> if (day < 19) "Водолей" else "Рыбы"
                3 -> if (day < 21) "Рыбы" else "Овен"
                4 -> if (day < 20) "Овен" else "Телец"
                5 -> if (day < 21) "Телец" else "Близнецы"
                6 -> if (day < 21) "Близнецы" else "Рак"
                7 -> if (day < 23) "Рак" else "Лев"
                8 -> if (day < 23) "Лев" else "Дева"
                9 -> if (day < 23) "Дева" else "Весы"
                10 -> if (day < 23) "Весы" else "Скорпион"
                11 -> if (day < 22) "Скорпион" else "Стрелец"
                else -> if (day < 22) "Стрелец" else "Козерог"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return defaultZodiacSign
        }
    }

}