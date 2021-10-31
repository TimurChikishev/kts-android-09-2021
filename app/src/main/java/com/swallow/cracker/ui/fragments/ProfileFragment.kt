package com.swallow.cracker.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentProfileBinding
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.ui.viewmodels.ProfileViewModel
import com.swallow.cracker.utils.toast
import kotlinx.coroutines.flow.collect

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
    private val profileViewModel: ProfileViewModel by viewModels()
    private var dialogLogout: MaterialAlertDialogBuilder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        bindViewModel()
        initTopAppBar()
        initLogoutDialog()
    }

    private fun initViewModels() {
        profileViewModel.init()
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
                profileViewModel.logout()
            }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted { profileViewModel.logoutFlow.collect(::logout) }

        launchWhenCreated { profileViewModel.toastFlow.collect(::toast) }

        launchWhenCreated { profileViewModel.getProfileInfo() }

        launchWhenCreated { profileViewModel.profileInfoFlow.collect(::setContentProfileHeader) }
    }

    private fun setContentProfileHeader(profile: RedditProfile?) = with(viewBinding) {
        profile ?: return@with

        setBannerImage(profile.bannerImg)
        setAvatarImage(profile)

        includeAppBar.apply {
            nameTextView.text = profile.name
            displayNameTextView.text = profile.displayName
            totalKarmaTextView.text = context?.getString(R.string.karma, profile.totalKarma)
        }
    }

    private fun setBannerImage(bannerImg: String?) = with(viewBinding) {
        Glide.with(this@ProfileFragment)
            .load(bannerImg)
            .error(R.drawable.header_image)
            .into(includeAppBar.bannerImageView)
    }

    private fun setAvatarImage(profile: RedditProfile) = with(viewBinding) {
        Glide.with(this@ProfileFragment)
            .load(profile.avatarImg ?: profile.iconImage)
            .error(R.drawable.ic_account_circle_24)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.isVisible = false
                    containerCoordinatorLayout.isVisible = true
                    return false
                }
            })
            .into(includeAppBar.avatarImageView)
    }

    private fun initTopAppBar() = with(viewBinding.includeAppBar) {
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
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