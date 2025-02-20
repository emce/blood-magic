package mobi.cwiklinski.bloodline

import kotlinx.datetime.LocalDate

object Constants {

    // Period after donation
    const val PERIOD_FULL_BLOOD_FULL_BLOOD = (8 * 7) + 1
    const val PERIOD_FULL_BLOOD_PLASMA = (2 * 7) + 1
    const val PERIOD_FULL_BLOOD_PLATELETS = (4 * 7) + 1
    const val PERIOD_PLASMA_FULL_BLOOD = (4 * 7) + 1
    const val PERIOD_PLASMA_PLASMA = (2 * 7) + 1
    const val PERIOD_PLASMA_PLATELETS = (4 * 7) + 1
    const val PERIOD_PLATELETS_FULL_BLOOD = (8 * 7) + 1
    const val PERIOD_PLATELETS_PLASMA = (4 * 7) + 1
    const val PERIOD_PLATELETS_PLATELETS = (4 * 7) + 1

    // Relief
    const val TAX_RELIEF_ONE_LITER_AMOUNT = 130
    val PERIOD_FIRST: LocalDate = LocalDate(1998, 10, 14)
    val PERIOD_SECOND: LocalDate = LocalDate(2006, 2, 23)

    // Badges
    const val BADGE_FEMALE_FIRST = 5000
    const val BADGE_MALE_FIRST = 6000
    const val BADGE_FINAL = 20000

    // Storage
    const val EMAIL_KEY = "email"
    const val NOTIFICATIONS_LATEST = "notifications_latest"

    // Centers
    const val CENTER_URL = "https://krwiodawca.cwiklin.ski/storage/images/center/%id.jpg"
    const val MAP_URL = "https://krwiodawca.cwiklin.ski/storage/images/maps/%id.png"

    // Animations
    const val ANIMATION_SPEED = 1000

    // Analytics
    const val ANALYTICS_NO_VALUE = ""
    const val ANALYTICS_KEY_SECTION = "section"
    const val ANALYTICS_KEY_SUB_SECTION = "subsection"
    const val ANALYTICS_KEY_SUB_SUB_SECTION = "subsubsection"

    const val ANALYTICS_SCREEN_ABOUT = "AboutScreen"
    const val ANALYTICS_SCREEN_CENTER = "CenterScreen"
    const val ANALYTICS_SCREEN_CENTERS = "CentersScreen"
    const val ANALYTICS_SCREEN_DELETE = "DeleteScreen"
    const val ANALYTICS_SCREEN_DONATIONS = "DonationsScreen"
    const val ANALYTICS_SCREEN_EDIT_DONATION = "EditDonationScreen"
    const val ANALYTICS_SCREEN_HOME = "HomeScreen"
    const val ANALYTICS_SCREEN_LOGIN = "LoginScreen"
    const val ANALYTICS_SCREEN_LOGOUT = "LogoutScreen"
    const val ANALYTICS_SCREEN_NEW_DONATION = "NewDonationScreen"
    const val ANALYTICS_SCREEN_NOTIFICATIONS = "NotificationsScreen"
    const val ANALYTICS_SCREEN_PROFILE_AVATAR = "ProfileAvatarScreen"
    const val ANALYTICS_SCREEN_PROFILE_DELETE = "ProfileDeleteScreen"
    const val ANALYTICS_SCREEN_PROFILE_PASSWORD = "ProfilePasswordScreen"
    const val ANALYTICS_SCREEN_PROFILE = "ProfileScreen"
    const val ANALYTICS_SCREEN_REGISTER = "RegisterScreen"
    const val ANALYTICS_SCREEN__RESET = "ResetScreen"
    const val ANALYTICS_SCREEN_SETUP = "SetupScreen"
    const val ANALYTICS_SCREEN_SPLASH = "SplashScreen"

    const val ANALYTICS_ACTION_ACCOUNT_DELETED = "ActionAccountDeleted"

}