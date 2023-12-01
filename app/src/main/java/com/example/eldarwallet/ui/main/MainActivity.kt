package com.example.eldarwallet.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.adapter.CardsAdapter
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityMainBinding
import com.example.eldarwallet.ui.main.generateQR.GenerateQRActivity
import com.example.eldarwallet.ui.main.newCard.NewCardActivity
import com.example.eldarwallet.ui.main.payWithCard.PayWithCardActivity
import com.example.eldarwallet.ui.main.scanQR.ScanActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val user by lazy { AppPreferences.getUser()!! }
    private val viewModel by lazy {
        ViewModelProvider(this)[DecodeViewModel::class.java].apply {
            cardsDecryptedLiveData.observe(this@MainActivity) {
                var adapter = CardsAdapter(it, this@MainActivity)
                binding.rvCards.adapter = adapter
            }
            onError.observe(this@MainActivity) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvTitle.text = getString(R.string.welcome, "${user.name} ${user.surname}")

        binding.btnAddCard.setOnClickListener {
            startActivity(Intent(this, NewCardActivity::class.java))
        }

        binding.btnGenerateQR.setOnClickListener {
            startActivity(Intent(this, GenerateQRActivity::class.java))
        }

        binding.btnPayQR.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        binding.btnPayCard.setOnClickListener {
            startActivity(Intent(this, PayWithCardActivity::class.java))
        }

        val number = user.balance
        val COUNTRY = "AR"
        val LANGUAGE = "es"
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        binding.tvBalance.text = str

        val list = user.cards
        if (!list.isNullOrEmpty()) {
            viewModel.decryptCardData(list)
        }
    }
}