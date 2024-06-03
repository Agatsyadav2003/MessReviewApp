package com.ex.messreview.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label : String,
    val selectedIcon  : ImageVector,
    val unselectedIcon  : ImageVector,
    val route : String
)

val listOfNavItems: List<NavItem> = listOf(
    NavItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Screens.StudentHome.name
    ),
    NavItem(
        label = "Rating",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarBorder,
        route = Screens.OverallRating.name
    ),
    NavItem(
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person2,
        route = Screens.ProfileScreen.name
    )

)