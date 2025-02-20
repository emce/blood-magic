package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.notificationsEmpty
import mobi.cwiklinski.bloodline.resources.notificationsTitle
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.NotificationView
import org.jetbrains.compose.resources.stringResource

@Parcelize
class NotificationsScreen : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        MobileLayoutWithTitle(
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.goBack)
                    )
                }
            },
            title = stringResource(Res.string.notificationsTitle),
        ) { paddingValues ->
            InternalNotificationsView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        MobileLayoutWithTitle(
            title = stringResource(Res.string.notificationsTitle),
            actions = {
                CloseButton {
                    navigator.pop()
                }
            }
        ) { paddingValues ->
            InternalNotificationsView(paddingValues)
        }
    }

    @Composable
    fun InternalNotificationsView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val notifications by screenModel.notifications.collectAsStateWithLifecycle(emptyList())
        NotificationsView(
            paddingValues = paddingValues,
            notifications = notifications,
        )
    }
}

@Composable
fun NotificationsView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notifications: List<Notification> = emptyList()
) {
    TrackScreen(Constants.ANALYTICS_SCREEN_NOTIFICATIONS)
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(AppThemeColors.white),
            verticalArrangement = Arrangement.Top,
        ) {
            if (notifications.isNotEmpty()) {
                itemsIndexed(notifications) { index, notification ->
                    NotificationView(notification)
                    if (index < notifications.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Transparent
                        )
                    }
                }
            } else {
                item {
                    Text(
                        stringResource(Res.string.notificationsEmpty),
                        style = toolbarTitle().copy(
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 100.dp)
                    )
                }
            }
        }
    }
}