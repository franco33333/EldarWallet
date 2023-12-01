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
import com.example.eldarwallet.utils.GenericDialogFragment
import com.example.eldarwallet.utils.gone
import com.example.eldarwallet.utils.visible
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val user by lazy { AppPreferences.getUser()!! }
    private val viewModel by lazy {
        ViewModelProvider(this)[DecodeViewModel::class.java].apply {
            cardsDecryptedLiveData.observe(this@MainActivity) {
                binding.progressBar.gone()
                var adapter = CardsAdapter(it, this@MainActivity)
                binding.rvCards.adapter = adapter
            }
            onError.observe(this@MainActivity) {
                binding.progressBar.gone()
            }
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
            if (user.cards.isNullOrEmpty()) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.title_no_card),
                    getString(R.string.subtitle_no_card),
                    showBtnPositive = true,
                    showBtnNegative = true,
                    textBtnPositive = getString(R.string.yes),
                    textBtnNegative = getString(R.string.no)
                )
                dialog.onClickAccept = {
                    startActivity(Intent(this@MainActivity, NewCardActivity::class.java))
                    dialog.dismiss()
                }
                dialog.onClickCancel = {
                    dialog.dismiss()
                }
                dialog.show(supportFragmentManager, "")
            } else {
                startActivity(Intent(this, PayWithCardActivity::class.java))
            }
        }

        val number = user.balance
        val COUNTRY = "AR"
        val LANGUAGE = "es"
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        binding.tvBalance.text = str
    }

    override fun onResume() {
        super.onResume()

        val list = user.cards
        if (!list.isNullOrEmpty()) {
            binding.progressBar.visible()
            viewModel.decryptCardData(list)
        }
    }
}