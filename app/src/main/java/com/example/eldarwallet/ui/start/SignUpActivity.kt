package com.example.eldarwallet.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.User
import com.example.eldarwallet.databinding.ActivitySignUpBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val userName = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (name.isEmpty() || userName.isEmpty() || surname.isEmpty() || password.isEmpty()) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.incomplete_fields),
                description = getString(R.string.please_complete_fields),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else {
            //ALMACENAR DATOS en ROOM
            AppPreferences.setUser(
                User(name = name, surname = surname, userName = userName, password = password)
            )
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.app_name),
                description = getString(R.string.user_created),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
                startActivity(
                    Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            dialog.show(supportFragmentManager, null)
        }
    }
}