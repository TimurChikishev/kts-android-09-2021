package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentProfileBinding
import com.swallow.cracker.ui.viewmodels.ProfileViewModel
import com.swallow.cracker.utils.toast
import kotlinx.coroutines.flow.collect


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private var dialogLogout: MaterialAlertDialogBuilder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initTopAppBar()
        initLogoutDialog()
    }

    private fun initLogoutDialog() {
        dialogLogout = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Theme_MaterialComponents_Dialog_Alert
        )
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_dialog_text)
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.logout()
            }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted { viewModel.logout.collect(::logout) }

        launchWhenCreated { viewModel.toastStateFlow.collect(::toast) }
    }

    private fun initTopAppBar() {
        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    dialogLogout?.show()
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToAuthFragment() {
        val action = ProfileFragmentDirections.actionProfileFragmentToAuthFragment()
        findNavController().navigate(action)
    }

    private fun logout(logout: Boolean) {
        if (logout) navigateToAuthFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogLogout = null
    }
}