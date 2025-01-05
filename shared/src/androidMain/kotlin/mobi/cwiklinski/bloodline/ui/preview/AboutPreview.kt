package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.infoTeamDescription
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import mobi.cwiklinski.bloodline.ui.widget.RichText
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun AboutPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp)
        ) {
            RichText(
                stringResource(Res.string.infoTeamDescription).trimIndent()
            )
        }
    }
}