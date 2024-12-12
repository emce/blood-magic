package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewAmountError
import mobi.cwiklinski.bloodline.resources.donationNewCenterError
import mobi.cwiklinski.bloodline.resources.donationNewDateError
import mobi.cwiklinski.bloodline.resources.donationNewError
import mobi.cwiklinski.bloodline.resources.donationNewTypeError
import mobi.cwiklinski.bloodline.ui.model.DonationError
import org.jetbrains.compose.resources.stringResource

abstract class AppDonationScreen : AppScreen() {

    @Composable
    fun getError(error: DonationError) = stringResource(
        when (error) {
            DonationError.DATE_IN_FUTURE_ERROR -> Res.string.donationNewDateError
            DonationError.AMOUNT_ERROR -> Res.string.donationNewAmountError
            DonationError.CENTER_ERROR -> Res.string.donationNewCenterError
            DonationError.TYPE_ERROR -> Res.string.donationNewTypeError
            else -> Res.string.donationNewError
        }
    )
}