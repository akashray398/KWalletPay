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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private var isBalanceShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupActionButtons()
        setupFinancialServices()
        setupRecyclerView()
        setupSettings()
        setupNotifications()
        setupReferralCard()
        setupProfile()
        setupBottomNavigation()
        startReferCardAnimations()
    }

    private fun startReferCardAnimations() {
        val referGlow = findViewById<View>(R.id.referGlow)
        val referIcon = findViewById<View>(R.id.referIcon)

        // Glow Pulse & Rotate Animation
        ObjectAnimator.ofFloat(referGlow, View.ALPHA, 0.2f, 0.5f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        ObjectAnimator.ofFloat(referGlow, View.ROTATION, 0f, 360f).apply {
            duration = 10000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            start()
        }

        // Icon Floating Animation
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

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.navHome)?.setOnClickListener {
            updateBottomNavSelection(it)
        }
        findViewById<View>(R.id.navExplore)?.setOnClickListener {
            updateBottomNavSelection(it)
            showExploreBottomSheet()
        }
        findViewById<View>(R.id.navHistory)?.setOnClickListener {
            updateBottomNavSelection(it)
            showHistoryBottomSheet()
        }
    }

    private fun updateBottomNavSelection(selected: View) {
        val navHome = findViewById<View>(R.id.navHome)
        val navExplore = findViewById<View>(R.id.navExplore)
        val navHistory = findViewById<View>(R.id.navHistory)

        val navs = listOf(
            Triple(navHome, R.id.ivNavHome, R.id.tvNavHome),
            Triple(navExplore, R.id.ivNavExplore, R.id.tvNavExplore),
            Triple(navHistory, R.id.ivNavHistory, R.id.tvNavHistory)
        )

        navs.forEach { (view, iconId, textId) ->
            val icon = view?.findViewById<ImageView>(iconId)
            val text = view?.findViewById<TextView>(textId)

            if (view == selected) {
                icon?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                text?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            } else {
                icon?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.text_secondary))
                text?.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            }
        }
    }

    private fun showExploreBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_explore_bottom_sheet, null)

        // Investments
        setupActionInExplore(view, R.id.exploreStocks, "Stocks", R.drawable.ic_nav_explorer_unique, R.color.explore_bg_invest, R.color.explore_icon_invest)
        setupActionInExplore(view, R.id.exploreMutualFunds, "Mutual Funds", R.drawable.ic_action_deposit_unique, R.color.explore_bg_invest, R.color.explore_icon_invest)
        setupActionInExplore(view, R.id.exploreGold, "Digital Gold", R.drawable.ic_shop_unique, R.color.bg_gold, R.color.icon_gold)
        setupActionInExplore(view, R.id.exploreInsurance, "Insurance", R.drawable.ic_profile_placeholder_unique, R.color.explore_bg_insurance, R.color.explore_icon_insurance)

        // Shopping
        setupActionInExplore(view, R.id.exploreShopping, "Store", R.drawable.ic_shop_unique, R.color.explore_bg_shopping, R.color.explore_icon_shopping)
        setupActionInExplore(view, R.id.exploreVouchers, "Gift Cards", R.drawable.ic_action_transfer_unique, R.color.bg_rewards, R.color.icon_rewards)
        setupActionInExplore(view, R.id.exploreTravel, "Travel", R.drawable.ic_nav_home_unique, R.color.explore_bg_travel, R.color.explore_icon_travel)
        setupActionInExplore(view, R.id.exploreMovies, "Movies", R.drawable.baseline_circle_notifications_24, R.color.colorAccent, R.color.white)

        // Utilities
        setupActionInExplore(view, R.id.exploreElectricity, "Electricity", R.drawable.ic_action_paybill_unique, R.color.explore_bg_utilities, R.color.explore_icon_utilities)
        setupActionInExplore(view, R.id.exploreRecharge, "Recharge", R.drawable.ic_scan_qr_unique, R.color.bg_paybill, R.color.icon_paybill)
        setupActionInExplore(view, R.id.exploreGas, "Gas", R.drawable.ic_action_deposit_unique, R.color.explore_bg_utilities, R.color.explore_icon_utilities)
        setupActionInExplore(view, R.id.exploreWater, "Water", R.drawable.ic_action_paybill_unique, R.color.explore_bg_utilities, R.color.explore_icon_utilities)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showHistoryBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_history_bottom_sheet, null)

        val historyRv = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val transactions = getRecentTransactions() // Using same data for now

        historyRv.layoutManager = LinearLayoutManager(this)
        historyRv.adapter = TransactionAdapter(transactions)

        view.findViewById<View>(R.id.btnDownloadStatement)?.setOnClickListener {
            Toast.makeText(this, "Downloading PDF Statement...", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.btnFilterMonth)?.setOnClickListener {
            Toast.makeText(this, "Opening Month Filter...", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupActionInExplore(parent: View, id: Int, text: String, iconRes: Int, bgColor: Int, iconColor: Int) {
        val view = parent.findViewById<View>(id)
        view?.findViewById<TextView>(R.id.actionText)?.text = text
        val iconView = view?.findViewById<ImageView>(R.id.actionIcon)
        iconView?.setImageResource(iconRes)
        iconView?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, bgColor))
        iconView?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, iconColor))

        view?.setOnClickListener {
            Toast.makeText(this, "$text feature is coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupProfile() {
        findViewById<View>(R.id.profileCard)?.setOnClickListener {
            showProfileBottomSheet()
        }
    }

    private fun showProfileBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_profile_bottom_sheet, null)

        setupSettingOption(view, R.id.optionMyQr, "My QR Code", "View your unique UPI QR", R.drawable.ic_scan_qr_unique)
        setupSettingOption(view, R.id.optionBanks, "Bank Accounts", "3 Banks Linked", R.drawable.ic_check_balance_unique)
        setupSettingOption(view, R.id.optionUpiSettings, "UPI Settings", "Manage IDs & PINs", R.drawable.ic_settings)
        setupSettingOption(view, R.id.optionAutopay, "AutoPay", "Manage subscriptions & bills", R.drawable.ic_action_paybill_unique)
        setupSettingOption(view, R.id.optionScreenLock, "Screen Lock", "Biometric & App Lock enabled", R.drawable.ic_settings)
        setupSettingOption(view, R.id.optionLanguage, "Language", "English (Default)", R.drawable.ic_shop_unique)
        setupSettingOption(view, R.id.optionAbout, "About & Support", "24/7 Help center", R.drawable.ic_nav_home_unique)

        view.findViewById<ImageView>(R.id.btnEditProfile)?.setOnClickListener {
            Toast.makeText(this, "Edit Profile coming soon!", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<TextView>(R.id.btnProfileLogout)?.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Logged out safely", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupReferralCard() {
        findViewById<MaterialButton>(R.id.referButton)?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Join KWalletPay")
            val shareMessage = "Hey! Join me on KWalletPay, the fastest way to pay and earn cashback. Use my link to get ₹201 on your first payment: https://kwalletpay.app/refer/akash_secure"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Invite via"))
        }
    }

    private fun setupSettings() {
        findViewById<View>(R.id.settingsCard)?.setOnClickListener {
            showSettingsBottomSheet()
        }
    }

    private fun setupNotifications() {
        findViewById<View>(R.id.notificationCard)?.setOnClickListener {
            showNotificationsBottomSheet()
        }
    }

    private fun showNotificationsBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_notifications_bottom_sheet, null)
        val container = view.findViewById<LinearLayout>(R.id.notificationsContainer)

        val notifications = listOf(
            Notification("Cashback Received!", "You've received ₹50 cashback on your last Zomato order.", "5 mins ago"),
            Notification("Security Alert", "New login detected from a Chrome browser on Windows.", "1 hour ago"),
            Notification("Monthly Summary", "Your spending for October is 15% lower than last month. Keep it up!", "Today")
        )

        notifications.forEach { notification ->
            val notificationView = layoutInflater.inflate(R.layout.item_notification, container, false)
            notificationView.findViewById<TextView>(R.id.notificationTitle).text = notification.title
            notificationView.findViewById<TextView>(R.id.notificationMessage).text = notification.message
            notificationView.findViewById<TextView>(R.id.notificationTime).text = notification.time
            container.addView(notificationView)
        }

        view.findViewById<TextView>(R.id.btnMarkAllRead)?.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "All caught up!", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSettingsBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_settings_bottom_sheet, null)

        setupSettingOption(view, R.id.optionProfile, "My Profile", "KYC Verified • Personal Details", R.drawable.ic_profile_placeholder_unique)
        setupSettingOption(view, R.id.optionBankAccounts, "Bank Accounts", "Manage linked accounts & cards", R.drawable.ic_check_balance_unique)
        setupSettingOption(view, R.id.optionUpiIds, "UPI IDs", "Primary: akash@kwallet", R.drawable.ic_settings)
        setupSettingOption(view, R.id.optionSecurity, "Security", "Fingerprint, PIN & App Lock", R.drawable.ic_settings)
        setupSettingOption(view, R.id.optionHelp, "Help & Support", "24/7 Chat & Call support", R.drawable.ic_nav_home_unique)

        view.findViewById<TextView>(R.id.btnLogout)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupSettingOption(parentView: View, id: Int, title: String, subtitle: String, iconRes: Int = R.drawable.ic_settings) {
        val view = parentView.findViewById<View>(id)
        view?.findViewById<TextView>(R.id.optionTitle)?.text = title
        view?.findViewById<TextView>(R.id.optionSubtitle)?.text = subtitle
        view?.findViewById<ImageView>(R.id.optionIcon)?.setImageResource(iconRes)
        view?.setOnClickListener {
            Toast.makeText(this, "$title feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupActionButtons() {
        setupAction(R.id.includeScanQR, "Scan QR", R.drawable.ic_scan_qr_unique, R.color.pro_bg_scan, R.color.pro_icon_scan)
        setupAction(R.id.includeCheckBalance, "Check Balance", R.drawable.ic_check_balance_unique, R.color.pro_bg_balance, R.color.pro_icon_balance)
        setupAction(R.id.includeShop, "Shop", R.drawable.ic_shop_unique, R.color.pro_bg_shop, R.color.pro_icon_shop)
        setupAction(R.id.includeDeposit, "Deposit", R.drawable.ic_action_deposit_unique, R.color.pro_bg_deposit, R.color.pro_icon_deposit)
        setupAction(R.id.includePaybill, "Pay Bill", R.drawable.ic_action_paybill_unique, R.color.pro_bg_paybill, R.color.pro_icon_paybill)
        setupAction(R.id.includeTransfer, "Transfer", R.drawable.ic_action_transfer_unique, R.color.pro_bg_transfer, R.color.pro_icon_transfer)

        findViewById<View>(R.id.cardCheckBalance)?.setOnClickListener {
            toggleBalance()
        }
        findViewById<View>(R.id.cardShop)?.setOnClickListener {
            showExploreBottomSheet()
        }
    }

    private fun setupFinancialServices() {
        setupAction(R.id.includePersonalLoan, "Personal Loan", R.drawable.ic_action_deposit_unique, R.color.pro_bg_loan, R.color.pro_icon_loan)
        setupAction(R.id.includeGoldLoan, "Gold Loan", R.drawable.ic_shop_unique, R.color.bg_gold, R.color.icon_gold)
        setupAction(R.id.includeCreditScore, "Credit Score", R.drawable.ic_scan_qr_unique, R.color.pro_bg_deposit, R.color.pro_icon_deposit)
        setupAction(R.id.includeRewards, "Rewards", R.drawable.ic_nav_home_unique, R.color.pro_bg_balance, R.color.pro_icon_balance)
    }

    private fun setupAction(includeId: Int, text: String, iconResId: Int, bgColorId: Int, iconColorId: Int) {
        val view = findViewById<View>(includeId)
        view?.apply {
            findViewById<TextView>(R.id.actionText)?.text = text
            val iconView = findViewById<ImageView>(R.id.actionIcon)
            iconView?.setImageResource(iconResId)
            iconView?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, bgColorId))
            iconView?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, iconColorId))
        }
    }

    private fun toggleBalance() {
        isBalanceShown = !isBalanceShown
        val checkBalanceView = findViewById<View>(R.id.includeCheckBalance)
        val actionText = checkBalanceView?.findViewById<TextView>(R.id.actionText)

        if (isBalanceShown) {
            actionText?.text = "₹1,85,540"
            actionText?.setTextColor(ContextCompat.getColor(this, R.color.pro_icon_balance))
        } else {
            actionText?.text = "Check Balance"
            actionText?.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        }
    }

    private fun getRecentTransactions(): List<Transaction> {
        return listOf(
            Transaction("Paid to Swiggy", "Today • 08:45 PM", "-₹342", true),
            Transaction("Received from Akash Yadav", "Today • 12:30 PM", "+₹2,500", false),
            Transaction("Jio Prepaid Recharge", "Yesterday • 10:15 AM", "-₹749", true),
            Transaction("Paid to Grocery Store", "Yesterday • 09:15 AM", "-₹1,240", true),
            Transaction("Salary Credited", "25 Oct • 09:00 AM", "+₹85,000", false),
            Transaction("Netflix India", "24 Oct • 11:45 PM", "-₹649", true),
            Transaction("Electricity Bill", "22 Oct • 02:20 PM", "-₹2,380", true),
            Transaction("Indian Oil Petrol", "20 Oct • 08:30 AM", "-₹1,500", true),
            Transaction("Khushi Bansal", "18 Oct • 04:00 PM", "-₹4,200", true)
        )
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.transactionRecyclerView)
        val historyHeader = findViewById<View>(R.id.historyHeader)
        val historyLabel = findViewById<TextView>(R.id.historyLabel)
        val btnSeeAll = findViewById<View>(R.id.btnSeeAllHistory)

        val transactions = getRecentTransactions()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TransactionAdapter(transactions)

        historyHeader?.setOnClickListener {
            if (recyclerView.visibility == View.VISIBLE) {
                recyclerView.visibility = View.GONE
                historyLabel?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            } else {
                recyclerView.visibility = View.VISIBLE
                historyLabel?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
        }

        btnSeeAll?.setOnClickListener {
            showHistoryBottomSheet()
        }
    }
}
