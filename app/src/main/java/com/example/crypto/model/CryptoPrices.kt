package com.example.crypto.model

import com.example.crypto.utils.Constant
import org.json.JSONObject

data class CryptoPrices(var logo: String? = null, var title: String? = null, var current_price_in_usd: String? = null) {

    constructor() : this(null) {}

    constructor(dataObj: JSONObject) : this() {
        if (dataObj.has(Constant.logo)) {
            logo = dataObj.getString(Constant.logo)
        }

        if (dataObj.has(Constant.title)) {
            title = dataObj.getString(Constant.title)
        }

        if (dataObj.has(Constant.current_price_in_usd)) {
            current_price_in_usd = dataObj.getString(Constant.current_price_in_usd)
        }
    }
}