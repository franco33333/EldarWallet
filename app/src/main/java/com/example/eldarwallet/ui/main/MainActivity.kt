package com.example.eldarwallet.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAddCard.setOnClickListener {
            startActivity(Intent(this, NewCardActivity::class.java))
        }

        binding.btnGenerateQR.setOnClickListener {  }

        binding.btnPayQR.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        binding.btnPayCard.setOnClickListener {  }
    }
}