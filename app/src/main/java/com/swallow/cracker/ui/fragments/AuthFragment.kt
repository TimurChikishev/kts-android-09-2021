package com.swallow.cracker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentAuthBinding
import com.swallow.cracker.ui.viewmodels.AuthViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import com.swallow.cracker.utils.toast
import kotlinx.coroutines.flow.collect
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModel()
    private val viewBinding by viewBinding(FragmentAuthBinding::bind)

    private var buttonAuthByReddit: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewBinding.loginButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.openLoginPage()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loadingStateFlow.collect(::updateIsLoading)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.openAuthPageStateFlow.collect(::openAuthPage)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.toastStateFlow.collect(::toast)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.authSuccessStateFlow.collect {
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
            }
        }
    }

    private fun updateIsLoading(isLoading: Boolean) = with(viewBinding) {
        loginButton.isVisible = !isLoading
        loginProgress.isVisible = isLoading
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTH_REQUEST_CODE && data != null) {
            val tokenExchangeRequest = AuthorizationResponse.fromIntent(data)
                ?.createTokenExchangeRequest()
            val exception = AuthorizationException.fromIntent(data)
            when {
                tokenExchangeRequest != null && exception == null ->
                    viewModel.onAuthCodeReceived(tokenExchangeRequest)
                exception != null ->
                    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                        viewModel.onAuthCodeFailed()
                    }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openAuthPage(intent: Intent) {
        startActivityForResult(intent, AUTH_REQUEST_CODE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        buttonAuthByReddit = null
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 342
    }
}