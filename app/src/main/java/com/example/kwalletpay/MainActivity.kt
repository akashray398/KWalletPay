package com.example.kwalletpay

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kwalletpay.databinding.ActivityMainBinding
import com.example.kwalletpay.databinding.ActivityProfileBinding
import com.example.kwalletpay.databinding.ItemSettingsOptionBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isBalanceShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContent) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupActionButtons()
        setupFinancialServices()
        setupRecyclerView()
        setupDrawer()
        setupBottomNavigation()
        startReferCardAnimations()
    }

    private fun setupDrawer() {
        // Setup Profile button to open drawer
        binding.profileCard.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Setup logic for the included profile layout inside the drawer
        val drawerBinding = binding.drawerProfile
        
        // Fix "Option Title" issue in Drawer Profile
        setupProfileOptions(drawerBinding)

        drawerBinding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        drawerBinding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out safely", Toast.LENGTH_SHORT).show()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupProfileOptions(pBinding: ActivityProfileBinding) {
        // UPI Section
        setupOptionItem(pBinding.optionAccountManagement, R.string.account_management, "Manage your linked bank accounts", R.drawable.ic_check_balance_unique)
        setupOptionItem(pBinding.optionTransactionHistory, R.string.transaction_history, "View all your past transactions", R.drawable.ic_nav_history)
        setupOptionItem(pBinding.optionEverythingUpi, R.string.everything_upi, "UPI IDs, QR codes, and more", R.drawable.ic_settings)

        // Shop Section
        setupOptionItem(pBinding.optionOrders, R.string.orders, "Track and manage your orders", R.drawable.ic_shop_unique)
        setupOptionItem(pBinding.optionWishlist, R.string.wishlist, "Items you've saved for later", R.drawable.ic_action_transfer_unique)
        setupOptionItem(pBinding.optionSavedAddresses, R.string.saved_addresses, "Manage your delivery locations", R.drawable.ic_nav_home_unique)

        // Others Section
        setupOptionItem(pBinding.optionHelpSupport, R.string.help_support, "Get help with your queries", R.drawable.ic_nav_home_unique)
        setupOptionItem(pBinding.optionTerms, R.string.terms_conditions, "Read our terms of service", R.drawable.ic_settings)
        setupOptionItem(pBinding.optionPrivacy, R.string.privacy_policy, "How we handle your data", R.drawable.ic_settings)
        setupOptionItem(pBinding.optionRateUs, R.string.rate_us, "Tell us what you think of the app", R.drawable.baseline_circle_notifications_24)
    }

    private fun setupOptionItem(itemBinding: ItemSettingsOptionBinding, titleRes: Int, subtitle: String, iconRes: Int) {
        itemBinding.optionTitle.text = getString(titleRes)
        itemBinding.optionSubtitle.text = subtitle
        itemBinding.optionIcon.setImageResource(iconRes)
        
        itemBinding.root.setOnClickListener {
            Toast.makeText(this, "Opening ${getString(titleRes)}...", Toast.LENGTH_SHORT).show()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun startReferCardAnimations() {
        // Glow Pulse & Rotate Animation
        ObjectAnimator.ofFloat(binding.referCard.findViewById(R.id.referGlow), View.ALPHA, 0.2f, 0.5f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Icon Floating Animation
        val floatAnim = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -10f, 10f)
        val rotateAnim = PropertyValuesHolder.ofFloat(View.ROTATION, -15f, -5f)

        ObjectAnimator.ofPropertyValuesHolder(binding.referCard.findViewById(R.id.referIcon), floatAnim, rotateAnim).apply {
            duration = 2500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun setupBottomNavigation() {
        binding.navHome.setOnClickListener { updateBottomNavSelection(it) }
        binding.navExplore.setOnClickListener {
            updateBottomNavSelection(it)
            startActivity(Intent(this, ShopActivity::class.java))
        }
        binding.navHistory.setOnClickListener {
            updateBottomNavSelection(it)
            Toast.makeText(this, "Transaction History coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBottomNavSelection(selected: View) {
        val navs = listOf(
            Triple(binding.navHome, binding.ivNavHome, binding.tvNavHome),
            Triple(binding.navExplore, binding.ivNavExplore, binding.tvNavExplore),
            Triple(binding.navHistory, binding.ivNavHistory, binding.tvNavHistory)
        )

        navs.forEach { (view, icon, text) ->
            val color = if (view == selected) R.color.colorPrimary else R.color.text_secondary
            icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
            text.setTextColor(ContextCompat.getColor(this, color))
        }
    }

    private fun setupActionButtons() {
        setupAction(binding.includeScanQR.root, "Scan QR", R.drawable.ic_scan_qr_unique, R.color.pro_bg_scan, R.color.pro_icon_scan)
        setupAction(binding.includeCheckBalance.root, "Check Balance", R.drawable.ic_check_balance_unique, R.color.pro_bg_balance, R.color.pro_icon_balance)
        setupAction(binding.includeShop.root, "Shop", R.drawable.ic_shop_unique, R.color.pro_bg_shop, R.color.pro_icon_shop)
        setupAction(binding.includeDeposit.root, "Deposit", R.drawable.ic_action_deposit_unique, R.color.pro_bg_deposit, R.color.pro_icon_deposit)
        setupAction(binding.includePaybill.root, "Pay Bill", R.drawable.ic_action_paybill_unique, R.color.pro_bg_paybill, R.color.pro_icon_paybill)
        setupAction(binding.includeTransfer.root, "Transfer", R.drawable.ic_action_transfer_unique, R.color.pro_bg_transfer, R.color.pro_icon_transfer)

        binding.cardCheckBalance.setOnClickListener { toggleBalance() }
        binding.cardShop.setOnClickListener { startActivity(Intent(this, ShopActivity::class.java)) }
        binding.cardDeposit.setOnClickListener { startActivity(Intent(this, DepositActivity::class.java)) }
        
        binding.referButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Join KWalletPay and get ₹201 cashback: https://kwalletpay.app/refer/akash")
            }
            startActivity(Intent.createChooser(shareIntent, "Invite via"))
        }
    }

    private fun setupFinancialServices() {
        setupAction(binding.includePersonalLoan.root, "Personal Loan", R.drawable.ic_action_deposit_unique, R.color.pro_bg_loan, R.color.pro_icon_loan)
        setupAction(binding.includeGoldLoan.root, "Gold Loan", R.drawable.ic_shop_unique, R.color.bg_gold, R.color.icon_gold)
        setupAction(binding.includeCreditScore.root, "Credit Score", R.drawable.ic_scan_qr_unique, R.color.pro_bg_deposit, R.color.pro_icon_deposit)
        setupAction(binding.includeRewards.root, "Rewards", R.drawable.ic_nav_home_unique, R.color.pro_bg_balance, R.color.pro_icon_balance)
    }

    private fun setupAction(view: View, text: String, iconResId: Int, bgColorId: Int, iconColorId: Int) {
        view.findViewById<TextView>(R.id.actionText).text = text
        view.findViewById<ImageView>(R.id.actionIcon).apply {
            setImageResource(iconResId)
            // Fix: backgroundTintList was being applied to ImageView directly which might not be what's intended for item_action layout
            // But since the user wants to fix unresolved references, keeping logic but ensuring resources exist.
            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, iconColorId))
        }
    }

    private fun toggleBalance() {
        isBalanceShown = !isBalanceShown
        val actionText = binding.includeCheckBalance.root.findViewById<TextView>(R.id.actionText)

        if (isBalanceShown) {
            actionText.text = "₹1,85,540"
            actionText.setTextColor(ContextCompat.getColor(this, R.color.pro_icon_balance))
        } else {
            actionText.text = "Check Balance"
            actionText.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        }
    }

    private fun setupRecyclerView() {
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = TransactionAdapter(getRecentTransactions())

        binding.historyHeader.setOnClickListener {
            binding.transactionRecyclerView.visibility = if (binding.transactionRecyclerView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        
        binding.btnSeeAllHistory.setOnClickListener {
            Toast.makeText(this, "Full history coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRecentTransactions(): List<Transaction> {
        return listOf(
            Transaction("Paid to Swiggy", "Today • 08:45 PM", "-₹342", true),
            Transaction("Received from Akash Yadav", "Today • 12:30 PM", "+₹2,500", false),
            Transaction("Jio Prepaid Recharge", "Yesterday • 10:15 AM", "-₹749", true),
            Transaction("Salary Credited", "25 Oct • 09:00 AM", "+₹85,000", false)
        )
    }
}
