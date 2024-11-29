package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.common.toPrecision
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.profileAvatarFairy
import mobi.cwiklinski.bloodline.resources.profileAvatarFenix
import mobi.cwiklinski.bloodline.resources.profileAvatarKing
import mobi.cwiklinski.bloodline.resources.profileAvatarNymph
import mobi.cwiklinski.bloodline.resources.profileAvatarPegasus
import mobi.cwiklinski.bloodline.resources.profileAvatarWizard
import mobi.cwiklinski.bloodline.ui.util.Avatar
import org.jetbrains.compose.resources.stringResource

fun Int.capacity(ml: String, l: String): String = if (this < 1000) {
    "${this}$ml"
} else {
    val lowCapacity = this / 1000.00
    "${lowCapacity.toPrecision(2).replace(".", ",")}$l"
}

@Composable
fun Break() = Spacer(Modifier.height(20.dp))

@Composable
fun getAvatarName(avatar: Avatar) = stringResource(
    when (avatar) {
        Avatar.PEGASUS -> Res.string.profileAvatarPegasus
        Avatar.FENIX -> Res.string.profileAvatarFenix
        Avatar.FAIRY -> Res.string.profileAvatarFairy
        Avatar.NYMPH -> Res.string.profileAvatarNymph
        Avatar.WIZARD -> Res.string.profileAvatarWizard
        Avatar.KING -> Res.string.profileAvatarKing
    }
)