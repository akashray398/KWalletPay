package com.example.kwalletpay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

data class Transaction(
    val title: String,
    val date: String,
    val amount: String,
    val isNegative: Boolean = true
)

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.transactionTitle)
        val date: TextView = view.findViewById(R.id.transactionDate)
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
        holder.date.text = transaction.date
        holder.amount.text = transaction.amount
        
        val context = holder.itemView.context
        if (transaction.isNegative) {
            // Use Primary Text color for negative or a soft red
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        } else {
            // Use Green/Emerald for credits
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.explore_icon_invest))
        }
    }

    override fun getItemCount() = transactions.size
}