package com.example.eldarwallet.ui.main.payWithCard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eldarwallet.R
import com.example.eldarwallet.adapter.CardsSelectableAdapter
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityPayWithCardBinding
import com.example.eldarwallet.ui.main.DecodeViewModel
import com.example.eldarwallet.utils.GenericDialogFragment
import com.example.eldarwallet.utils.formatAsCurrency
import com.example.eldarwallet.utils.gone
import com.example.eldarwallet.utils.visible

class PayWithCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPayWithCardBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)[DecodeViewModel::class.java].apply {
            cardsDecryptedLiveData.observe(this@PayWithCardActivity) {
                binding.progressBar.gone()
                var adapter = CardsSelectableAdapter(it, this@PayWithCardActivity)
                binding.rvCards.adapter = adapter
            }
            onError.observe(this@PayWithCardActivity) {
                binding.progressBar.gone()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().replace("[$.,]+".toRegex(), "").trim()

                val amountFormatted = formatAsCurrency(text)

                binding.etAmount.removeTextChangedListener(this)
                binding.etAmount.setText(amountFormatted)
                binding.btnPay.isEnabled = amountFormatted.isNotEmpty()
                if (amountFormatted.length <= 15)
                    binding.etAmount.setSelection(amountFormatted.length)
                binding.etAmount.addTextChangedListener(this)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnPay.setOnClickListener {
            val cardSelected = (binding.rvCards.adapter as CardsSelectableAdapter).getSelectedCard()
            if (cardSelected == null) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.app_name),
                    getString(R.string.select_card_to_continue),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                }
                dialog.show(supportFragmentManager, "")
            } else if (binding.etCbu.text.toString().isEmpty()) {
                val dialog = GenericDialogFragment.createInstance(
                    getString(R.string.incomplete_fields),
                    getString(R.string.please_complete_fields),
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                }
                dialog.show(supportFragmentManager, "")
            } else {
                startActivity(Intent(this, ScanNFCActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val list = AppPreferences.getUser()!!.cards
        if (!list.isNullOrEmpty()) {
            binding.progressBar.visible()
            viewModel.decryptCardData(list)
        }
    }
}