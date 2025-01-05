package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.infoTeam
import mobi.cwiklinski.bloodline.resources.infoTeamDescription
import mobi.cwiklinski.bloodline.resources.infoTitle
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.widget.HeaderText
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.RichText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Parcelize
class AboutScreen : AppScreen() {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val libraries by rememberLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
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
            Column {
                HeaderText(
                    stringResource(Res.string.infoTeam),
                    textStyle = getTypography().headlineLarge
                )
                RichText(
                    stringResource(Res.string.infoTeamDescription).trimIndent()
                )
                HeaderText(
                    stringResource(Res.string.infoTeam),
                    textStyle = getTypography().headlineLarge
                )
                LibrariesContainer(libraries, Modifier.fillMaxSize())
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun tabletView() {
        super.tabletView()
        val navigator = LocalNavigator.currentOrThrow
        val libraries by rememberLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
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
            AboutHorizontalView(libraries)
        }
    }
}

@Composable
fun AboutHorizontalView(libraries: Libs?) {
    Row {
        Column {
            HeaderText(
                stringResource(Res.string.infoTeam),
                textStyle = getTypography().headlineLarge
            )
            RichText(
                stringResource(Res.string.infoTeamDescription).trimIndent()
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
        Column {
            HeaderText(
                stringResource(Res.string.infoTeam),
                textStyle = getTypography().headlineLarge
            )
            LibrariesContainer(libraries, Modifier.fillMaxSize())
        }
    }
}