package com.example.crypto.model

import com.example.crypto.utils.Constant
import org.json.JSONObject

data class CryptoHolding(
    var logo: String? = null,
    var title: String? = null,
    var current_bal_in_token: String? = null,
    var current_bal_in_usd: String? = null,
    var isCalledToViewBalance: Boolean = false
) {

    constructor() : this(null) {}

    constructor(dataObj: JSONObject) : this() {
        if (dataObj.has(Constant.logo)) {
            logo = dataObj.getString(Constant.logo)
        }

        if (dataObj.has(Constant.title)) {
            title = dataObj.getString(Constant.title)
        }

        if (dataObj.has(Constant.current_bal_in_token)) {
            current_bal_in_token = dataObj.getString(Constant.current_bal_in_token)
        }

        if (dataObj.has(Constant.current_bal_in_usd)) {
            current_bal_in_usd = dataObj.getString(Constant.current_bal_in_usd)
        }
    }
}