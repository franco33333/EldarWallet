package com.example.eldarwallet.ui.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}