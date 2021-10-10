package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentLogInBinding
import com.swallow.cracker.ui.viewmodels.AuthViewModel
import com.swallow.cracker.ui.viewmodels.LoginState
import com.google.android.material.textfield.TextInputEditText

class LogInFragment : Fragment(R.layout.fragment_log_in) {

    private val viewModel: AuthViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentLogInBinding::bind)

    private var emailTextInputEditText: TextInputEditText? = null
    private var passwordTextInputEditText: TextInputEditText? = null
    private var buttonLogin: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogInFrom()

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is LoginState.LoginSuccessfulState -> {
                    val action = LogInFragmentDirections.actionLogInFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is LoginState.ErrorState<*> -> {
                    when (state.message) {
                        is Int -> Toast.makeText(
                            context,
                            getString(state.message),
                            Toast.LENGTH_SHORT
                        ).show()
                        is String -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    buttonLogin?.isEnabled = false
                }
                is LoginState.DefaultState -> {
                    buttonLogin?.isEnabled = false
                }
                is LoginState.LoginButtonEnable -> {
                    buttonLogin?.isEnabled = true
                }
            }
        })
    }

    private fun initLogInFrom() {
        emailTextInputEditText = viewBinding.emailTextInputEditText
        passwordTextInputEditText = viewBinding.passwordTextInputEditText
        buttonLogin = viewBinding.buttonLogIn

        buttonLogin?.setOnClickListener {
            viewModel.login(
                email = emailTextInputEditText?.text.toString(),
                password = passwordTextInputEditText?.text.toString()
            )
        }

        emailTextInputEditText?.doAfterTextChanged {
            viewModel.isValidate(
                email = emailTextInputEditText?.text.toString(),
                password = passwordTextInputEditText?.text.toString()
            )
        }

        passwordTextInputEditText?.doAfterTextChanged {
            viewModel.isValidate(
                email = emailTextInputEditText?.text.toString(),
                password = passwordTextInputEditText?.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emailTextInputEditText = null
        passwordTextInputEditText = null
        buttonLogin = null
    }
}