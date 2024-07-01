package com.ex.messreview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.ex.messreview.Screens.*
import com.ex.messreview.SharedViewModel
import androidx.compose.runtime.livedata.observeAsState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController: NavHostController = rememberNavController()
    val username1 by sharedViewModel.username.observeAsState("User")
    Scaffold(
        bottomBar = {
            val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
            val currentDestination: NavDestination? = navBackStackEntry?.destination
            val selectedItemIndex = listOfNavItems.indexOfFirst { it.route == currentDestination?.route }

            if (currentDestination?.route != "login") {
                NavigationBar {
                    listOfNavItems.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
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
                                        imageVector = if (index == selectedItemIndex) navItem.selectedIcon else navItem.unselectedIcon,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = { Text(text = navItem.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                AuthScreen { username, password ->
                    sharedViewModel.login(username)
                    navController.navigate(Screens.StudentHome.name) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            composable(route = Screens.StudentHome.name) {
                HomeScreen(navController = navController,sharedViewModel)
            }
            composable(route = Screens.OverallRating.name) {
                OverallRatingScreen(sharedViewModel)
            }
            composable(route = Screens.ProfileScreen.name) {
                ProfileScreen(sharedViewModel)
            }
            composable(
                route = "rating_screen/{itemName}/{imageResId}/{itemInfo}",
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
                    RatingScreen(itemName = itemName, imageResId = imageResId,username=username1,itemInfo=itemInfo)
                }
            }
        }
    }
}


