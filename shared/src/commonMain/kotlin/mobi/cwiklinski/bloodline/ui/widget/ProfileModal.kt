package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar

@Composable
fun ProfileModal(
    modifier: Modifier = Modifier,
    profile: Profile,
    title: String,
    titleStyle: TextStyle = contentTitle(),
    desiredContent: @Composable () -> Unit
    ) {
    Column(
        modifier = modifier.fillMaxWidth().wrapContentHeight()
            .background(AppThemeColors.white)
            .scrollable(
                rememberScrollState(), Orientation.Vertical
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            profile.name,
            style = contentTitle()
        )
        Text(
            "⎯⎯  ${getAvatarName(Avatar.byName(profile.avatar))}  ⎯⎯",
            style = contentAction(),
        )
        Text(
            title,
            style = titleStyle,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        )
        Spacer(Modifier.height(20.dp))
        desiredContent.invoke()
    }
}