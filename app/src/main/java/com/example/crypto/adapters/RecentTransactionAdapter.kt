package com.example.crypto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.crypto.databinding.RecentTransactionRvItemBinding
import com.example.crypto.model.RecentTransaction

class RecentTransactionAdapter(private val context: Context, private val recentTransactions: ArrayList<RecentTransaction>) :
    RecyclerView.Adapter<RecentTransactionAdapter.RecentTransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTransactionViewHolder {
        val recentTransactionRvItemBinding = RecentTransactionRvItemBinding.inflate(LayoutInflater.from(context))
        val view = recentTransactionRvItemBinding.root
        view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return RecentTransactionViewHolder(view, recentTransactionRvItemBinding)
    }

    override fun onBindViewHolder(holder: RecentTransactionViewHolder, position: Int) {
        val recentTransaction = recentTransactions[position]
        val imageLoader = ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build()
        holder.cryptoIconIV.load(recentTransaction.txn_logo, imageLoader)
        holder.cryptoNameMTV.text = recentTransaction.title
        holder.minutesMTV.text = recentTransaction.txn_time
        holder.minutesMTV.text = recentTransaction.txn_time
        holder.buyPriceMTV.text = recentTransaction.txn_sub_amount
        val balance = "$${recentTransaction.txn_amount}"
        holder.cryptoBalance.text = balance
    }

    override fun getItemCount(): Int {
        return recentTransactions.size
    }

    inner class RecentTransactionViewHolder(itemView: View, recentTransactionRvItemBinding: RecentTransactionRvItemBinding) : RecyclerView.ViewHolder(itemView) {
        val cryptoIconIV = recentTransactionRvItemBinding.idIVCryptoIcon
        val cryptoNameMTV = recentTransactionRvItemBinding.idMTVCryptoName
        val minutesMTV = recentTransactionRvItemBinding.idMTVMinutes
        val cryptoBalance = recentTransactionRvItemBinding.idMTVCryptoBalance
        val buyPriceMTV = recentTransactionRvItemBinding.idMTVBuyPrice
    }
}