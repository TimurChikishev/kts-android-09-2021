package com.swallow.cracker.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.swallow.cracker.R
import com.swallow.cracker.databinding.ActivityMainBinding
import com.swallow.cracker.utils.setupWithNavController
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentNavController: LiveData<NavController>? = null

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            Timber.d("controller: $controller, destination: $destination, arguments: $arguments")
            Timber.d("controller graph: ${controller.graph}")

            // if you need to show/hide bottom nav or toolbar based on destination
            // binding.bottomNavigationView.isVisible = destination.id != R.id.itemDetail
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setUpBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigationBar()
    }

    private fun setUpBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.navigation,
            R.navigation.nav_tab_subscriptions
        )

        val controller = binding.bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.navHostContainer,
            intent = intent
        )

        controller.observe(this) { navController ->
            // unregister old onDestinationChangedListener, if it exists
            currentNavController?.value?.removeOnDestinationChangedListener(
                onDestinationChangedListener
            )

            // add onDestinationChangedListener to the new NavController
            navController.addOnDestinationChangedListener(onDestinationChangedListener)
        }

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}