package com.example.crypto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.crypto.databinding.CryptoHoldingRvItemBinding
import com.example.crypto.model.CryptoHolding
import com.example.crypto.utils.Constant

class CryptoHoldingAdapter(private val context: Context, private val isCalledFor: String, private val cryptoHoldings: ArrayList<CryptoHolding>, val onClickBuyOnCryptoHolding: (Int) -> Unit) :
    RecyclerView.Adapter<CryptoHoldingAdapter.CryptoHoldingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHoldingViewHolder {
        val cryptoHoldingRvItemBinding = CryptoHoldingRvItemBinding.inflate(LayoutInflater.from(context))
        val view = cryptoHoldingRvItemBinding.root
        view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return CryptoHoldingViewHolder(view, cryptoHoldingRvItemBinding)
    }

    override fun onBindViewHolder(holder: CryptoHoldingViewHolder, position: Int) {
        val cryptoHolding = cryptoHoldings[position]
        val imageLoader = ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build()
        holder.cryptoIconIV.load(cryptoHolding.logo, imageLoader)
        holder.cryptoNameMTV.text = cryptoHolding.title
        holder.cryptoTokenMTV.text = cryptoHolding.current_bal_in_token
        val balance = "$${cryptoHolding.current_bal_in_usd}"
        holder.cryptoBalanceMTV.text = balance

        if (Constant.emptyState == isCalledFor) {
            holder.cryptoTokenMTV.visibility = View.GONE
            val isCalledToViewBalance = cryptoHolding.isCalledToViewBalance
            if (isCalledToViewBalance) {
                holder.cryptoBalanceMTV.visibility = View.VISIBLE
                holder.cryptoBuyMBTN.visibility = View.GONE
                holder.cryptoDepositMBTN.visibility = View.GONE
            } else {
                holder.cryptoBalanceMTV.visibility = View.GONE
                holder.cryptoBuyMBTN.visibility = View.VISIBLE
                holder.cryptoDepositMBTN.visibility = View.VISIBLE
            }
        } else {
            holder.cryptoTokenMTV.visibility = View.VISIBLE
            holder.cryptoBalanceMTV.visibility = View.VISIBLE
            holder.cryptoBuyMBTN.visibility = View.GONE
            holder.cryptoDepositMBTN.visibility = View.GONE
        }

        holder.cryptoBuyMBTN.setOnClickListener {
            onClickBuyOnCryptoHolding(position)
        }
    }

    override fun getItemCount(): Int {
        return cryptoHoldings.size
    }

    inner class CryptoHoldingViewHolder(itemView: View, cryptoHoldingRvItemBinding: CryptoHoldingRvItemBinding) : ViewHolder(itemView) {
        val cryptoIconIV = cryptoHoldingRvItemBinding.idIVCryptoIcon
        val cryptoNameMTV = cryptoHoldingRvItemBinding.idMTVCryptoName
        val cryptoTokenMTV = cryptoHoldingRvItemBinding.idMTVCryptoToken
        val cryptoBalanceMTV = cryptoHoldingRvItemBinding.idMTVCryptoBalance
        val cryptoBuyMBTN = cryptoHoldingRvItemBinding.idMBtnBuy
        val cryptoDepositMBTN = cryptoHoldingRvItemBinding.idMBtnDeposit
    }
}