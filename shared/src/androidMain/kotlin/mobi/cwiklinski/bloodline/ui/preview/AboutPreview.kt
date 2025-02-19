package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import mobi.cwiklinski.bloodline.config.AppConfig
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.infoLibraries
import mobi.cwiklinski.bloodline.resources.infoTeam
import mobi.cwiklinski.bloodline.resources.infoTeamDescription
import mobi.cwiklinski.bloodline.ui.screen.AboutHorizontalView
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.librariesColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.HeaderText
import mobi.cwiklinski.bloodline.ui.widget.RichText
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun AboutPreview() {
    val libraries = Libs.Builder()
        .withJson(DummyData.LIBRARIES_JSON)
        .build()
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
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
                stringResource(Res.string.infoTeamDescription).trimIndent()
            )
            HeaderText(
                stringResource(Res.string.infoLibraries),
                textStyle = getTypography().headlineLarge
            )
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier.fillMaxSize(),
                colors = librariesColors(),
                showVersion = false
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_tablet")
@Composable
fun HorizontalAboutPreview() {
    val libraries = Libs.Builder()
        .withJson(DummyData.LIBRARIES_JSON)
        .build()
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            AboutHorizontalView(libraries)
        }
    }
}