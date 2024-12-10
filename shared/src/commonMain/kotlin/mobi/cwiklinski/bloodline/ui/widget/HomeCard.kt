package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.home_card_star
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.contentText
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    subSubTitle: @Composable () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth().wrapContentHeight().padding(20.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.TopCenter)
                .offset(y = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppThemeColors.cardBackground,
                disabledContainerColor = AppThemeColors.cardBackground,
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(50.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, style = cardTitle())
                Spacer(Modifier.height(20.dp))
                Text(subTitle, style = contentText())
                subSubTitle.invoke()
            }
        }
        Image(
            painterResource(Res.drawable.home_card_star),
            title,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}