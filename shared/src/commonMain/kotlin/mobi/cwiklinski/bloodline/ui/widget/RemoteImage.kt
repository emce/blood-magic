package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun RemoteImage(
    modifier: Modifier = Modifier,
    url: String,
    description: String? = null,
    error: DrawableResource,
    placeHolder: DrawableResource,
    contentScale: ContentScale = ContentScale.FillWidth,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
) =
    AsyncImage(
        model = url,
        contentDescription = description,
        placeholder = painterResource(placeHolder),
        error = painterResource(error),
        fallback = painterResource(placeHolder),
        contentScale = contentScale,
        modifier = modifier,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError
    )