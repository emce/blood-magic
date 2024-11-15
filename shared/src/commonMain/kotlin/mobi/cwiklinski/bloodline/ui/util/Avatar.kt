package mobi.cwiklinski.bloodline.ui.util

import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.avatar_fairy
import mobi.cwiklinski.bloodline.resources.avatar_fenix
import mobi.cwiklinski.bloodline.resources.avatar_king
import mobi.cwiklinski.bloodline.resources.avatar_nymph
import mobi.cwiklinski.bloodline.resources.avatar_pegasus
import mobi.cwiklinski.bloodline.resources.avatar_wizard
import org.jetbrains.compose.resources.DrawableResource

enum class Avatar(val icon: DrawableResource) {
    PEGASUS(Res.drawable.avatar_pegasus),
    FENIX(Res.drawable.avatar_fenix),
    FAIRY(Res.drawable.avatar_fairy),
    NYMPH(Res.drawable.avatar_nymph),
    WIZARD(Res.drawable.avatar_wizard),
    KING(Res.drawable.avatar_king);

    companion object {
        fun byIcon(icon: DrawableResource) = entries.firstOrNull { it.icon == icon } ?: PEGASUS
        fun byName(name: String) = entries.firstOrNull { it.name == name } ?: PEGASUS
    }
}