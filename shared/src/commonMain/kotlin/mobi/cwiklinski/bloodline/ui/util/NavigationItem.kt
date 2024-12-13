package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.ui.graphics.Color
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.ic_notification_standard
import mobi.cwiklinski.bloodline.resources.ic_notification_urgent
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.navCenters
import mobi.cwiklinski.bloodline.resources.navDonations
import mobi.cwiklinski.bloodline.resources.navHome
import mobi.cwiklinski.bloodline.resources.navProfile
import mobi.cwiklinski.bloodline.resources.nav_icon_center
import mobi.cwiklinski.bloodline.resources.nav_icon_home
import mobi.cwiklinski.bloodline.resources.nav_icon_profile
import mobi.cwiklinski.bloodline.resources.nav_icon_text
import mobi.cwiklinski.bloodline.resources.notificationsReadTabTitle
import mobi.cwiklinski.bloodline.resources.notificationsTypeStandard
import mobi.cwiklinski.bloodline.resources.notificationsTypeUrgent
import mobi.cwiklinski.bloodline.resources.notificationsUnreadTabTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class NavigationItem(val icon: DrawableResource, val title: StringResource) {
    HOME(Res.drawable.nav_icon_home, Res.string.navHome),
    LIST(Res.drawable.nav_icon_text, Res.string.navDonations),
    CENTER(Res.drawable.nav_icon_center, Res.string.navCenters),
    PROFILE(Res.drawable.nav_icon_profile, Res.string.navProfile),
    OTHER(Res.drawable.icon_close, Res.string.appName)
}

enum class NotificationTab(val title: StringResource) {
    UNREAD(Res.string.notificationsUnreadTabTitle),
    ALREADY_READ(Res.string.notificationsReadTabTitle)
}

enum class NavigationType(
    val type: Int,
    val color: Color,
    val icon: DrawableResource,
    val description: StringResource
) {
    STANDARD(
        1,
        AppThemeColors.background,
        Res.drawable.ic_notification_standard,
        Res.string.notificationsTypeStandard
    ),
    URGENT(
        2,
        AppThemeColors.alertBackground,
        Res.drawable.ic_notification_urgent,
        Res.string.notificationsTypeUrgent
    );

    companion object {
        fun fromType(type: Int) = NavigationType.entries.firstOrNull { it.type == type } ?: STANDARD
    }
}