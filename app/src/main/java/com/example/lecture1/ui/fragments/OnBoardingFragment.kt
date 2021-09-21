package com.example.lecture1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lecture1.R
import com.example.lecture1.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

//    private var viewBinding by viewBinding(FragmentOnBoardingBinding::bind)
    // Without reflection
    private val viewBinding by viewBinding(FragmentOnBoardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnSkip.setOnClickListener {
            val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLogInFragment()
            findNavController().navigate(action)
        }
    }
}