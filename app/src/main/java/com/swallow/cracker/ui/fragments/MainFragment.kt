package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentMainBinding
import com.swallow.cracker.ui.adapters.MainFragmentAdapter
import com.swallow.cracker.ui.model.*
import com.swallow.cracker.ui.viewmodels.NetworkStatusViewModel
import com.swallow.cracker.ui.viewmodels.ProfileViewModel
import com.swallow.cracker.utils.*
import kotlinx.coroutines.flow.collect

class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewBinding by viewBinding(FragmentMainBinding::bind)
    private val profileViewModel: ProfileViewModel by viewModels()
    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    private var noInternetSnackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initViewModels()
        initSnackBar()
        bindingViewModel()
        initTopAppBar()
        initTabBar()
        initBottomNavigationView()
        initNavigationView()
    }

    private fun initBottomNavigationView() {
        viewBinding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.subscribePageAction -> true
                else -> true //  R.id.mainPageAction
            }
        }
    }

    private fun initTabBar() = with(viewBinding) {
        viewPager.adapter = MainFragmentAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(includeAppBar.headerTabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun initViewModels() {
        profileViewModel.init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar_home, menu)
    }

    private fun initTopAppBar() = with(viewBinding) {
        (activity as AppCompatActivity).setSupportActionBar(includeAppBar.topAppBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        includeAppBar.topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        includeAppBar.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sortedAction -> false // TODO: sorted dialog
                R.id.searchAction -> {
                    navigateToSearchFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun initNavigationView() = with(viewBinding) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileAction -> {
                    navigateToProfileFragment()
                    drawerLayout.close()
                    true
                }
                else -> {
                    drawerLayout.close()
                    false
                }
            }
        }
    }

    private fun initSnackBar() = with(viewBinding) {
        noInternetSnackBar = getNoInternetConnectionSnackBar(root)
        networkStatusViewModel.checkNetworkState()
    }

    private fun bindingViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted { networkStatusViewModel.isNoNetwork.collect(::showNetworkState) }

        launchWhenCreated { profileViewModel.getProfileInfo() }

        launchWhenCreated { profileViewModel.profileInfoFlow.collect(::setContentProfileHeader) }
    }

    private fun setContentProfileHeader(profile: RedditProfile?) = with(viewBinding) {
        val header = navigationView.getHeaderView(0)
        val avatarIcon = header.findViewById<ImageView>(R.id.profileIconImageView)
        header.findViewById<TextView>(R.id.profileNameTextView).text = profile?.name
        header.findViewById<TextView>(R.id.profileDisplayNameTextView).text = profile?.displayName

        header.setOnClickListener {
            navigateToProfileFragment()
            drawerLayout.close()
        }

        Glide.with(this@MainFragment)
            .load(profile?.avatarImg ?: profile?.iconImage)
            .error(R.drawable.ic_account_circle_24)
            .into(avatarIcon)
    }

    private fun showNetworkState(isNoInternet: Boolean) = when (isNoInternet) {
        true -> noInternetSnackBar?.show()
        false -> noInternetSnackBar?.dismiss()
    }

    private fun navigateToProfileFragment() {
        val action = MainFragmentDirections.actionMainFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    private fun navigateToSearchFragment() {
        val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noInternetSnackBar = null
    }

    companion object {
        val tabTitles = listOf("Home", "Popular")
    }
}