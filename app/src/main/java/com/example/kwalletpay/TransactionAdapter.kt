package com.example.kwalletpay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        
        if (transaction.isNegative) {
            holder.amount.setTextColor(holder.itemView.context.getColor(android.R.color.black))
        } else {
            holder.amount.setTextColor(holder.itemView.context.getColor(R.color.colorPrimary))
        }
    }

    override fun getItemCount() = transactions.size
}