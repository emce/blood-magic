package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun RemoteImage(
    modifier: Modifier = Modifier,
    url: String,
    description: String? = null,
    error: DrawableResource,
    placeHolder: DrawableResource
) =
    AsyncImage(
        model = url,
        contentDescription = description,
        placeholder = painterResource(placeHolder),
        error = painterResource(error),
        fallback = painterResource(placeHolder),
        contentScale = ContentScale.FillWidth,
        modifier = modifier.clip(RoundedCornerShape(10.dp))
    )