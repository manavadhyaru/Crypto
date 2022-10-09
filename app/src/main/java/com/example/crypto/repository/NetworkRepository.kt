package com.example.crypto.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crypto.config.Config
import org.json.JSONObject

class NetworkRepository : ViewModel() {

    private val emptyAndValuesStateMLD = MutableLiveData<JSONObject?>()
    val emptyAndValuesStateLD: LiveData<JSONObject?>
        get() = emptyAndValuesStateMLD

    // ::: Function to fetch result of empty state..
    fun getEmptyStateResultThroughAPI(context: Context) {
        val requestQueue = Volley.newRequestQueue(context)
        val urlToGetEmptyStateResult = "${Config.baseUrl}/empty-home"
        val getEmptyStateResultRequest = JsonObjectRequest(Request.Method.GET, urlToGetEmptyStateResult, null, {
            emptyAndValuesStateMLD.postValue(it)
        }, {
            emptyAndValuesStateMLD.postValue(null)
        })
        requestQueue.add(getEmptyStateResultRequest)
    }

    // ::: Function to fetch result of values state..
    fun getValuesStateResultThroughAPI(context: Context) {
        val requestQueue = Volley.newRequestQueue(context)
        val urlToGetValueStateResult = "${Config.baseUrl}/home"
        val getValuesStateResultRequest = JsonObjectRequest(Request.Method.GET, urlToGetValueStateResult, null, {
            emptyAndValuesStateMLD.postValue(it)
        }, {
            emptyAndValuesStateMLD.postValue(null)
        })
        requestQueue.add(getValuesStateResultRequest)
    }
}