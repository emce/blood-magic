package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.domain.model.Donation
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DonationServiceImplementationTest {

    private val donationService: DonationService = DonationServiceImplementation()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `returns donation list`() = runTest {
        val donations = donationService.getDonations().first()
        assertEquals(DummyData.DONATIONS.size, donations.size)
    }

    @Test
    fun `returns donation`() = runTest {
        val desiredDonation = DummyData.DONATIONS.toList().random()
        val donation = donationService.getDonation(desiredDonation.id).first()
        assertEquals(desiredDonation.id, donation.id)
        assertEquals(desiredDonation.center.name, donation.center.name)
        assertEquals(desiredDonation.amount, donation.amount)
        assertEquals(desiredDonation.date, donation.date)
    }

    @Test
    fun `adds donation`() = runTest {
        val newDonation = DataFiledTestTools.generateDonation()
        val result = donationService.addDonation(
            date = newDonation.date,
            type = newDonation.type,
            amount = newDonation.amount,
            hemoglobin = newDonation.hemoglobin,
            systolic = newDonation.systolic,
            diastolic = newDonation.diastolic,
            disqualification = newDonation.disqualification,
            center = newDonation.center
        ).first()
        assertIs<Either.Left<Donation>>(result)
        val donation = result.value
        assertEquals(newDonation.center.name, donation.center.name)
        assertEquals(newDonation.amount, donation.amount)
        assertEquals(newDonation.date, donation.date)
    }

    @Test
    fun `updates donation`() = runTest {
        val randomDonation = donationService.getDonations().first().random()
        val updatedDonation = DataFiledTestTools.generateDonation(id = randomDonation.id)
        val result = donationService.updateDonation(
            id = updatedDonation.id,
            date = updatedDonation.date,
            type = updatedDonation.type,
            amount = updatedDonation.amount,
            hemoglobin = updatedDonation.hemoglobin,
            systolic = updatedDonation.systolic,
            diastolic = updatedDonation.diastolic,
            disqualification = updatedDonation.disqualification,
            center = updatedDonation.center
        ).first()
        assertIs<Either.Left<Donation>>(result)
        val donation = result.value
        assertEquals(updatedDonation.center.name, donation.center.name)
        assertEquals(updatedDonation.amount, donation.amount)
        assertEquals(updatedDonation.date, donation.date)
    }

    @Test
    fun `deletes donation`() = runTest {
        val randomDonation = donationService.getDonations().first().random()
        val result = donationService.deleteDonation(randomDonation.id).first()
        assertIs<Either.Left<Boolean>>(result)
        val donations = donationService.getDonations().first()
        assertNull(donations.firstOrNull { it.id == randomDonation.id })
    }
}