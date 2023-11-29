package com.example.eldarwallet.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.adapter.CardsAdapter
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityMainBinding
import com.example.eldarwallet.ui.main.newCard.NewCardActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val user by lazy { AppPreferences.getUser()!! }

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

        val number = user.balance
        val COUNTRY = "AR"
        val LANGUAGE = "es"
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        binding.tvBalance.text = str

        val list = user.cards
        if (!list.isNullOrEmpty()) {
            var adapter = CardsAdapter(list, this)
            binding.rvCards.adapter = adapter
        }
    }
}