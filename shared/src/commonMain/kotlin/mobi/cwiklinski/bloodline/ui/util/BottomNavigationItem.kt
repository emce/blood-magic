package mobi.cwiklinski.bloodline.ui.util

import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centersTitle
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_center
import mobi.cwiklinski.bloodline.resources.nav_icon_home
import mobi.cwiklinski.bloodline.resources.nav_icon_profile
import mobi.cwiklinski.bloodline.resources.nav_icon_text
import mobi.cwiklinski.bloodline.resources.profileTitle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class BottomNavigationItem(val icon: DrawableResource, val title: StringResource) {
    HOME(Res.drawable.nav_icon_home, Res.string.homeTitle),
    LIST(Res.drawable.nav_icon_text, Res.string.donationsTitle),
    CENTER(Res.drawable.nav_icon_center, Res.string.centersTitle),
    PROFILE(Res.drawable.nav_icon_profile, Res.string.profileTitle);
}