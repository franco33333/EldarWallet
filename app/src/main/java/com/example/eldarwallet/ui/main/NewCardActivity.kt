package com.example.eldarwallet.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.databinding.ActivityNewCardBinding

class NewCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewCardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}