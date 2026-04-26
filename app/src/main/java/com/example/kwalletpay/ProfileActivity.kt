package com.example.kwalletpay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kwalletpay.databinding.ActivityProfileBinding
import com.example.kwalletpay.databinding.ItemSettingsOptionBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.toolbar.setOnClickListener {
            finish()
        }

        setupOptions()
    }

    private fun setupOptions() {
        // UPI Section
        setupOptionItem(binding.optionAccountManagement, R.string.account_management, "Manage your linked bank accounts", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionTransactionHistory, R.string.transaction_history, "View all your past transactions", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionEverythingUpi, R.string.everything_upi, "UPI IDs, QR codes, and more", R.drawable.ic_nav_history)

        // Shop Section
        setupOptionItem(binding.optionOrders, R.string.orders, "Track and manage your orders", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionWishlist, R.string.wishlist, "Items you've saved for later", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionSavedAddresses, R.string.saved_addresses, "Manage your delivery locations", R.drawable.ic_nav_history)

        // Others Section
        setupOptionItem(binding.optionHelpSupport, R.string.help_support, "Get help with your queries", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionTerms, R.string.terms_conditions, "Read our terms of service", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionPrivacy, R.string.privacy_policy, "How we handle your data", R.drawable.ic_nav_history)
        setupOptionItem(binding.optionRateUs, R.string.rate_us, "Tell us what you think of the app", R.drawable.ic_nav_history)

        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out safely", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupOptionItem(itemBinding: ItemSettingsOptionBinding, titleRes: Int, subtitle: String, iconRes: Int) {
        itemBinding.optionTitle.text = getString(titleRes)
        itemBinding.optionSubtitle.text = subtitle
        itemBinding.optionIcon.setImageResource(iconRes)
        
        itemBinding.root.setOnClickListener {
            Toast.makeText(this, "Opening ${getString(titleRes)}...", Toast.LENGTH_SHORT).show()
        }
    }
}
