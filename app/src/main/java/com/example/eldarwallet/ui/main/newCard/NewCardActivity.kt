package com.example.eldarwallet.ui.main.newCard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.databinding.ActivityNewCardBinding
import com.example.eldarwallet.ui.main.MainActivity
import com.example.eldarwallet.utils.GenericDialogFragment
import com.example.eldarwallet.utils.getCardLogo
import com.example.eldarwallet.utils.gone
import com.example.eldarwallet.utils.visible
import java.util.Calendar

class NewCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewCardBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)[NewCardViewModel::class.java].apply {
            cardCreatedLiveData.observe(this@NewCardActivity) {
                val dialog = GenericDialogFragment.createInstance(
                    title = getString(R.string.app_name),
                    description = getString(R.string.card_added),
                    showBtnPositive = true,
                    showBtnNegative = false,
                    textBtnPositive = getString(R.string.ok)
                )
                dialog.onClickAccept = {
                    dialog.dismiss()
                    startActivity(
                        Intent(this@NewCardActivity, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                dialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.containerToolbar.toolbar.title = getString(R.string.add_card)
        binding.containerToolbar.toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(binding.containerToolbar.toolbar)
        binding.containerToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.etExpirationDate.addTextChangedListener(object : TextWatcher {
            var isEditing = false
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null || s.toString() == "MM/YY") return

                isEditing = true
                val formattedText = formatAsDate(s.toString())
                binding.etExpirationDate.setText(formattedText)
                binding.etExpirationDate.setSelection(formattedText.length)
                isEditing = false
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            var isEditing = false
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return

                isEditing = true
                if (s.length == 1) {
                    val cardLogo = getCardLogo(s.toString())
                    if (cardLogo != null) {
                        Glide.with(this@NewCardActivity).load(
                            AppCompatResources.getDrawable(
                                this@NewCardActivity,
                                cardLogo
                                )).into(binding.ivCardLogo)
                        binding.ivCardLogo.visible()
                    }
                } else if (s.isEmpty()){
                    binding.ivCardLogo.gone()
                } else {
                    val formattedText = formatAsCardNumber(s.toString())
                    binding.etCardNumber.setText(formattedText)
                    binding.etCardNumber.setSelection(formattedText.length)
                }
                isEditing = false
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnAddCard.setOnClickListener {
            validateFields()
        }
    }

    private fun formatAsCardNumber(text: String): String {
        val numbersOnly = text.replace("[^0-9]".toRegex(), "")
        val formattedText = StringBuilder(numbersOnly)

        val firstNumber = text.first().digitToInt()
        if (firstNumber != 3) {
            if (numbersOnly.length >= 5) {
                formattedText.insert(4, " ")
                if(numbersOnly.length >= 9) {
                    formattedText.insert(9, " ")
                    if(numbersOnly.length >= 13) {
                        formattedText.insert(14, " ")
                    }
                }
            }
        } else {
            if (numbersOnly.length >= 5) {
                formattedText.insert(4, " ")
                if(numbersOnly.length >= 11) {
                    formattedText.insert(11, " ")
                }
            }
        }

        return formattedText.toString()
    }

    private fun validateFields() {
        val cardNumber = binding.etCardNumber.text.toString().trim()
        val cardName = binding.etCardName.text.toString().trim()
        val expirationDate = binding.etExpirationDate.text.toString().trim()
        val securityCode = binding.etSecurityCode.text.toString().trim()
        val document = binding.etCardDocument.text.toString().trim()

        val user = AppPreferences.getUser()!!

        if (cardNumber.isEmpty() || cardName.isEmpty() || expirationDate.isEmpty() || securityCode.isEmpty() || document.isEmpty()) {
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
        } else if (!validateCardNumber(cardNumber)) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.card_number_invalid),
                description = getString(R.string.please_fix_card_number),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else if (!cardName.contains(user.name!!, true) ||
            !cardName.contains(user.surname!!, true)) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.card_name_invalid),
                description = getString(R.string.please_fix_card_name),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else if (!isExpirationDateValid(expirationDate)) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.expiration_date_invalid),
                description = getString(R.string.please_fix_expiration_date),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else if (securityCode.length<3) {
            val dialog = GenericDialogFragment.createInstance(
                title = getString(R.string.security_code_invalid),
                description = getString(R.string.please_fix_security_number),
                showBtnPositive = true,
                showBtnNegative = false,
                textBtnPositive = getString(R.string.ok)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(supportFragmentManager, null)
        } else {
            viewModel.addCard(cardNumber, cardName, expirationDate, securityCode, document)
        }
    }

    private fun isExpirationDateValid(expirationDate: String): Boolean {
        if (expirationDate.length!=5)
            return false
        val month = expirationDate.substring(0..1)
        val year = expirationDate.substring(3..4)
        if (month.toInt() > 12 || month.toInt() < 1 || year.toInt() > 99 || year.toInt() < 23)
            return false
        if (year.toInt() < (Calendar.getInstance().get(Calendar.YEAR) % 100))
            return false
        if (year.toInt() == (Calendar.getInstance().get(Calendar.YEAR) % 100) &&
            month.toInt() < Calendar.getInstance().get(Calendar.MONTH)+1)
            return false
        return true
    }

    private fun formatAsDate(text: String): String {
        val numbersOnly = text.replace("[^0-9]".toRegex(), "")
        val formattedText = StringBuilder(numbersOnly)

        if (numbersOnly.length >= 3) {
            formattedText.insert(2, "/")
        }

        return formattedText.toString()
    }

    private fun validateCardNumber(cardNumber: String): Boolean {
        val firstNumber = cardNumber.first().digitToInt()
        if (firstNumber < 3 || firstNumber > 5)
            return false
        if (firstNumber == 3) {
            if (cardNumber.length < 17 || cardNumber.length > 17)
                return false
        }
        return true
    }
}