package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewInformationTitle
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.ic_mark_all_read
import mobi.cwiklinski.bloodline.resources.notificationsMarkAllAsRead
import mobi.cwiklinski.bloodline.resources.notificationsMarkAllAsReadConfirmation
import mobi.cwiklinski.bloodline.resources.notificationsNoReadInformation
import mobi.cwiklinski.bloodline.resources.notificationsNoUnreadInformation
import mobi.cwiklinski.bloodline.resources.notificationsTitle
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.model.NotificationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.NotificationTab
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.InformationDialog
import mobi.cwiklinski.bloodline.ui.widget.MarkAllReadDialog
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.NotificationView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class NotificationsScreen : AppScreen() {

    override val supportDialogs = false

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(NotificationState.Idle)
        when (state) {
            NotificationState.SetMarkingAllAsRead -> {
                MarkAllReadDialog(
                    onClose = {
                        screenModel.resetState()
                    }
                ) {
                    screenModel.markAllAsRead()
                }
            }
            NotificationState.MarkedAllAsRead -> {
                InformationDialog(
                    stringResource(Res.string.donationNewInformationTitle),
                    stringResource(Res.string.notificationsMarkAllAsReadConfirmation)
                ) {
                    screenModel.resetState()
                }
            }
            else -> {}
        }
        super.Content()
    }

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
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
            actions = {
                IconButton(onClick = { screenModel.setForMarkAllAsRead() }) {
                    Icon(
                        painterResource(Res.drawable.ic_mark_all_read),
                        contentDescription = stringResource(Res.string.notificationsMarkAllAsRead)
                    )
                }
            }
        ) { paddingValues ->
            NotificationsView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        MobileLayoutWithTitle(
            title = stringResource(Res.string.notificationsTitle),
            actions = {
                IconButton(onClick = { screenModel.setForMarkAllAsRead() }) {
                    Icon(
                        painterResource(Res.drawable.ic_mark_all_read),
                        contentDescription = stringResource(Res.string.notificationsMarkAllAsRead),
                        modifier = Modifier.padding(6.dp)
                    )
                }
                CloseButton {
                    navigator.pop()
                }
            }
        ) { paddingValues ->
            NotificationsView(paddingValues)
        }
    }

    @Composable
    fun NotificationsView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(NotificationState.Idle)
        val notifications by screenModel.notifications.collectAsStateWithLifecycle(emptyList())
        var tabIndex by remember { mutableStateOf(0) }
        if (state == NotificationState.MarkedAllAsRead) {
            tabIndex = 1
        }
        val currentNotifications = notifications.filter {
            if (tabIndex == 0) {
                !it.read
            } else {
                it.read
            }
        }
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            PrimaryTabRow(selectedTabIndex = tabIndex) {
                NotificationTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = {
                            Text(
                                text = stringResource(tab.title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(AppThemeColors.white),
                verticalArrangement = Arrangement.Top
            ) {
                if (currentNotifications.isNotEmpty()) {
                    items(currentNotifications) { notification ->
                        NotificationView(notification, state) { toMark ->
                            screenModel.markAsRead(toMark)
                        }
                    }
                } else {
                    item {
                        Text(
                            stringResource(if (tabIndex == 0)
                                Res.string.notificationsNoUnreadInformation
                                    else Res.string.notificationsNoReadInformation),
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
}