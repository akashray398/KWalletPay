package com.example.kwalletpay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kwalletpay.databinding.ActivityDepositBinding

class DepositActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepositBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupQuickAmounts()
        setupBankOptions()

        binding.btnAddMoney.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            if (amount.isNotEmpty() && amount.toInt() > 0) {
                Toast.makeText(this, "₹$amount added to wallet successfully!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupQuickAmounts() {
        binding.btn500.setOnClickListener { binding.etAmount.setText("500") }
        binding.btn1000.setOnClickListener { binding.etAmount.setText("1000") }
        binding.btn2000.setOnClickListener { binding.etAmount.setText("2000") }
    }

    private fun setupBankOptions() {
        binding.bankHdfc.apply {
            findViewById<android.widget.TextView>(R.id.optionTitle).text = "HDFC Bank"
            findViewById<android.widget.TextView>(R.id.optionSubtitle).text = "•••• 4321"
            findViewById<android.widget.ImageView>(R.id.optionIcon).setImageResource(R.drawable.ic_check_balance_unique)
        }

        binding.bankSbi.apply {
            findViewById<android.widget.TextView>(R.id.optionTitle).text = "State Bank of India"
            findViewById<android.widget.TextView>(R.id.optionSubtitle).text = "•••• 8899"
            findViewById<android.widget.ImageView>(R.id.optionIcon).setImageResource(R.drawable.ic_check_balance_unique)
        }
    }
}
