package mobi.cwiklinski.bloodline.ui.util

import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.navCenters
import mobi.cwiklinski.bloodline.resources.navDonations
import mobi.cwiklinski.bloodline.resources.navHome
import mobi.cwiklinski.bloodline.resources.navProfile
import mobi.cwiklinski.bloodline.resources.nav_icon_center
import mobi.cwiklinski.bloodline.resources.nav_icon_home
import mobi.cwiklinski.bloodline.resources.nav_icon_profile
import mobi.cwiklinski.bloodline.resources.nav_icon_text
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class NavigationItem(val icon: DrawableResource, val title: StringResource) {
    HOME(Res.drawable.nav_icon_home, Res.string.navHome),
    LIST(Res.drawable.nav_icon_text, Res.string.navDonations),
    CENTER(Res.drawable.nav_icon_center, Res.string.navCenters),
    PROFILE(Res.drawable.nav_icon_profile, Res.string.navProfile);
}