package mobi.cwiklinski.bloodline

import kotlinx.datetime.LocalDate

object Constants {

    val EMAIL_PATTERN =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val CURRENCY_SYMBOL = "PLN"

    // Reminder
    const val REMINDER_PERIOD_FULL_BLOOD = (8 * 7) + 1
    const val REMINDER_PERIOD_PLASMA = (4 * 7) + 1
    const val REMINDER_PERIOD_PLATELETS = (4 * 7) + 1

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

    // Notifications
    const val CHANNEL_NAME = "BloodMagic"
    const val CHANNEL_ID = "mobi.cwiklinski.bloodline"
    const val NOTIFICATION_ID = 2001

    object DefaultDonationCapacity {
        const val FULL_BLOOD = 450
        const val PLASMA = 600
        const val PLATELETS = 300
        const val PACKED = 300
    }
}