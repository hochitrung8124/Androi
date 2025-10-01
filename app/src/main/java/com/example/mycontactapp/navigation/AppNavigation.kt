package com.example.mycontactapp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycontactapp.ui.screens.ContactDetailScreen
import com.example.mycontactapp.ui.screens.ContactListScreen

// Định nghĩa Routes và Keys cho arguments
object AppDestinations {
    const val CONTACT_LIST_ROUTE = "contact_list"
    const val CONTACT_DETAIL_ROUTE = "contact_detail"
    const val CONTACT_ID_KEY = "contactId"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppDestinations.CONTACT_LIST_ROUTE
    ) {
        composable(AppDestinations.CONTACT_LIST_ROUTE) {
            ContactListScreen(navController = navController)
        }
        composable(
            route = "${AppDestinations.CONTACT_DETAIL_ROUTE}/{${AppDestinations.CONTACT_ID_KEY}}",
            arguments = listOf(navArgument(AppDestinations.CONTACT_ID_KEY) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt(AppDestinations.CONTACT_ID_KEY)
            ContactDetailScreen(navController = navController, contactId = contactId)
        }
    }
}