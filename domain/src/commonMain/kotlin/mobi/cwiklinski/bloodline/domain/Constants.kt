package mobi.cwiklinski.bloodline.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.Constants.DONATION_TYPE_FULL_BLOOD
import mobi.cwiklinski.bloodline.domain.Constants.DONATION_TYPE_PACKED_CELLS
import mobi.cwiklinski.bloodline.domain.Constants.DONATION_TYPE_PLASMA
import mobi.cwiklinski.bloodline.domain.Constants.DONATION_TYPE_PLATELETS

object Constants {

    val EMAIL_PATTERN =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val CURRENCY_SYMBOL = "PLN"

    // Donations
    const val DONATION_TYPE_FULL_BLOOD = 1
    const val DONATION_TYPE_PLASMA = 2
    const val DONATION_TYPE_PLATELETS = 3
    const val DONATION_TYPE_PACKED_CELLS = 4

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

    // Discount
    const val COMMUNICATION_DISCOUNT = "\\d{1,3}%"

    // Notifications
    const val CHANNEL_NAME = "BloodLine"
    const val CHANNEL_ID = "mobi.cwiklinski.bloodline"
    const val NOTIFICATION_ID = 2001

    // Firebase
    const val FIREBASE_URL = "https://bloodline-d9107.firebaseio.com"

    const val POLISH_DIACRITCS = "aąbcćdeęfghijklłmnńoópqrsśtvwxyzźż"

    object DefaultDonationCapacity {
        const val FULL_BLOOD = 450
        const val PLASMA = 600
        const val PLATELETS = 300
        const val PACKED = 300
    }

    object Extras {
        const val KEY_SETTINGS = "settings"
        const val KEY_PROFILE = "profile"
    }

    @Serializable
    enum class Sex(val sex: String) {
        MALE("m"),
        FEMALE("f");

        fun isFemale() = this == FEMALE

        companion object {
            fun fromSex(sex: String) = Sex.entries.first { it.sex == sex }
        }
    }
}

enum class DonationType(val type: Int) {
    FULL_BLOOD(DONATION_TYPE_FULL_BLOOD),
    PLASMA(DONATION_TYPE_PLASMA),
    PLATELETS(DONATION_TYPE_PLATELETS),
    PACKED_CELLS(DONATION_TYPE_PACKED_CELLS);

    companion object {
        fun byType(type: Int) =
            entries.firstOrNull { it.type == type } ?: DonationType.FULL_BLOOD
    }
}