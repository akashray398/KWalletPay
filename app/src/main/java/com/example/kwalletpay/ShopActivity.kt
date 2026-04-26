package com.example.kwalletpay

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kwalletpay.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupShopActions()
    }

    private fun setupShopActions() {
        // Recharges & Bills
        setupAction(binding.shopMobile.root, "Mobile", R.drawable.ic_shop_mobile, R.color.pro_bg_scan, R.color.pro_icon_scan)
        setupAction(binding.shopDth.root, "DTH", R.drawable.ic_shop_dth, R.color.pro_bg_paybill, R.color.pro_icon_paybill)
        setupAction(binding.shopElectricity.root, "Electricity", R.drawable.ic_shop_electricity, R.color.explore_bg_utilities, R.color.explore_icon_utilities)
        setupAction(binding.shopRent.root, "Rent", R.drawable.ic_shop_rent, R.color.pro_bg_transfer, R.color.pro_icon_transfer)

        // Travel
        setupAction(binding.shopFlights.root, "Flights", R.drawable.ic_shop_flights, R.color.explore_bg_travel, R.color.explore_icon_travel)
        setupAction(binding.shopBus.root, "Bus", R.drawable.ic_shop_bus, R.color.explore_bg_travel, R.color.explore_icon_travel)
        setupAction(binding.shopTrains.root, "Trains", R.drawable.ic_shop_trains, R.color.explore_bg_travel, R.color.explore_icon_travel)
        setupAction(binding.shopHotels.root, "Hotels", R.drawable.ic_shop_hotels, R.color.explore_bg_travel, R.color.explore_icon_travel)

        // Entertainment
        setupAction(binding.shopGooglePlay.root, "Google Play", R.drawable.ic_shop_google_play, R.color.bg_rewards, R.color.icon_rewards)
        setupAction(binding.shopVouchers.root, "Gift Cards", R.drawable.ic_shop_vouchers, R.color.bg_rewards, R.color.icon_rewards)
        setupAction(binding.shopMovies.root, "Movies", R.drawable.ic_shop_movies, R.color.colorAccent, R.color.white)
        setupAction(binding.shopEvents.root, "Events", R.drawable.ic_shop_events, R.color.colorAccent, R.color.white)
    }

    private fun setupAction(view: View, text: String, iconRes: Int, bgColor: Int, iconColor: Int) {
        view.findViewById<TextView>(R.id.actionText)?.text = text
        val iconView = view.findViewById<ImageView>(R.id.actionIcon)
        iconView?.setImageResource(iconRes)
        iconView?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, bgColor))
        iconView?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, iconColor))

        // Initial entrance animation
        view.alpha = 0f
        view.scaleX = 0.8f
        view.scaleY = 0.8f
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay((0..300).random().toLong())
            .start()

        view.setOnClickListener {
            // Interactive click animation
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                }.start()
            
            Toast.makeText(this, "$text booking is coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
