package com.example.crypto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crypto.activity.MainActivity
import com.example.crypto.R
import com.example.crypto.databinding.FragmentHomeBinding
import com.example.crypto.utils.Constant
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {

    private var activity: MainActivity? = null
    private var homeBinding: FragmentHomeBinding? = null
    private lateinit var emptyStateMBtn: MaterialButton
    private lateinit var valuesStateMBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        activity = getActivity() as MainActivity
        activity?.apply {
            supportActionBar?.title = requireContext().getString(R.string.home)
        }
        initializeVariablesAndReferences()
        initializeClickActions()
        return homeBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            supportActionBar?.title = requireContext().getString(R.string.home)
        }
    }

    // ::: Function to initialize all the views and variables..
    private fun initializeVariablesAndReferences() {
        emptyStateMBtn = homeBinding!!.idMBEmptyState
        valuesStateMBtn = homeBinding!!.idMBValuesState
    }

    // ::: Function to initialize all the click actions..
    private fun initializeClickActions() {
        emptyStateMBtn.setOnClickListener {
            activity?.replaceFCVWithFragments(EmptyOrValuesStateFragment().apply {
                arguments = Bundle().apply {
                    putString(Constant.isCalledFor, Constant.emptyState)
                }
            }, isNeedToAddOnStack = true)
        }

        valuesStateMBtn.setOnClickListener {
            activity?.replaceFCVWithFragments(EmptyOrValuesStateFragment().apply {
                arguments = Bundle().apply {
                    putString(Constant.isCalledFor, Constant.valuesState)
                }
            }, isNeedToAddOnStack = true)
        }
    }
}