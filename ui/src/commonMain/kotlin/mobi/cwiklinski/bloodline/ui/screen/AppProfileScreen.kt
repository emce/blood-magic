package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.profileAvatarFairy
import mobi.cwiklinski.bloodline.resources.profileAvatarFenix
import mobi.cwiklinski.bloodline.resources.profileAvatarKing
import mobi.cwiklinski.bloodline.resources.profileAvatarNymph
import mobi.cwiklinski.bloodline.resources.profileAvatarPegasus
import mobi.cwiklinski.bloodline.resources.profileAvatarWizard
import mobi.cwiklinski.bloodline.resources.profileDataCurrentPasswordError
import mobi.cwiklinski.bloodline.resources.profileDataEmailError
import mobi.cwiklinski.bloodline.resources.profileDataEmailUpdateError
import mobi.cwiklinski.bloodline.resources.profileDataPasswordError
import mobi.cwiklinski.bloodline.resources.profileDataPasswordRepeatError
import mobi.cwiklinski.bloodline.resources.profileDataPasswordUpdateError
import mobi.cwiklinski.bloodline.resources.profileDataUpdateError
import mobi.cwiklinski.bloodline.resources.profileUpdateError
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.util.Avatar
import org.jetbrains.compose.resources.stringResource

abstract class AppProfileScreen : AppScreen() {

    @Composable
    fun getAvatarName(avatarKey: String?): String {
        if (avatarKey != null) {
            val avatar = Avatar.byName(avatarKey)
            return when (avatar) {
                Avatar.PEGASUS -> stringResource(Res.string.profileAvatarPegasus)
                Avatar.FENIX -> stringResource(Res.string.profileAvatarFenix)
                Avatar.FAIRY -> stringResource(Res.string.profileAvatarFairy)
                Avatar.NYMPH -> stringResource(Res.string.profileAvatarNymph)
                Avatar.WIZARD -> stringResource(Res.string.profileAvatarWizard)
                Avatar.KING -> stringResource(Res.string.profileAvatarKing)
            }
        }
        return stringResource(Res.string.profileAvatarPegasus)
    }

    @Composable
    fun getError(errors: List<ProfileError>) =
        errors.map {
            when(it) {
                ProfileError.PASSWORD -> stringResource(Res.string.profileDataPasswordError)
                ProfileError.EMAIL -> stringResource(Res.string.profileDataEmailError)
                ProfileError.DATA -> stringResource(Res.string.profileDataUpdateError)
                ProfileError.ERROR -> stringResource(Res.string.profileUpdateError)
                ProfileError.CURRENT_PASSWORD -> stringResource(Res.string.profileDataCurrentPasswordError)
                ProfileError.REPEAT -> stringResource(Res.string.profileDataPasswordRepeatError)
                ProfileError.NEW_PASSWORD -> stringResource(Res.string.profileDataPasswordUpdateError)
                ProfileError.NEW_EMAIL -> stringResource(Res.string.profileDataEmailUpdateError)
            }
        }
            .joinToString("\n")
}