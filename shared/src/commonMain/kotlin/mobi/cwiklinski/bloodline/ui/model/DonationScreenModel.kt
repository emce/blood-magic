package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

class DonationScreenModel(
    callbackManager: CallbackManager,
    private val donationService: DonationService,
    centerService: CenterService,
    profileService: ProfileService
) :
    AppModel<DonationState>(DonationState.Idle, callbackManager) {

    val query = MutableStateFlow("")

    val donations = donationService.getDonations()

    val centers = centerService.getCenters()

    val profile = profileService.getProfile()

    val filteredCenters =
        combine(centers, query) { centers, query ->
            centers.filter {
                it.name.lowercase().removeDiacritics()
                    .contains(query.lowercase().removeDiacritics()) or
                        it.city.lowercase().removeDiacritics()
                            .contains(query.lowercase().removeDiacritics()) or
                        it.street.lowercase().removeDiacritics().contains(
                            query.lowercase().removeDiacritics()
                        )
            }
        }

    init {
        bootstrap()
    }

    fun deleteDonation(donation: Donation) {
        mutableState.value = DonationState.Saving
        screenModelScope.launch {
            donationService.deleteDonation(donation.id)
                .collectLatest {
                    when (it) {
                        is Either.Left -> {
                            mutableState.value = DonationState.Deleted
                        }

                        is Either.Right -> {
                            mutableState.value = DonationState.Error(DonationError.DELETION_ERROR)
                        }
                    }
                }
        }
    }

    fun addDonation(
        amount: Int,
        date: LocalDate,
        center: Center?,
        type: Int,
        hemoglobin: Float = 0f,
        systolic: Int = 0,
        diastolic: Int = 0,
        disqualification: Boolean = false
    ) {
        mutableState.value = DonationState.Saving
        validateDonation(
            amount,
            date,
            center,
            type
        ) {
            screenModelScope.launch {
                center?.let { center ->
                    donationService.addDonation(
                        date,
                        DonationType.byType(type),
                        amount,
                        hemoglobin,
                        systolic,
                        diastolic,
                        disqualification,
                        center
                    )
                        .collectLatest {
                            when (it) {
                                is Either.Left -> {
                                    mutableState.value = DonationState.Saved
                                }
                                is Either.Right -> {
                                    mutableState.value =
                                        DonationState.Error(DonationError.DONATION_ERROR)
                                }
                            }
                        }
                }
            }
        }
    }

    fun updateDonation(
        id: String,
        amount: Int,
        date: LocalDate,
        center: Center,
        type: Int,
        hemoglobin: Float = 0f,
        systolic: Int = 0,
        diastolic: Int = 0,
        disqualification: Boolean
    ) {
        mutableState.value = DonationState.Saving
        validateDonation(
            amount,
            date,
            center,
            type
        ) {
            screenModelScope.launch {
                donationService.updateDonation(
                    id,
                    date,
                    DonationType.byType(type),
                    amount,
                    hemoglobin,
                    systolic,
                    diastolic,
                    disqualification,
                    center
                )
                    .collectLatest {
                        when (it) {
                            is Either.Left -> {
                                mutableState.value = DonationState.Saved
                            }

                            is Either.Right -> {
                                mutableState.value =
                                    DonationState.Error(DonationError.DONATION_ERROR)
                            }
                        }
                    }
                }
        }
    }

    private fun validateDonation(
        amount: Int,
        date: LocalDate,
        center: Center?,
        type: Int,
        follow: () -> Unit
    ) {
        if (amount > 0) {
            if (!date.isAfter(today())) {
                if (center != null) {
                    if (DonationType.entries.firstOrNull { it.type == type } != null) {
                        follow.invoke()
                    } else {
                        mutableState.value = DonationState.Error(DonationError.TYPE_ERROR)
                    }
                } else {
                    mutableState.value = DonationState.Error(DonationError.CENTER_ERROR)
                }
            } else {
                mutableState.value = DonationState.Error(DonationError.DATE_IN_FUTURE_ERROR)
            }
        } else {
            mutableState.value = DonationState.Error(DonationError.AMOUNT_ERROR)
        }
    }

    fun clearError() {
        mutableState.value = DonationState.Idle
    }

    fun markToDelete(donation: Donation) {
        mutableState.value = DonationState.ToDelete(donation)
    }
}

sealed class DonationState {
    data object Idle : DonationState()
    data object Saving : DonationState()
    data object Saved : DonationState()
    data class ToDelete(val donation: Donation) : DonationState()
    data object Deleted : DonationState()
    data class Error(val error: DonationError) : DonationState()
}

enum class DonationError {
    DATE_IN_FUTURE_ERROR,
    AMOUNT_ERROR,
    CENTER_ERROR,
    TYPE_ERROR,
    PRESSURE_ERROR,
    HEMOGLOBIN_ERROR,
    DELETION_ERROR,
    DONATION_ERROR
}