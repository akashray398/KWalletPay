package com.example.kwalletpay

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
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

        // Screen Load Animation
        binding.mainContent.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        setupActionButtons()
        setupFinancialServices()
        setupRecyclerView()
        setupDrawer()
        setupBottomNavigation()
        startReferCardAnimations()
    }

    private fun setupDrawer() {
        binding.profileCard.setOnClickListener {
            animateClick(it)
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        val drawerBinding = binding.drawerProfile
        setupProfileOptions(drawerBinding)

        drawerBinding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        drawerBinding.btnLogout.setOnClickListener {
            animateClick(it)
            Toast.makeText(this, "Logged out safely", Toast.LENGTH_SHORT).show()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupProfileOptions(pBinding: ActivityProfileBinding) {
        setupOptionItem(pBinding.optionAccountManagement, R.string.account_management, "Manage your linked bank accounts", R.drawable.ic_check_balance_unique)
        setupOptionItem(pBinding.optionTransactionHistory, R.string.transaction_history, "View all your past transactions", R.drawable.ic_nav_history)
        setupOptionItem(pBinding.optionEverythingUpi, R.string.everything_upi, "UPI IDs, QR codes, and more", R.drawable.ic_settings)
        setupOptionItem(pBinding.optionOrders, R.string.orders, "Track and manage your orders", R.drawable.ic_shop_unique)
        setupOptionItem(pBinding.optionWishlist, R.string.wishlist, "Items you've saved for later", R.drawable.ic_action_transfer_unique)
        setupOptionItem(pBinding.optionSavedAddresses, R.string.saved_addresses, "Manage your delivery locations", R.drawable.ic_nav_home_unique)
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
            animateClick(it)
            Toast.makeText(this, "Opening ${getString(titleRes)}...", Toast.LENGTH_SHORT).show()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun animateClick(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
    }

    private fun startReferCardAnimations() {
        val referGlow = binding.referCard.findViewById<View>(R.id.referGlow)
        if (referGlow != null) {
            ObjectAnimator.ofFloat(referGlow, View.ALPHA, 0.2f, 0.5f).apply {
                duration = 3000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }

        val referIcon = binding.referCard.findViewById<View>(R.id.referIcon)
        if (referIcon != null) {
            val floatAnim = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -10f, 10f)
            val rotateAnim = PropertyValuesHolder.ofFloat(View.ROTATION, -15f, -5f)

            ObjectAnimator.ofPropertyValuesHolder(referIcon, floatAnim, rotateAnim).apply {
                duration = 2500
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.navHome.setOnClickListener { 
            animateClick(it)
            updateBottomNavSelection(it) 
        }
        binding.navExplore.setOnClickListener {
            animateClick(it)
            updateBottomNavSelection(it)
            startActivity(Intent(this, ShopActivity::class.java))
        }
        binding.navHistory.setOnClickListener {
            animateClick(it)
            updateBottomNavSelection(it)
            Toast.makeText(this, "Transaction History coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBottomNavSelection(selected: View) {
        val navItems = listOf(
            Triple(binding.navHome, binding.indicatorHome, listOf(binding.ivNavHome, binding.tvNavHome)),
            Triple(binding.navExplore, binding.indicatorExplore, listOf(binding.ivNavExplore, binding.tvNavExplore)),
            Triple(binding.navHistory, binding.indicatorHistory, listOf(binding.ivNavHistory, binding.tvNavHistory))
        )

        navItems.forEach { (root, indicator, contents) ->
            val isActive = root == selected
            val color = if (isActive) R.color.accent else R.color.text_secondary
            
            indicator.animate().alpha(if (isActive) 1f else 0f).setDuration(200).start()
            
            contents.forEach { view ->
                val tint = ContextCompat.getColor(this, color)
                if (view is ImageView) view.imageTintList = ColorStateList.valueOf(tint)
                if (view is TextView) view.setTextColor(tint)
                
                view.animate()
                    .scaleX(if (isActive) 1.1f else 1.0f)
                    .scaleY(if (isActive) 1.1f else 1.0f)
                    .setDuration(200)
                    .start()
            }
        }
    }

    private fun setupActionButtons() {
        setupAction(binding.includeScanQR.root, "Scan QR", R.drawable.ic_scan_qr_unique, R.color.pro_bg_scan, R.color.pro_icon_scan)
        setupAction(binding.includeCheckBalance.root, "Check Balance", R.drawable.ic_check_balance_unique, R.color.pro_bg_balance, R.color.pro_icon_balance)
        setupAction(binding.includeShop.root, "Shop", R.drawable.ic_shop_unique, R.color.pro_bg_shop, R.color.pro_icon_shop)
        setupAction(binding.includeDeposit.root, "Deposit", R.drawable.ic_action_deposit_unique, R.color.pro_bg_deposit, R.color.pro_icon_deposit)
        setupAction(binding.includePaybill.root, "Pay Bill", R.drawable.ic_action_paybill_unique, R.color.pro_bg_paybill, R.color.pro_icon_paybill)
        setupAction(binding.includeTransfer.root, "Transfer", R.drawable.ic_action_transfer_unique, R.color.pro_bg_transfer, R.color.pro_icon_transfer)

        binding.cardCheckBalance.setOnClickListener { 
            animateClick(it)
            toggleBalance() 
        }
        binding.cardShop.setOnClickListener { 
            animateClick(it)
            startActivity(Intent(this, ShopActivity::class.java)) 
        }
        binding.cardDeposit.setOnClickListener { 
            animateClick(it)
            startActivity(Intent(this, DepositActivity::class.java)) 
        }
        
        binding.referButton.setOnClickListener {
            animateClick(it)
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
            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, iconColorId))
        }
        view.setOnClickListener { animateClick(it) }
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
        binding.transactionRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_staggered)

        binding.historyHeader.setOnClickListener {
            animateClick(it)
            binding.transactionRecyclerView.visibility = if (binding.transactionRecyclerView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        
        binding.btnSeeAllHistory.setOnClickListener {
            animateClick(it)
            Toast.makeText(this, "Full history coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRecentTransactions(): List<Transaction> {
        return listOf(
            Transaction("Paid to Swiggy", "Today • 08:45 PM", "-₹342", R.drawable.ic_shop_unique, true),
            Transaction("Received from Akash Yadav", "Today • 12:30 PM", "+₹2,500", R.drawable.ic_action_transfer_unique, false),
            Transaction("Jio Prepaid Recharge", "Yesterday • 10:15 AM", "-₹749", R.drawable.ic_action_paybill_unique, true),
            Transaction("Salary Credited", "25 Oct • 09:00 AM", "+₹85,000", R.drawable.ic_action_transfer_unique, false)
        )
    }
}
