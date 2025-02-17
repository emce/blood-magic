package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.infoTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_info
import mobi.cwiklinski.bloodline.resources.profileTitle
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigation
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.LeftNavigation
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun MobileToolbarPreview() {
    MobileTitleBar(
        title = stringResource(Res.string.profileTitle),
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    painterResource(Res.drawable.nav_icon_info),
                    contentDescription = stringResource(Res.string.infoTitle),
                    modifier = Modifier.size(40.dp).padding(5.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(Res.string.settingsLogoutTitle),
                    modifier = Modifier.size(40.dp).padding(5.dp)
                )
            }
        })
}

@Preview
@Composable
fun ProgressPreview() {
    Box(modifier = Modifier.padding(6.dp).background(Color.White)) {
        FormProgress()
    }
}

@Preview
@Composable
fun TabletToolbarPreview() {
    LeftNavigation(
        onClicked = {},
    )
}

@Preview(locale = "pl")
@Composable
fun DesktopToolbarPreview() {
    DesktopNavigation(
        onClicked = {},
        floatingAction = {}
    ) {}
}