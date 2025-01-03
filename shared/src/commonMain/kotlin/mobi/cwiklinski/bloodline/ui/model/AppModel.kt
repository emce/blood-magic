package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.StateScreenModel
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import org.koin.core.component.KoinComponent

abstract class AppModel<S>(initialState: S, private val callbackManager: CallbackManager) :
    StateScreenModel<S>(initialState), KoinComponent, CallbackManager by callbackManager {

    private var _bootstrapped: Boolean = false

    override fun bootstrap() {
        _bootstrapped = true
        callbackManager.bootstrap()
    }
}