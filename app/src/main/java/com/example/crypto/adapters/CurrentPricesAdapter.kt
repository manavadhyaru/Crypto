package com.example.crypto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.crypto.databinding.CurrentPricesRvItemBinding
import com.example.crypto.model.CryptoPrices

class CurrentPricesAdapter(private val context: Context, private val currentPrices: ArrayList<CryptoPrices>) :
    RecyclerView.Adapter<CurrentPricesAdapter.CurrentPricesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentPricesViewHolder {
        val currentPricesRvItemBinding = CurrentPricesRvItemBinding.inflate(LayoutInflater.from(context))
        val view = currentPricesRvItemBinding.root
        view.layoutParams = RecyclerView.LayoutParams((parent.measuredWidth * 0.5f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        return CurrentPricesViewHolder(view, currentPricesRvItemBinding)
    }

    override fun onBindViewHolder(holder: CurrentPricesViewHolder, position: Int) {
        val currentPrice = currentPrices[position]
        val imageLoader = ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build()
        holder.cryptoIconIV.load(currentPrice.logo, imageLoader)
        holder.cryptoNameMTV.text = currentPrice.title
        val balance = "$${currentPrice.current_price_in_usd}"
        holder.cryptoPricesMTV.text = balance
    }

    override fun getItemCount(): Int {
        return currentPrices.size
    }

    inner class CurrentPricesViewHolder(itemView: View, currentPricesRvItemBinding: CurrentPricesRvItemBinding) : RecyclerView.ViewHolder(itemView) {
        val cryptoIconIV = currentPricesRvItemBinding.idIVCryptoIcon
        val cryptoNameMTV = currentPricesRvItemBinding.idMTVCryptoName
        val cryptoPricesMTV = currentPricesRvItemBinding.idMTVCryptoPrice
    }
}