package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle

@Composable
fun HeaderText(
    text: String,
    textStyle: TextStyle = itemSubTitle(),
    lineWidth: Dp = 30.dp,
    lineHeight: Dp = 1.dp,
    lineColor: Color = AppThemeColors.black
) {
    Row(
        modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(lineWidth).height(lineHeight).background(lineColor))
        Spacer(modifier = Modifier.width(5.dp))
        Text(text.uppercase(), style = textStyle.copy(fontStyle = FontStyle.Italic))
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.width(lineWidth).height(lineHeight).background(lineColor))
    }
}