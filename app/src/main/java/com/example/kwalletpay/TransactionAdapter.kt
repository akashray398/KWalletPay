package com.example.kwalletpay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

data class Transaction(
    val title: String,
    val status: String,
    val amount: String,
    val iconRes: Int,
    val isNegative: Boolean = true
)

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.transactionIcon)
        val title: TextView = view.findViewById(R.id.transactionTitle)
        val status: TextView = view.findViewById(R.id.transactionStatus)
        val amount: TextView = view.findViewById(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.title.text = transaction.title
        holder.status.text = transaction.status
        holder.amount.text = transaction.amount
        holder.icon.setImageResource(transaction.iconRes)
        
        val context = holder.itemView.context
        
        // Color coding for amount
        if (transaction.isNegative) {
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        } else {
            // Success/Credit color
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.accent))
        }

        // Status styling if needed
        if (transaction.status.contains("Pending", ignoreCase = true)) {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.icon_gold))
        } else {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
        }
    }

    override fun getItemCount() = transactions.size
}