package com.example.lecture1.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lecture1.R
import com.example.lecture1.databinding.FragmentLogInBinding
import com.example.lecture1.databinding.FragmentOnBoardingBinding
import com.example.lecture1.ui.viewmodels.AuthViewModal
import com.example.lecture1.ui.viewmodels.LoginState
import com.google.android.material.textfield.TextInputEditText

class LogInFragment : Fragment(R.layout.fragment_log_in) {

    private val viewModel: AuthViewModal by viewModels()
    private val viewBinding by viewBinding(FragmentLogInBinding::bind)

    private var inputEmail: TextInputEditText? = null
    private var inputPassword: TextInputEditText? = null
    private var btnLogin: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogInFrom()

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when(state) {
                is LoginState.LoginSuccessfulState -> {
                    val action = LogInFragmentDirections.actionLogInFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is LoginState.ErrorState<*>-> {
                    when (state.message){
                        is Int -> Toast.makeText(context, getString(state.message) , Toast.LENGTH_SHORT).show()
                        is String -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                    btnLogin?.isEnabled = false
                }
                is LoginState.DefaultState -> {
                    btnLogin?.isEnabled = false
                }
                is LoginState.LoginButtonEnable -> {
                    btnLogin?.isEnabled = true
                }
            }
        })
    }

    private fun initLogInFrom() {
        inputEmail = viewBinding.inputEmail
        inputPassword = viewBinding.inputPassword
        btnLogin = viewBinding.btnLogIn

        btnLogin?.setOnClickListener {
            viewModel.login(email = inputEmail?.text.toString(), password = inputPassword?.text.toString())
        }

        inputEmail?.doAfterTextChanged { viewModel.isValidate(email = inputEmail?.text.toString(), password = inputPassword?.text.toString()) }

        inputPassword?.doAfterTextChanged { viewModel.isValidate(email = inputEmail?.text.toString(), password = inputPassword?.text.toString()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        inputEmail = null
        inputPassword = null
        btnLogin = null
    }
}