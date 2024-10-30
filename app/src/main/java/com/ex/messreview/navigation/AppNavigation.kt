@file:OptIn(ExperimentalMaterial3Api::class)

package com.ex.messreview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ex.messreview.Screens.AuthScreen
import com.ex.messreview.Screens.HomeScreen
import com.ex.messreview.Screens.OverallRatingScreen
import com.ex.messreview.Screens.ProfileScreen
import com.ex.messreview.Screens.RatingScreen
import com.ex.messreview.SharedViewModel
import com.ex.messreview.viewmodel.AuthViewModel

// Define route constants for easier reference and management
sealed class Route(val route: String) {
    object Login : Route("login")
    object Home : Route(Screens.StudentHome.name)
    object Rating : Route(Screens.OverallRating.name)
    object Profile : Route(Screens.ProfileScreen.name)
    object RatingDetail : Route("rating_screen/{itemName}/{imageResId}/{itemInfo}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(sharedViewModel: SharedViewModel, authViewModel: AuthViewModel) {
    // Use rememberSaveable to retain NavController state across configuration changes
    val rememberNavController = rememberNavController()
    val navController = remember { rememberNavController }
    val username1 by sharedViewModel.username.observeAsState("User")

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            if (currentDestination?.route != Route.Login.route) {
                BottomNavBar(navController, currentDestination)
            }
        }
    ) { paddingValues: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Route.Login.route) {
                AuthScreen(authViewModel, navController = navController, sharedViewModel)
            }
            composable(Route.Home.route) {
                HomeScreen(navController = navController, sharedViewModel, authViewModel)
            }
            composable(Route.Rating.route) {
                OverallRatingScreen(sharedViewModel)
            }
            composable(Route.Profile.route) {
                ProfileScreen(sharedViewModel, authViewModel, navController = navController)
            }
            composable(
                route = Route.RatingDetail.route,
                arguments = listOf(
                    navArgument("itemName") { type = NavType.StringType },
                    navArgument("imageResId") { type = NavType.IntType },
                    navArgument("itemInfo") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val itemName = backStackEntry.arguments?.getString("itemName")
                val imageResId = backStackEntry.arguments?.getInt("imageResId")
                val itemInfo = backStackEntry.arguments?.getString("itemInfo")
                if (itemName != null && imageResId != null) {
                    RatingScreen(
                        itemName = itemName,
                        imageResId = imageResId,
                        username = username1,
                        itemInfo = itemInfo
                    )
                }
            }
        }
    }
}

// Separate BottomNavBar for improved readability and reuse
@Composable
fun BottomNavBar(navController: NavController, currentDestination: NavDestination?) {
    NavigationBar {
        listOfNavItems.forEach { navItem ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(badge = {}) {
                        Icon(
                            imageVector = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon,
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = navItem.label) }
            )
        }
    }
}


