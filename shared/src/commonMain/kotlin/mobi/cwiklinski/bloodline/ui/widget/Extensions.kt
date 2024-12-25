package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.common.toPrecision
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationFullBlood
import mobi.cwiklinski.bloodline.resources.donationFullBloodGenitive
import mobi.cwiklinski.bloodline.resources.donationPacked
import mobi.cwiklinski.bloodline.resources.donationPackedGenitive
import mobi.cwiklinski.bloodline.resources.donationPlasma
import mobi.cwiklinski.bloodline.resources.donationPlasmaGenitive
import mobi.cwiklinski.bloodline.resources.donationPlatelets
import mobi.cwiklinski.bloodline.resources.donationPlateletsGenitive
import mobi.cwiklinski.bloodline.resources.icon_full_blood
import mobi.cwiklinski.bloodline.resources.icon_packed
import mobi.cwiklinski.bloodline.resources.icon_plasma
import mobi.cwiklinski.bloodline.resources.icon_platelets
import mobi.cwiklinski.bloodline.resources.profileAvatarFairy
import mobi.cwiklinski.bloodline.resources.profileAvatarFenix
import mobi.cwiklinski.bloodline.resources.profileAvatarKing
import mobi.cwiklinski.bloodline.resources.profileAvatarNymph
import mobi.cwiklinski.bloodline.resources.profileAvatarPegasus
import mobi.cwiklinski.bloodline.resources.profileAvatarWizard
import mobi.cwiklinski.bloodline.ui.util.Avatar
import okio.FileSystem
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

fun Orientation.isHorizontal() = this == Orientation.Horizontal

@Composable
fun DonationType.getGenitive() = stringResource(
    when (this) {
        DonationType.FULL_BLOOD -> Res.string.donationFullBloodGenitive
        DonationType.PLASMA -> Res.string.donationPlasmaGenitive
        DonationType.PLATELETS -> Res.string.donationPlateletsGenitive
        DonationType.PACKED_CELLS -> Res.string.donationPackedGenitive
    }
)

@Composable
fun DonationType.getName() = stringResource(
    when (this) {
        DonationType.FULL_BLOOD -> Res.string.donationFullBlood
        DonationType.PLASMA -> Res.string.donationPlasma
        DonationType.PLATELETS -> Res.string.donationPlatelets
        DonationType.PACKED_CELLS -> Res.string.donationPacked
    }
)

@Composable
fun DonationType.getIcon() = when (this) {
    DonationType.FULL_BLOOD -> Res.drawable.icon_full_blood
    DonationType.PLASMA -> Res.drawable.icon_plasma
    DonationType.PLATELETS -> Res.drawable.icon_platelets
    DonationType.PACKED_CELLS -> Res.drawable.icon_packed
}

fun String.camelCase() = this.split(' ')
    .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

fun Long?.toLocalDate() = if (this != null) {
        Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    } else {
        today()
    }

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            newDiskCache()
        }
        .components {
            add(SvgDecoder.Factory())
        }
        .crossfade(true)
        .logger(DebugLogger())
    .build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(512L * 1024 * 1024)
            .build()
}