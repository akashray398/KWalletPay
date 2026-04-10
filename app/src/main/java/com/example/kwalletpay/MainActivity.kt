package com.example.kwalletpay

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

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
        setupRecyclerView()
        setupSettings()
        setupNotifications()
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
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSettingsBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_settings_bottom_sheet, null)

        // Setup Options
        setupSettingOption(view.findViewById(R.id.optionProfile), "My Profile", "KYC Verified • Personal Details")
        setupSettingOption(view.findViewById(R.id.optionBankAccounts), "Bank Accounts", "Manage linked accounts & cards")
        setupSettingOption(view.findViewById(R.id.optionUpiIds), "UPI IDs", "Primary: akash@kwallet")
        setupSettingOption(view.findViewById(R.id.optionSecurity), "Security", "Fingerprint, PIN & App Lock")
        setupSettingOption(view.findViewById(R.id.optionHelp), "Help & Support", "24/7 Chat & Call support")

        view.findViewById<TextView>(R.id.btnLogout)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupSettingOption(view: View, title: String, subtitle: String) {
        view.findViewById<TextView>(R.id.optionTitle).text = title
        view.findViewById<TextView>(R.id.optionSubtitle).text = subtitle
        view.setOnClickListener {
            // Handle option click
        }
    }

    private fun setupActionButtons() {
        // Row 1 Setup with Trending Colors
        setupAction(
            R.id.includeScanQR, 
            R.string.scan_qr, 
            R.drawable.ic_scan_qr_unique, 
            R.color.bg_scan, 
            R.color.icon_scan
        )
        setupAction(
            R.id.includeCheckBalance, 
            R.string.check_balance, 
            R.drawable.ic_check_balance_unique, 
            R.color.bg_balance, 
            R.color.icon_balance
        )
        setupAction(
            R.id.includeShop, 
            R.string.shop, 
            R.drawable.ic_shop_unique, 
            R.color.bg_shop, 
            R.color.icon_shop
        )

        // Row 2 Setup with Trending Colors
        setupAction(
            R.id.includeDeposit, 
            R.string.deposit, 
            R.drawable.ic_action_deposit_unique, 
            R.color.bg_deposit, 
            R.color.icon_deposit
        )
        setupAction(
            R.id.includePaybill, 
            R.string.paybill, 
            R.drawable.ic_action_paybill_unique, 
            R.color.bg_paybill, 
            R.color.icon_paybill
        )
        setupAction(
            R.id.includeTransfer, 
            R.string.transfer, 
            R.drawable.ic_action_transfer_unique, 
            R.color.bg_transfer, 
            R.color.icon_transfer
        )

        // Set Click Listener for Check Balance
        findViewById<View>(R.id.cardCheckBalance)?.setOnClickListener {
            toggleBalance()
        }
    }

    private fun setupAction(includeId: Int, textResId: Int, iconResId: Int, bgColorId: Int, iconColorId: Int) {
        val view = findViewById<View>(includeId)
        view?.apply {
            findViewById<TextView>(R.id.actionText)?.text = getString(textResId)
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
            actionText?.setTextColor(ContextCompat.getColor(this, R.color.icon_balance))
        } else {
            actionText?.text = getString(R.string.check_balance)
            actionText?.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.transactionRecyclerView)
        val historyLabel = findViewById<TextView>(R.id.historyLabel)
        
        val transactions = listOf(
            Transaction("Starbucks Coffee", "Today • 10:30 AM", "-₹450", true),
            Transaction("Amazon India", "Yesterday • 02:15 PM", "-₹2,499", true),
            Transaction("Monthly Salary", "22 Oct • 09:00 AM", "+₹85,000", false),
            Transaction("Netflix Subscription", "20 Oct • 11:45 PM", "-₹649", true),
            Transaction("Zomato Order", "19 Oct • 08:20 PM", "-₹380", true),
            Transaction("Dividend Payout", "18 Oct • 10:00 AM", "+₹1,200", false),
            Transaction("Apple Store", "15 Oct • 04:00 PM", "-₹1,200", true)
        )
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TransactionAdapter(transactions)

        // Toggle visibility on click
        historyLabel?.setOnClickListener {
            if (recyclerView.visibility == View.VISIBLE) {
                recyclerView.visibility = View.GONE
                historyLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            } else {
                recyclerView.visibility = View.VISIBLE
                historyLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
        }
    }
}
