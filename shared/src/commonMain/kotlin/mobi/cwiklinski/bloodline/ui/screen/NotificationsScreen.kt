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
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Notification
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
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
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

@Parcelize
class NotificationsScreen : AppScreen() {

    @IgnoredOnParcel
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
                getMarkAllAction { navigator.pop() }
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
            InternalNotificationsView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        MobileLayoutWithTitle(
            title = stringResource(Res.string.notificationsTitle),
            actions = {
                getMarkAllAction { navigator.pop() }
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
        NotificationsView(
            paddingValues = paddingValues,
            notifications = currentNotifications,
            tabIndex = tabIndex,
            isMarking = { notificationId ->
                state is NotificationState.MarkingAsRead && (state as NotificationState.MarkingAsRead).notification.id == notificationId
            },
            markAsRead = { notification ->
                screenModel.markAsRead(notification)
            },
            setIndex = { newIndex ->
                tabIndex = newIndex
            }
        )
    }

    @Composable
    fun getMarkAllAction(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.DoneAll,
                contentDescription = stringResource(Res.string.goBack)
            )
        }
    }
}

@Composable
fun NotificationsView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notifications: List<Notification> = emptyList(),
    tabIndex: Int = 0,
    isMarking: (String) -> Boolean = { false },
    markAsRead: (Notification) -> Unit = {},
    setIndex: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            NotificationTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        setIndex.invoke(index)
                    },
                    text = {
                        Text(
                            text = stringResource(tab.title).uppercase(),
                            style = itemSubTitle(),
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
            verticalArrangement = Arrangement.Top,
        ) {
            if (notifications.isNotEmpty()) {
                itemsIndexed(notifications) { index, notification ->
                    NotificationView(notification, isMarking) { toMark ->
                        markAsRead.invoke(toMark)
                    }
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