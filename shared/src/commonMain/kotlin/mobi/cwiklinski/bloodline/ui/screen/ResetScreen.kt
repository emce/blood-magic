package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.icon_reset
import mobi.cwiklinski.bloodline.resources.loginEmailError
import mobi.cwiklinski.bloodline.resources.loginEmailLabel
import mobi.cwiklinski.bloodline.resources.resetError
import mobi.cwiklinski.bloodline.resources.resetInformation
import mobi.cwiklinski.bloodline.resources.resetSubmitButton
import mobi.cwiklinski.bloodline.resources.resetSuccessful
import mobi.cwiklinski.bloodline.resources.resetTitle
import mobi.cwiklinski.bloodline.ui.model.ResetError
import mobi.cwiklinski.bloodline.ui.model.ResetScreenModel
import mobi.cwiklinski.bloodline.ui.model.ResetState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.RichText
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Parcelize
class ResetScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        handleSideEffects<ResetState, ResetScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                InternalResetView(paddingValues)
            }
        )
    }

    @Composable
    override fun tabletView() {
        val width = getScreenWidth()
        handleSideEffects<ResetState, ResetScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                val newPaddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = width / 4,
                    end = width / 4
                )
                InternalResetView(newPaddingValues)
            }
        )
    }

    @Composable
    fun InternalResetView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<ResetScreenModel>()
        var email by remember { mutableStateOf("") }
        val state by screenModel.state.collectAsStateWithLifecycle(ResetState.Idle)
        if (state == ResetState.Sent) {
            screenModel.resetState()
            screenModel.postSideEffect(SideEffects.SnackBar(stringResource(Res.string.resetSuccessful)))
            navigator.replaceAll(LoginScreen())
        }
        ResetView(
            paddingValues = paddingValues,
            formEnabled = state != ResetState.Sending,
            email = email,
            emailError = state is ResetState.Error,
            onEmailChanged = { newEmail ->
                email = newEmail
            },
            onSubmit = {
                screenModel.onPasswordReset(email)
            },
            onBack = {
                navigator.pop()
            },
            isResetting = state == ResetState.Sending,
            errors = if (state is ResetState.Error) (state as ResetState.Error).errors else emptyList(),
            errorMessage = { errors ->
                getError(errors)
            }
        )
    }

    @Composable
    fun getError(errors: List<ResetError>) =
        errors.map {
            when(it) {
                ResetError.EMAIL_ERROR -> stringResource(Res.string.loginEmailError)
                ResetError.ERROR -> stringResource(Res.string.resetError)
            }
        }
            .joinToString("\n")

}

@Composable
fun ResetView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    formEnabled: Boolean = true,
    email: String = "",
    emailError: Boolean = false,
    onEmailChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onBack: () -> Unit = {},
    isResetting: Boolean = false,
    errors: List<ResetError> = emptyList(),
    errorMessage: @Composable (List<ResetError>) -> String = { "" }
) {
    TrackScreen(Constants.ANALYTICS_SCREEN__RESET)
    if (errors.isNotEmpty()) {
        koinInject<CallbackManager>().postSideEffect(SideEffects.ErrorSnackBar(errorMessage.invoke(errors)))
    }
    Column(
        Modifier.fillMaxSize().background(
            AppThemeColors.authGradient
        ).padding(paddingValues).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(Modifier.height(30.dp))
        Image(
            painterResource(Res.drawable.icon_reset),
            stringResource(Res.string.resetTitle),
            modifier = Modifier
                .avatarShadow(
                    color = AppThemeColors.white,
                    sizeAdjustment = 0.38f
                )
        )
        Spacer(Modifier.height(30.dp))
        Text(
            stringResource(Res.string.resetTitle),
            style = getTypography().displayMedium.copy(
                color = AppThemeColors.violet4
            )
        )
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            RichText(
                stringResource(Res.string.resetInformation),
                centered = true
            )
            Spacer(Modifier.height(10.dp))
            OutlinedInput(
                text = email,
                onValueChanged = onEmailChanged,
                label = stringResource(Res.string.loginEmailLabel),
                enabled = formEnabled,
                error = emailError,
                errorMessage = errorMessage(listOf(ResetError.EMAIL_ERROR))
            )
            Spacer(Modifier.height(10.dp))
            if (isResetting) {
                FormProgress()
            } else {
                SubmitButton(
                    onClick = onSubmit,
                    text = stringResource(Res.string.resetSubmitButton),
                    enabled = formEnabled,
                )
            }
            Spacer(Modifier.height(20.dp))
            SecondaryButton(
                onClick = onBack,
                text = stringResource(Res.string.goBack)
            )
        }
    }
}