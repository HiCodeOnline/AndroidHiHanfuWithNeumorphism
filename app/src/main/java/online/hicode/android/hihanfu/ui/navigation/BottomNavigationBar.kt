package online.hicode.android.hihanfu.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import online.hicode.android.hihanfu.neumorphism.LightSource
import online.hicode.android.hihanfu.neumorphism.neu
import online.hicode.android.hihanfu.neumorphism.shape.Flat
import online.hicode.android.hihanfu.neumorphism.shape.RoundedCorner

/**
 * 底部导航栏
 *
 * @author HiCodeOnline
 * @since 2026-07-25
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = backStackEntry.value?.destination



    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .then(
                        if (currentDestination?.route == Screen.HanfuCabinetNav.route) {
                            Modifier.neu(
                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                shadowElevation = 6.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(12.dp)),
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .then(
                        Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate(Screen.HanfuCabinetNav.route) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Screen.HanfuCabinetNav.icon),
                    contentDescription = stringResource(Screen.HanfuCabinetNav.label),
                    tint =  if (currentDestination?.route == Screen.HanfuCabinetNav.route) Color.Unspecified else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .then(
                        if (currentDestination?.route == Screen.JewelryCabinetNav.route) {
                            Modifier.neu(
                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                shadowElevation = 6.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(12.dp)),
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .then(
                        Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate(Screen.JewelryCabinetNav.route) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Screen.JewelryCabinetNav.icon),
                    contentDescription = stringResource(Screen.JewelryCabinetNav.label),
                    tint = if (currentDestination?.route == Screen.JewelryCabinetNav.route) Color.Unspecified else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .then(
                        if (currentDestination?.route == Screen.OutfitNav.route) {
                            Modifier.neu(
                                lightShadowColor = MaterialTheme.colorScheme.tertiary,
                                darkShadowColor = MaterialTheme.colorScheme.surface,
                                shadowElevation = 6.dp,
                                lightSource = LightSource.LEFT_TOP,
                                shape = Flat(RoundedCorner(12.dp)),
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .then(
                        Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate(Screen.OutfitNav.route) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Screen.OutfitNav.icon),
                    contentDescription = stringResource(Screen.OutfitNav.label),
                    tint = if (currentDestination?.route == Screen.OutfitNav.route) Color.Unspecified else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }



}