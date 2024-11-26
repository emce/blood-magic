package mobi.cwiklinski.bloodline.ui.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Donation

class NextDonationTime(val donation: Donation?, val today: LocalDate) {

    private var donationNextDate = DonationNextDate(-1, -1, -1)

    init {
        donation?.let {
            when (it.type) {
                DonationType.PLASMA -> setPlasmaDonation(it)
                DonationType.PLATELETS -> setPlateletsDonation(it)
                else -> setFullBloodDonation(it)
            }
        }
    }

    private fun getDaysPassed(donation: Donation) = donation.date.daysUntil(today)

    private fun setFullBloodDonation(donation: Donation) {
        val daysPassed = getDaysPassed(donation)
        donationNextDate = DonationNextDate(
            getRemaining(Constants.PERIOD_FULL_BLOOD_FULL_BLOOD, daysPassed),
            getRemaining(Constants.PERIOD_FULL_BLOOD_PLASMA, daysPassed),
            getRemaining(Constants.PERIOD_FULL_BLOOD_PLATELETS, daysPassed)
        )
    }

    private fun setPlasmaDonation(donation: Donation) {
        val daysPassed = getDaysPassed(donation)
        donationNextDate = DonationNextDate(
            getRemaining(Constants.PERIOD_PLASMA_FULL_BLOOD, daysPassed),
            getRemaining(Constants.PERIOD_PLASMA_PLASMA, daysPassed),
            getRemaining(Constants.PERIOD_PLASMA_PLATELETS, daysPassed)
        )
    }

    private fun setPlateletsDonation(donation: Donation) {
        val daysPassed = getDaysPassed(donation)
        donationNextDate = DonationNextDate(
            getRemaining(Constants.PERIOD_PLATELETS_FULL_BLOOD, daysPassed),
            getRemaining(Constants.PERIOD_PLATELETS_PLASMA, daysPassed),
            getRemaining(Constants.PERIOD_PLATELETS_PLATELETS, daysPassed)
        )
    }

    private fun getRemaining(period: Int, passed: Int): Int {
        var result = 0
        if (passed < period) {
            result = period - passed
        }
        return result
    }

    fun fillData(nextDate: (nextDonationDate: DonationNextDate) -> Unit) {
        nextDate.invoke(donationNextDate)
    }

    fun getNextDate() = donationNextDate

    fun getPotentialDonations() = when {
        donationNextDate.fullBlood == 0 && donationNextDate.platelets == 0 && donationNextDate.plasma == 0 -> {
            PotentialDonation.ALL
        }

        donationNextDate.fullBlood == 0 && donationNextDate.platelets == 0 && donationNextDate.plasma > 0 -> {
            PotentialDonation.FULL_BLOOD_PLATELETS
        }

        donationNextDate.fullBlood == 0 && donationNextDate.platelets > 0 && donationNextDate.plasma == 0 -> {
            PotentialDonation.FULL_BLOOD_PLASMA
        }

        donationNextDate.fullBlood == 0 && donationNextDate.platelets > 0 && donationNextDate.plasma > 0 -> {
            PotentialDonation.FULL_BLOOD
        }

        donationNextDate.fullBlood > 0 && donationNextDate.platelets == 0 && donationNextDate.plasma > 0 -> {
            PotentialDonation.PLATELETS
        }

        donationNextDate.fullBlood > 0 && donationNextDate.platelets == 0 && donationNextDate.plasma == 0 -> {
            PotentialDonation.PLASMA_PLATELETS
        }

        donationNextDate.fullBlood > 0 && donationNextDate.platelets > 0 && donationNextDate.plasma == 0 -> {
            PotentialDonation.PLASMA
        }

        else -> PotentialDonation.NONE
    }
}

class DonationNextDate(val fullBlood: Int, val plasma: Int, val platelets: Int)

enum class PotentialDonation {
    ALL,
    FULL_BLOOD,
    PLASMA,
    PLATELETS,
    FULL_BLOOD_PLASMA,
    FULL_BLOOD_PLATELETS,
    PLASMA_PLATELETS,
    NONE
}