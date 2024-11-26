package mobi.cwiklinski.bloodline.ui.util

import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.nav_icon_home
import mobi.cwiklinski.bloodline.resources.nav_icon_profile
import mobi.cwiklinski.bloodline.resources.nav_icon_text
import org.jetbrains.compose.resources.DrawableResource

enum class BottomNavigationItem(val icon: DrawableResource) {
    HOME(Res.drawable.nav_icon_home),
    LIST(Res.drawable.nav_icon_text),
    PROFILE(Res.drawable.nav_icon_profile);
}