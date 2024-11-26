package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

class DonationScreenModel(
    private val donationService: DonationService,
    private val centerService: CenterService
) :
    AppModel<DonationState>(DonationState.Idle) {

    val query = MutableStateFlow("")

    val donations: StateFlow<List<Donation>> = donationService.getDonations()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val centers: StateFlow<List<Center>> = centerService.getCenters()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val filteredCenters: StateFlow<List<Center>> =
        query.combine(centers) { query, centers ->
            val filtered = centers.filter {
                it.name.lowercase().removeDiacritics()
                    .contains(query.lowercase().removeDiacritics()) or
                        it.city.lowercase().removeDiacritics()
                            .contains(query.lowercase().removeDiacritics()) or
                        it.street.lowercase().removeDiacritics().contains(
                            query.lowercase().removeDiacritics()
                        )
            }
            filtered
        }
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), centers.value)

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
        hemoglobin: Int,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean
    ) {
        mutableState.value = DonationState.Saving
        _validateDonation(
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
                        hemoglobin.toFloat(),
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
        center: Center?,
        type: Int,
        hemoglobin: Int,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean
    ) {
        mutableState.value = DonationState.Saving
        _validateDonation(
            amount,
            date,
            center,
            type
        ) {
            screenModelScope.launch {
                center?.let { center ->
                    donationService.updateDonation(
                        id,
                        date,
                        DonationType.byType(type),
                        amount,
                        hemoglobin.toFloat(),
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

    private fun _validateDonation(
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
}

sealed class DonationState {
    data object Idle : DonationState()
    data object Saving : DonationState()
    data object Saved : DonationState()
    data object Deleted : DonationState()
    data class Error(val error: DonationError) : DonationState()
}

enum class DonationError {
    DATE_IN_FUTURE_ERROR,
    AMOUNT_ERROR,
    CENTER_ERROR,
    TYPE_ERROR,
    DELETION_ERROR,
    DONATION_ERROR
}