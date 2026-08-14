package online.hicode.android.hihanfu.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.hicode.android.hihanfu.ui.screen.HanfuCabinetScreen
import online.hicode.android.hihanfu.ui.screen.HanfuCabinetViewModel
import online.hicode.android.hihanfu.ui.screen.HanfuScreen
import online.hicode.android.hihanfu.ui.screen.HanfuViewModel
import online.hicode.android.hihanfu.ui.screen.JewelryCabinetScreen
import online.hicode.android.hihanfu.ui.screen.JewelryCabinetViewModel
import online.hicode.android.hihanfu.ui.screen.JewelryScreen
import online.hicode.android.hihanfu.ui.screen.JewelryViewModel
import online.hicode.android.hihanfu.ui.screen.OutfitScreen
import online.hicode.android.hihanfu.ui.screen.OutfitViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.HanfuCabinetNav.route,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            route = Screen.HanfuCabinetNav.route,
        ) {
            val viewModel: HanfuCabinetViewModel = hiltViewModel()
            HanfuCabinetScreen(navController, viewModel)
        }
        composable(
            route = Screen.JewelryCabinetNav.route,
        ) {
            val viewModel: JewelryCabinetViewModel = hiltViewModel()
            JewelryCabinetScreen(navController, viewModel)
        }
        composable(
            route = Screen.OutfitNav.route,
        ) {
            val viewModel: OutfitViewModel = hiltViewModel()
            OutfitScreen(navController, viewModel)
        }
        composable(
            route = Screen.HanfuNav.route.plus("/{cabinetId}"),
            arguments = listOf(navArgument("cabinetId") { type = NavType.LongType }),
            enterTransition = { scaleIn(tween(300), initialScale = 0.9f) + fadeIn(tween(300)) },
            exitTransition = { scaleOut(tween(300), targetScale = 1.1f) + fadeOut(tween(300)) }
        ) { backStackEntry ->
            val viewModel: HanfuViewModel = hiltViewModel()
            val cabinetId = backStackEntry.arguments?.getLong("cabinetId") ?: 0L
            HanfuScreen(cabinetId, navController, hanfuViewModel = viewModel)
        }
        composable(
            route = Screen.JewelryNav.route.plus("/{cabinetId}"),
            arguments = listOf(navArgument("cabinetId") { type = NavType.LongType }),
            enterTransition = { scaleIn(tween(300), initialScale = 0.9f) + fadeIn(tween(300)) },
            exitTransition = { scaleOut(tween(300), targetScale = 1.1f) + fadeOut(tween(300)) }
        ) { backStackEntry ->
            val viewModel: JewelryViewModel = hiltViewModel()
            val cabinetId = backStackEntry.arguments?.getLong("cabinetId") ?: 0L
            JewelryScreen(cabinetId, navController, viewModel)
        }
    }

}