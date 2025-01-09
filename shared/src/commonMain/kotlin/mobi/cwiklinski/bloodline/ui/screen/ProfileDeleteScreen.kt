package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDeleteButton
import mobi.cwiklinski.bloodline.resources.profileDeleteContent
import mobi.cwiklinski.bloodline.resources.profileDeleteTitle
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.ProfileModal
import mobi.cwiklinski.bloodline.ui.widget.RichText
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class ProfileDeleteScreen : AppProfileScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val state by screenModel.state.collectAsStateWithLifecycle(ProfileState.Idle)
        when (state) {
            ProfileState.Saved -> {
                bottomSheetNavigator.hide()
            }
            ProfileState.ToDelete -> {
                screenModel.postSideEffect(SideEffects.DeleteAccountEffect())
                bottomSheetNavigator.hide()
                screenModel.resetState()
            }
            else -> { }
        }
        ProfileDeleteView(
            profile = profile,
            onClose = {
                bottomSheetNavigator.hide()
            },
            onConfirm = {
                screenModel.setToDelete()
            },
            isSaving = screenModel.state.value == ProfileState.Saving
        )
    }
}

@Composable
fun ProfileDeleteView(
    profile: Profile,
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {},
    isSaving: Boolean = false
) {
    Column(
        modifier = Modifier.background(AppThemeColors.homeGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth().wrapContentHeight()
                .background(Color.Transparent)
                .drawBehind {
                    val start = 100.dp.value
                    val middle = 450.dp.value
                    drawPath(
                        color = AppThemeColors.white,
                        path = Path().apply {
                            reset()
                            moveTo(0f, start)
                            cubicTo(
                                x1 = 0f,
                                y1 = start,
                                x2 = size.width / 2,
                                y2 = middle,
                                x3 = size.width,
                                y3 = start
                            )
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            lineTo(0f, start)
                            close()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(146.dp)
                    .shadow(
                        10.dp,
                        shape = CircleShape,
                        ambientColor = AppThemeColors.white,
                        spotColor = AppThemeColors.white
                    ).offset(y = 4.dp)
            ) {
                drawCircle(AppThemeColors.white.copy(alpha = 0.2f))
            }
            Image(
                painterResource(Avatar.byName(profile.avatar).icon),
                stringResource(Res.string.profileAvatarTitle),
                modifier = Modifier.width(184.dp).height(184.dp).avatarShadow()
            )
            CloseButton(modifier = Modifier.align(Alignment.TopEnd)) {
                onClose.invoke()
            }
        }
        ProfileModal(
            profile = profile,
            title = stringResource(Res.string.profileDeleteTitle),
            titleStyle = contentTitle().copy(
                textAlign = TextAlign.Center,
                color = AppThemeColors.alertRed
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                RichText(
                    stringResource(Res.string.profileDeleteContent)
                        .replace("%s", stringResource(Res.string.profileDeleteButton)),
                    modifier = Modifier.weight(1.0f),
                    centered = true
                )
                if (!isSaving) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SecondaryButton(
                            onClick = onClose,
                            text = stringResource(Res.string.close),
                        )
                        SubmitButton(
                            onClick = onConfirm,
                            text = stringResource(Res.string.profileDeleteButton),
                        )
                    }
                } else {
                    FormProgress()
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}