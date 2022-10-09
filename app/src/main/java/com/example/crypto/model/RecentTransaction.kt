package com.example.crypto.model

import com.example.crypto.utils.Constant
import org.json.JSONObject

data class RecentTransaction(var txn_logo: String? = null, var title: String? = null, var txn_time: String? = null, var txn_amount: String? = null, var txn_sub_amount: String? = null) {

    constructor() : this(null) {}

    constructor(dataObj: JSONObject) : this() {
        if (dataObj.has(Constant.txn_logo)) {
            txn_logo = dataObj.getString(Constant.txn_logo)
        }

        if (dataObj.has(Constant.title)) {
            title = dataObj.getString(Constant.title)
        }

        if (dataObj.has(Constant.txn_time)) {
            txn_time = dataObj.getString(Constant.txn_time)
        }

        if (dataObj.has(Constant.txn_amount)) {
            txn_amount = dataObj.getString(Constant.txn_amount)
        }

        if (dataObj.has(Constant.txn_sub_amount)) {
            txn_sub_amount = dataObj.getString(Constant.txn_sub_amount)
        }
    }
}