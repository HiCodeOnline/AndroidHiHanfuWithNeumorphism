package online.hicode.android.hihanfu.ui.navigation

import androidx.compose.material3.Text
import kotlinx.serialization.Serializable
import online.hicode.android.hihanfu.R

@Serializable
sealed class Screen (
    val route: String,
    val label: Int,
    val icon: Int
) {
    @Serializable
    object HanfuCabinetNav : Screen(
        route = "hanfu_cabinet",
        label = R.string.nav_hanfu_cabinet,
        icon = R.drawable.hanfu
    )

    @Serializable
    object HanfuNav : Screen(
        route = "hanfu",
        label = R.string.nav_hanfu,
        icon = R.drawable.hanfu
    )

    @Serializable
    object JewelryCabinetNav : Screen(
        route = "jewelry_cabinet",
        label = R.string.nav_jewelry_cabinet,
        icon = R.drawable.jewelry
    )

    @Serializable
    object JewelryNav : Screen(
        route = "jewelry",
        label = R.string.nav_jewelry,
        icon = R.drawable.jewelry
    )

    @Serializable
    object OutfitNav : Screen(
        route = "outfit",
        label = R.string.nav_outfit,
        icon = R.drawable.outfit
    )
}