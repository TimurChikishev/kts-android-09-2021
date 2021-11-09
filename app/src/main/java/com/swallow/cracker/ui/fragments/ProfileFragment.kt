package com.swallow.cracker.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentProfileBinding
import com.swallow.cracker.ui.adapters.ProfileFragmentAdapter
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.ui.viewmodels.ProfileViewModel
import com.swallow.cracker.ui.viewmodels.SharedProfileViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import com.swallow.cracker.utils.toast
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)
    private val profileViewModel: ProfileViewModel by viewModel()
    private val sharedViewModel: SharedProfileViewModel by activityViewModels()
    private var dialogLogout: MaterialAlertDialogBuilder? = null

    companion object {
        private val tabTitles = listOf("About")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        initViewModels()
        bindViewModel()
        initTopAppBar()
        initLogoutDialog()
        initTabBar()
    }

    private fun initTabBar() = with(viewBinding) {
        viewPager.adapter = ProfileFragmentAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(includeAppBar.headerTabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, false)
        }.attach()
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

        sharedViewModel.setProfileInfo(profile)
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
            .error(R.drawable.ic_user_24)
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