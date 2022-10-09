package com.example.crypto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.crypto.activity.MainActivity
import com.example.crypto.R
import com.example.crypto.adapters.CryptoHoldingAdapter
import com.example.crypto.adapters.CurrentPricesAdapter
import com.example.crypto.adapters.RecentTransactionAdapter
import com.example.crypto.config.Config
import com.example.crypto.databinding.FragmentEmptyOrValuesStateBinding
import com.example.crypto.model.CryptoHolding
import com.example.crypto.model.CryptoPrices
import com.example.crypto.model.RecentTransaction
import com.example.crypto.repository.NetworkRepository
import com.example.crypto.utils.Constant
import com.google.android.material.button.MaterialButton
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject

class EmptyOrValuesStateFragment : Fragment() {
    private var activity: MainActivity? = null
    private var emptyOrValuesStateBinding: FragmentEmptyOrValuesStateBinding? = null
    private lateinit var networkRepository: NetworkRepository
    private lateinit var contentFoundNSV: NestedScrollView
    private lateinit var loadingPB: ProgressBar

    // ::: Crypto account related..
    private lateinit var bitcoinIV: ImageView
    private lateinit var ethereumIV: ImageView
    private lateinit var cryptoAccountTitleMTV: MaterialTextView
    private lateinit var cryptoAccountSubTitleMTV: MaterialTextView
    private lateinit var cryptoAccountDepositMBtn: MaterialButton
    private lateinit var cryptoAccountBalanceMTV: MaterialTextView

    // ::: Crypto holding related..
    private lateinit var cryptoHoldingCL: ConstraintLayout
    private lateinit var cryptoHoldingRV: RecyclerView
    private lateinit var cryptoHoldingRA: CryptoHoldingAdapter
    private lateinit var cryptoHoldings: ArrayList<CryptoHolding>

    // ::: Crypto prices related..
    private lateinit var cryptoPricesCL: ConstraintLayout
    private lateinit var cryptoPricesRV: RecyclerView
    private lateinit var cryptoPricesRA: CurrentPricesAdapter
    private lateinit var cryptoPrices: ArrayList<CryptoPrices>

    // ::: Recent transaction related..
    private lateinit var divider2: MaterialDivider
    private lateinit var recentTransactionCL: ConstraintLayout
    private lateinit var recentTransactionRV: RecyclerView
    private lateinit var recentTransactionRA: RecentTransactionAdapter
    private lateinit var recentTransactions: ArrayList<RecentTransaction>

    // ::: logic based..
    private var isCalledFor = Constant.emptyState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        emptyOrValuesStateBinding = FragmentEmptyOrValuesStateBinding.inflate(layoutInflater)
        activity = getActivity() as MainActivity
        initializeArgumentBasedVariables()
        setActionBarTitleBasedOnCondition()
        initializeVariablesAndReferences()
        setDummyImagesIntoCryptoAccount()
        observeAndFetchLiveDataAPICalls()
        return emptyOrValuesStateBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emptyOrValuesStateBinding = null
        viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        viewModelStore.clear()
    }

    // ::: Function to initialize arguments variables..
    private fun initializeArgumentBasedVariables() {
        arguments?.apply {
            if (containsKey(Constant.isCalledFor)) {
                isCalledFor = getString(Constant.isCalledFor, Constant.emptyState)
            }
        }
    }

    // ::: Function to set action bar title based on condition..
    private fun setActionBarTitleBasedOnCondition() {
        activity?.apply {
            supportActionBar?.title = requireContext().getString(
                if (Constant.emptyState == isCalledFor) {
                    R.string.empty_state
                } else {
                    R.string.values_state
                }
            )
        }
    }

    // ::: Function to initialize all the views and variables..
    private fun initializeVariablesAndReferences() {
        bitcoinIV = emptyOrValuesStateBinding!!.idIVBitcoin
        ethereumIV = emptyOrValuesStateBinding!!.idIVEthereum
        cryptoAccountTitleMTV = emptyOrValuesStateBinding!!.idMTVCryptoAccountTitle
        cryptoAccountSubTitleMTV = emptyOrValuesStateBinding!!.idMTVCryptoAccountSubtitle
        cryptoAccountBalanceMTV = emptyOrValuesStateBinding!!.idMTVCryptoAccountBalance
        cryptoAccountDepositMBtn = emptyOrValuesStateBinding!!.idMBTNDepositCrypto
        contentFoundNSV = emptyOrValuesStateBinding!!.idNSVContentFound
        loadingPB = emptyOrValuesStateBinding!!.idPBLoading

        // ::: Crypto holding related..
        cryptoHoldingCL = emptyOrValuesStateBinding!!.idCLYourCryptoHolding
        cryptoHoldingRV = emptyOrValuesStateBinding!!.idRVCryptoHolding

        // ::: Crypto prices related..
        cryptoPricesCL = emptyOrValuesStateBinding!!.idCLCryptoPrices
        cryptoPricesRV = emptyOrValuesStateBinding!!.idRVCryptoPrices

        // ::: Recent transaction related..
        divider2 = emptyOrValuesStateBinding!!.idMaterialDivider2
        recentTransactionCL = emptyOrValuesStateBinding!!.idCLRecentTransaction
        recentTransactionRV = emptyOrValuesStateBinding!!.idRVRecentTransaction

        // ::: Init list..
        cryptoHoldings = ArrayList()
        cryptoPrices = ArrayList()
        recentTransactions = ArrayList()

        // ::: View model
        networkRepository = ViewModelProvider(this@EmptyOrValuesStateFragment)[NetworkRepository::class.java]
    }

    // ::: Function to set bitcoin and ethereum images in crypto account card..
    private fun setDummyImagesIntoCryptoAccount() {
        val imageLoader = ImageLoader.Builder(requireContext()).components { add(SvgDecoder.Factory()) }.build()
        bitcoinIV.load(Config.bitcoinUrl, imageLoader)
        ethereumIV.load(Config.ethereumUrl, imageLoader)
    }

    // ::: Function to fetch & observe api calls results..
    private fun observeAndFetchLiveDataAPICalls() {
        val emptyAndValuesStateObs = Observer<JSONObject?> { response ->
            if (response != null) {
                if (response.has(Constant.crypto_balance)) {
                    val cryptoBalance = response.getJSONObject(Constant.crypto_balance)
                    if (cryptoBalance.has(Constant.title)) {
                        cryptoAccountTitleMTV.text = cryptoBalance.getString(Constant.title)
                    }

                    if (cryptoBalance.has(Constant.subtitle)) {
                        cryptoAccountSubTitleMTV.text = cryptoBalance.getString(Constant.subtitle)
                    }

                    if (Constant.emptyState == isCalledFor) {
                        cryptoAccountDepositMBtn.visibility = View.VISIBLE
                    } else {
                        if (cryptoBalance.has(Constant.current_bal_in_usd)) {
                            val balance = "$${cryptoBalance.getString(Constant.current_bal_in_usd)}"
                            cryptoAccountBalanceMTV.text = balance
                            cryptoAccountBalanceMTV.visibility = View.VISIBLE
                        }
                    }
                }

                if (response.has(Constant.your_crypto_holdings)) {
                    val cryptoHoldingArray = response.getJSONArray(Constant.your_crypto_holdings)
                    val cryptoHoldingLength = cryptoHoldingArray.length()
                    for (i in 0 until cryptoHoldingLength) {
                        cryptoHoldings.add(CryptoHolding(cryptoHoldingArray.getJSONObject(i)))
                    }
                    cryptoHoldingCL.visibility = View.VISIBLE
                    prepareCryptoHoldingsRV()
                } else {
                    cryptoHoldingCL.visibility = View.GONE
                }

                if (response.has(Constant.crypto_prices)) {
                    val cryptoPricesArray = response.getJSONArray(Constant.crypto_prices)
                    val cryptoPricesLength = cryptoPricesArray.length()
                    for (i in 0 until cryptoPricesLength) {
                        cryptoPrices.add(CryptoPrices(cryptoPricesArray.getJSONObject(i)))
                    }
                    prepareCurrentPricesRV()
                    cryptoPricesCL.visibility = View.VISIBLE
                } else {
                    cryptoPricesCL.visibility = View.GONE
                }

                if (response.has(Constant.all_transactions)) {
                    val cryptoTransactionArray = response.getJSONArray(Constant.all_transactions)
                    val cryptoTransactionLength = cryptoTransactionArray.length()
                    for (i in 0 until cryptoTransactionLength) {
                        recentTransactions.add(RecentTransaction(cryptoTransactionArray.getJSONObject(i)))
                    }
                    if (cryptoTransactionLength > 0) {
                        divider2.visibility = View.VISIBLE
                        recentTransactionCL.visibility = View.VISIBLE
                        prepareRecentTransactionRV()
                    } else {
                        divider2.visibility = View.GONE
                        recentTransactionCL.visibility = View.GONE
                    }
                } else {
                    divider2.visibility = View.GONE
                    recentTransactionCL.visibility = View.GONE
                }
                contentFoundNSV.visibility = View.VISIBLE
            }
            loadingPB.visibility = View.GONE
        }
        networkRepository.emptyAndValuesStateLD.observe(viewLifecycleOwner, emptyAndValuesStateObs)

        // ::: fetching results from api based on conditions..
        if (Constant.emptyState == isCalledFor) {
            networkRepository.getEmptyStateResultThroughAPI(requireContext())
        } else {
            networkRepository.getValuesStateResultThroughAPI(requireContext())
        }
    }

    // ::: Function to prepare crypto holdings recycler view..
    private fun prepareCryptoHoldingsRV() {
        cryptoHoldingRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            cryptoHoldingRA = CryptoHoldingAdapter(requireContext(), isCalledFor, cryptoHoldings, ::onClickBuyOnCryptoHolding)
            adapter = cryptoHoldingRA
        }
    }

    // ::: Function to prepare recent transaction recycler view..
    private fun prepareRecentTransactionRV() {
        recentTransactionRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            recentTransactionRA = RecentTransactionAdapter(requireContext(), recentTransactions)
            adapter = recentTransactionRA
        }
    }

    // ::: Function to prepare current prices recycler view..
    private fun prepareCurrentPricesRV() {
        cryptoPricesRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            cryptoPricesRA = CurrentPricesAdapter(requireContext(), cryptoPrices)
            adapter = cryptoPricesRA
        }
    }

    // ::: This will triggered when you click on buy button in crypto holding flow..
    private fun onClickBuyOnCryptoHolding(pos: Int) {
        val cryptoHolding = cryptoHoldings[pos]
        cryptoHolding.isCalledToViewBalance = true
        cryptoHoldingRA.notifyItemChanged(pos, Any())
    }
}