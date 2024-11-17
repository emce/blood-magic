package mobi.cwiklinski.bloodline.ui.manager

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PersistableBundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.model.ScreenModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mobi.cwiklinski.bloodline.activityprovider.api.ActivityProvider
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.ui.event.Events
import org.koin.compose.koinInject
import java.io.File

@Composable
actual fun rememberPlatformManager(): PlatformManager {
    val context = LocalContext.current
    val activity = koinInject<ActivityProvider>().get() as? FragmentActivity

    return remember {
        PlatformManager(context, activity)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun askForNotificationPermissions(model: ScreenModel) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                model.postEvent(Events.NotificationPermissionGiven)
            } else {
                // Handle permission denial
            }
        }

        val notificatioPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(notificatioPermissionState) {
            if(!notificatioPermissionState.status.isGranted){
                // Show rationale if needed
                // notificatioPermissionState.status.shouldShowRationale

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

actual class PlatformManager(
    val context: Context,
    val activity: FragmentActivity?
) {
    private var isWindowSecure: Boolean = false

    actual fun openToast(content: String): Boolean {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        return true
    }

    actual fun openBrowser(url: String, openSystemBrowser: Boolean) {
        try {
            if (openSystemBrowser) {
                context.startActivity(Intent(Intent.ACTION_VIEW).also {
                    it.setData(Uri.parse(url))
                })
            } else {
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setUrlBarHidingEnabled(false)
                builder.setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
//                    .setToolbarColor(ContextCompat.getColor(context, R.color.brand_surface))
//                    .setNavigationBarColor(ContextCompat.getColor(context, R.color.brand_surface))
//                    .setNavigationBarDividerColor(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.brand_green
//                        )
//                    )
                        .build()
                )
//            builder.setStartAnimations(context, R.anim.enter_slide_up, R.anim.fade_out)
//            builder.setExitAnimations(context, R.anim.fade_in, R.anim.exit_slide_down)

                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(url))

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    actual fun copyToClipboard(content: String, label: String?, isSensitive: Boolean): Boolean {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText(label ?: "Green", content).apply {
                if (isSensitive) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        description.extras = PersistableBundle().apply {
                            putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                        }
                    }
                }
            }
        )

        return Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    internal actual fun getClipboard(): String? {
        return (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).let {
            it.primaryClip?.getItemAt(0)?.text?.toString()
        }
    }

    actual fun clearClipboard() {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.clearPrimaryClip()
            }
        }
    }

    actual suspend fun shareText(content: String) {
        val builder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText(content)

        context.startActivity(
            Intent.createChooser(
                builder.intent,
                stringResource(Res.string.id_share)
            )
        )
    }

    actual suspend fun shareFile(path: String) {
        val fileUri = FileProvider.getUriForFile(
            context,
            context.packageName.toString() + ".provider",
            File(path)
        )

        val builder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setStream(fileUri)

        context.startActivity(
            Intent.createChooser(
                builder.intent,
                stringResource(Res.string.id_share)
            )
        )
    }

    actual fun enableLocationService(){
        activity?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}