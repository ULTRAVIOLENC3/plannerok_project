package com.example.plannerok_project.feature_profile.presentation.edit_user_profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.plannerok_project.databinding.ActivityEditUserProfileBinding
import com.example.plannerok_project.feature_profile.presentation.user_profile.UserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

// #TODO: Добавить функционал выбора камеры.

@AndroidEntryPoint
class EditUserProfileActivity : ComponentActivity() {
    private lateinit var binding: ActivityEditUserProfileBinding
    lateinit var viewModel: EditUserProfileActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(EditUserProfileActivityViewModel::class.java)

        viewModel.fetchUserData()


        viewModel.avatar.observe(this) { avatar ->
            if (File(avatar).exists() && File(avatar).isFile()) {
                Glide.with(binding.civEditUserPfp)
                    .load(loadBase64ImageIntoImageView(avatar, binding.civEditUserPfp))
                    .into(binding.civEditUserPfp)
            }
        }


        viewModel.city.observe(this) { city ->
            updateEditText(binding.etUserCityEditProfile, city)
        }

        viewModel.birthday.observe(this) { birthday ->
            updateEditText(binding.etUserBirthDateEditProfile, birthday)
        }

        viewModel.username.observe(this) { username ->
            updateEditText(binding.etUsernameEditProfile, username)
        }

        viewModel.phoneNumber.observe(this) { phoneNumber ->
            updateEditText(binding.etPhoneNumberEditProfile, phoneNumber)
        }

        viewModel.userBio.observe(this) { userBio ->
            updateEditText(binding.etUserBioEditProfile, userBio)
        }

        binding.btnSaveEditProfile.setOnClickListener {
            viewModel.updateViewModelData(
            city = binding.etUserCityEditProfile.text.toString(),
            birthday = binding.etUserBirthDateEditProfile.text.toString(),
            bio = binding.etUserBioEditProfile.text.toString(),
            )
            viewModel.editUserData()
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        binding.civEditUserPfp.setOnClickListener {
            getImage.launch("image/*")
        }
    }



//    private fun showImageSelectionDialog() {
//        val dialog = AlertDialog.Builder(this)
//            .setTitle("Select Image")
//            .setItems(arrayOf("Gallery", "Camera")) { _, which ->
//                when (which) {
//                    0 -> openGallery()
//                    1 -> openCamera()
//                }
//            }
//            .setNegativeButton("Cancel", null)
//            .create()
//
//        dialog.show()
//    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri: Uri? ->
        if (imageUri != null) {
            val fileName = viewModel.getFileNameFromUri(contentResolver, imageUri)
            val image = viewModel.processImage(imageUri, contentResolver)
            viewModel.processedImage = image
            viewModel.selectedImageName = fileName
        }
    }

    // Change view based on the value param.
    private fun updateEditText(
        editText: EditText,
        value: String?,
        defaultText: String = "",
        focusable: Boolean = true
    ) {
        val editableValue: Editable =
            Editable.Factory.getInstance().newEditable(value ?: defaultText)
        editText.text = editableValue
    }


    fun loadBase64ImageIntoImageView(base64String: String?, imageView: ImageView): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}