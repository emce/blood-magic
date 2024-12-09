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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.avatar_submit
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.profileAvatarPegasus
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppFontFamily
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getFontFamily
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ProfileAvatarScreen : AppProfileScreen() {

    @Composable
    override fun verticalView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        var avatar by remember { mutableStateOf(Avatar.byName(profile.avatar)) }
        val density = LocalDensity.current
        var cellHeight by remember { mutableStateOf(0.dp) }
        val state by screenModel.state.collectAsStateWithLifecycle(ProfileState.Idle)
        if (state == ProfileState.Saved) {
            bottomSheetNavigator.hide()
        }
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
                Button(
                    onClick = { bottomSheetNavigator.hide() },
                    colors = AppThemeColors.textButtonColors(),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Image(
                        painterResource(Res.drawable.icon_close),
                        stringResource(Res.string.close)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .background(AppThemeColors.white).padding(bottom = 20.dp)
                    .scrollable(
                        rememberScrollState(), Orientation.Vertical
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    stringResource(Res.string.profileAvatarPegasus),
                    style = getTypography().displayMedium.copy(color = AppThemeColors.black)
                )
                Text(
                    "⎯⎯  ${getAvatarName(profile.avatar)}  ⎯⎯",
                    style = getTypography().displayMedium.copy(
                        color = AppThemeColors.black70,
                        fontFamily = getFontFamily(AppFontFamily.REGULAR)
                    ),
                )
                Text(
                    stringResource(Res.string.profileAvatarTitle),
                    style = getTypography().displaySmall.copy(
                        color = AppThemeColors.black,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 124.dp),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(Avatar.entries.size) { index ->
                        val listAvatar = Avatar.entries[index]
                        Column(
                            modifier = Modifier.width(124.dp).wrapContentHeight()
                                .onGloballyPositioned {
                                    cellHeight = with(density) {
                                        it.size.height.toDp()
                                    }
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .selectable(
                                        selected = profile.avatar == listAvatar.name,
                                        interactionSource = MutableInteractionSource(),
                                        indication = null,
                                        onClick = {
                                            avatar = listAvatar
                                        },
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatar == listAvatar) {
                                    Canvas(
                                        modifier = Modifier.size(70.dp).offset(y = 2.dp)
                                    ) {
                                        drawCircle(AppThemeColors.violet2)
                                    }
                                }
                                Image(
                                    painterResource(listAvatar.icon),
                                    listAvatar.name,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Text(
                                getAvatarName(listAvatar.name),
                                style = getTypography().headlineMedium.copy(
                                    color = AppThemeColors.black70,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.width(124.dp).height(cellHeight),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (screenModel.state.value != ProfileState.Saving) {
                                Image(
                                    painterResource(Res.drawable.avatar_submit),
                                    stringResource(Res.string.profileDataSubmitButton),
                                    modifier = Modifier.width(80.dp).height(80.dp).clickable {
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
                            } else {
                                FormProgress()
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    @Composable
    override fun horizontalView() {
        verticalView()
    }
}