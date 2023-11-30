package com.example.eldarwallet.ui.start.signUp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.ActivitySignUpBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java].apply {
            signUpLiveData.observe(this@SignUpActivity) {
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
                        Intent(this@SignUpActivity, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
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
            viewModel.signUp(name, surname, userName, password)
        }
    }
}