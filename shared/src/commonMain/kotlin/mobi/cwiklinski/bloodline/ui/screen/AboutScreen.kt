package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.config.AppConfig
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.infoLibraries
import mobi.cwiklinski.bloodline.resources.infoTeam
import mobi.cwiklinski.bloodline.resources.infoTeamDescription
import mobi.cwiklinski.bloodline.resources.infoTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.librariesColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.DesktopWithTitleScaffold
import mobi.cwiklinski.bloodline.ui.widget.HeaderText
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.RichText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class AboutScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        MobileLayoutWithTitle(
            title = stringResource(Res.string.infoTitle),
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.goBack)
                    )
                }
            }
        ) {
            AboutVerticalView()
        }
    }

    @Composable
    override fun tabletView() {
        super.tabletView()
        val navigator = LocalNavigator.currentOrThrow
        MobileLayoutWithTitle(
            title = stringResource(Res.string.infoTitle),
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.goBack)
                    )
                }
            }
        ) {
            AboutHorizontalView()
        }
    }

    @Composable
    override fun desktopView() {
        super.desktopView()
        val navigator = LocalNavigator.currentOrThrow
        DesktopWithTitleScaffold(
            title = stringResource(Res.string.infoTitle),
            actions = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        painterResource(Res.drawable.icon_close),
                        contentDescription = stringResource(Res.string.close)
                    )
                }
            }
        ) {
            AboutHorizontalView()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboutVerticalView() {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    var tabIndex by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        Text(
            stringResource(Res.string.appName),
            style = toolbarTitle()
        )
        Text(
            AppConfig.VERSION,
            style = cardTitle()
        )
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            AboutTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                    },
                    text = {
                        Text(
                            text = stringResource(tab.title).uppercase(),
                            style = itemSubTitle(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        if (tabIndex == 0) {
            RichText(
                stringResource(Res.string.infoTeamDescription).trimIndent(),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else {
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier.fillMaxWidth().weight(1.0f).padding(horizontal = 20.dp),
                colors = librariesColors(),
                showVersion = false
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboutHorizontalView() {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    TrackScreen(Constants.ANALYTICS_SCREEN_ABOUT)
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.appName),
                style = toolbarTitle()
            )
            Text(
                AppConfig.VERSION,
                style = cardTitle()
            )
            HeaderText(
                stringResource(Res.string.infoTeam),
                textStyle = getTypography().headlineLarge
            )
            RichText(
                stringResource(Res.string.infoTeamDescription).trimIndent(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            HeaderText(
                stringResource(Res.string.infoLibraries),
                textStyle = getTypography().headlineLarge
            )
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = librariesColors(),
                showVersion = false
            )
        }
    }
}

enum class AboutTab(val title: StringResource) {
    TEAM(Res.string.infoTeam),
    LIBRARIES(Res.string.infoLibraries)
}