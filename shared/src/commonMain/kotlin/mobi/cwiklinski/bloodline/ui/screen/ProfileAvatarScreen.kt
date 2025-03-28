package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.avatar_submit
import mobi.cwiklinski.bloodline.resources.profileAvatarPegasus
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class ProfileAvatarScreen : AppProfileScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        var avatar by remember { mutableStateOf(Avatar.byName(profile.avatar)) }
        val state by screenModel.state.collectAsStateWithLifecycle(ProfileState.Idle)
        if (state == ProfileState.Saved) {
            bottomSheetNavigator.hide()
        }
        AvatarView(
            avatar = Avatar.byName(profile.avatar),
            onClose = {
                bottomSheetNavigator.hide()
            },
            avatarName = { avatarName ->
                getAvatarName(avatarName)
            },
            isAvatarSelected = { name ->
                profile.avatar == name
            },
            onAvatarSelected = { newAvatar ->
                avatar = newAvatar
            },
            isSaving = screenModel.state.value == ProfileState.Saving,
            saveProfile = {
                screenModel.onProfileDataUpdate(
                    profile.name,
                    profile.email,
                    avatar.name,
                    profile.sex,
                    profile.notification,
                    profile.starting,
                    profile.centerId
                )
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvatarView(
    avatar: Avatar = Avatar.FENIX,
    onClose: () -> Unit = {},
    avatarName: @Composable (String) -> String = { avatar.name },
    isAvatarSelected: (String) -> Boolean = { false },
    onAvatarSelected: (Avatar) -> Unit = {},
    isSaving: Boolean = false,
    saveProfile: () -> Unit = {}
) {
    TrackScreen(Constants.ANALYTICS_SCREEN_PROFILE_AVATAR)
    val cellWidth = 124.dp
    val cellHeight = 120.dp
    val cellPadding = 10.dp
    val avatarImageSize = 60.dp
    Column(
        modifier = Modifier.background(AppThemeColors.homeGradient)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth().wrapContentHeight()
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
            Image(
                painterResource(avatar.icon),
                avatar.name,
                modifier = Modifier.width(184.dp).height(184.dp).avatarShadow()
            )
            CloseButton(modifier = Modifier.align(Alignment.TopEnd)) {
                onClose.invoke()
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .background(AppThemeColors.white).padding(bottom = 40.dp)
                .scrollable(
                    rememberScrollState(), Orientation.Vertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                stringResource(Res.string.profileAvatarPegasus),
                style = contentTitle()
            )
            Text(
                "⎯⎯  ${avatarName.invoke(avatar.name)}  ⎯⎯",
                style = contentAction(),
            )
            Text(
                stringResource(Res.string.profileAvatarTitle),
                style = contentTitle(),
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.Top,
                maxItemsInEachRow = 3
            ) {
                Avatar.entries.filter { it != avatar }.forEach { listAvatar ->
                    val selected = isAvatarSelected.invoke(listAvatar.name)
                    Column(
                        modifier = Modifier.padding(cellPadding).width(cellWidth - cellPadding.times(2)).height(cellHeight - cellPadding.times(2)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(80.dp)
                                .selectable(
                                    selected = isAvatarSelected(listAvatar.name),
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        onAvatarSelected.invoke(listAvatar)
                                    },
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) {
                                Canvas(
                                    modifier = Modifier.size(70.dp).offset(y = 1.dp)
                                ) {
                                    drawCircle(AppThemeColors.violet2)
                                }
                            }
                            Image(
                                painterResource(listAvatar.icon),
                                listAvatar.name,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Text(
                            avatarName.invoke(listAvatar.name),
                            style = if (selected) itemSubTitle().copy(fontWeight = FontWeight.Bold) else itemSubTitle(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(cellPadding).width(cellWidth - cellPadding.times(2)).height(cellHeight - cellPadding.times(2)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isSaving) {
                        Image(
                            painterResource(Res.drawable.avatar_submit),
                            stringResource(Res.string.profileDataSubmitButton),
                            modifier = Modifier.size(avatarImageSize).clickable {
                                saveProfile.invoke()
                            }
                        )
                    } else {
                        FormProgress(modifier = Modifier.size(avatarImageSize))
                    }
                }
            }
        }
    }
}