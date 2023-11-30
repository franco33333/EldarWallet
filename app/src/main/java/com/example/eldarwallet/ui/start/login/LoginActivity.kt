package com.example.eldarwallet.ui.start.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.ActivityLoginBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.ui.start.SignUpActivity
import com.example.eldarwallet.utils.GenericDialogFragment

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java].apply {
            loginLiveData.observe(this@LoginActivity) {
                /*if (it == true) {
                    startActivity(
                        Intent(this@LoginActivity, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } else {
                    val dialog = GenericDialogFragment.createInstance(
                        getString(R.string.invalid_credentials),
                        getString(R.string.invalid_credentials_description),
                        showBtnPositive = true,
                        textBtnPositive = getString(R.string.ok)
                    )
                    dialog.onClickAccept = {
                        dialog.dismiss()
                    }
                    dialog.show(supportFragmentManager, null)
                }*/
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            onError.observe(this@LoginActivity) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.invalid_credentials),
                    getString(R.string.invalid_credentials_description),
                    showBtnPositive = true,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                }
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            if (userName.isEmpty() || password.isEmpty()) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.incomplete_fields),
                    getString(R.string.please_complete_fields),
                    showBtnPositive = true,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                }
                dialog.show(supportFragmentManager, null)
            } else {
                viewModel.login(userName, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}