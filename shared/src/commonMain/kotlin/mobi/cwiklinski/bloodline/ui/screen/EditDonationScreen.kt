package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.domain.model.Donation

class EditDonationScreen(val donation: Donation) : AppScreen() {

    @Composable
    override fun defaultView() = portraitView()

    @Composable
    override fun portraitView() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun landscapeView() {
        portraitView()
    }
}