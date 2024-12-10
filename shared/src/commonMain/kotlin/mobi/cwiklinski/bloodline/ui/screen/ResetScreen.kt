package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ResetScreen() : AppScreen() {

    @Composable
    override fun defaultView() = portraitView()

    @Composable
    override fun portraitView() {
        Scaffold {
            ResetView()
        }
    }

    @Composable
    override fun landscapeView() {
        Scaffold {
            ResetView()
        }
    }

    @Composable
    fun ResetView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<ResetScreenModel>()
        var email by remember { mutableStateOf("") }
        val state by screenModel.state.collectAsStateWithLifecycle(ResetState.Idle)
        if (state == ResetState.Sent) {
            screenModel.resetState()
            navigator.replaceAll(LoginScreen(LoginScreenState.Info(stringResource(Res.string.resetSuccessful))))
        }
        Column(
            Modifier.fillMaxSize().background(
                AppThemeColors.authGradient
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painterResource(Res.drawable.icon_reset),
                stringResource(Res.string.resetTitle),
                modifier = Modifier.padding(20.dp)
            )
            Spacer(Modifier.height(40.dp))
            Text(
                stringResource(Res.string.resetTitle),
                style = getTypography().displayLarge.copy(fontSize = 34.sp, color = AppThemeColors.violet4)
            )
            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    stringResource(Res.string.resetInformation),
                    style = getTypography().bodyMedium.copy(
                        color = AppThemeColors.black70,
                        fontSize = 16.sp
                    )
                )
                Spacer(Modifier.height(50.dp))
                OutlinedInput(
                    text = email,
                    onValueChanged = { email = it },
                    label = stringResource(Res.string.loginEmailLabel),
                    enabled = state != ResetState.Sending,
                    error = state is ResetState.Error,
                    errorMessage = getError(listOf(ResetError.EMAIL_ERROR))
                )
                Spacer(Modifier.height(30.dp))
                if (state is ResetState.Error) {
                    Text(
                        getError((state as ResetState.Error).errors),
                        style = getTypography().displaySmall.copy(
                            color = AppThemeColors.red1
                        )
                    )
                    Spacer(Modifier.height(30.dp))
                }
                if (state == ResetState.Sending) {
                    FormProgress()
                } else {
                    SubmitButton(
                        onClick = { screenModel.onPasswordReset(email) },
                        text = stringResource(Res.string.resetSubmitButton),
                        enabled = state != ResetState.Sending,
                    )
                }
                Spacer(Modifier.height(10.dp))
                SecondaryButton(
                    onClick = { navigator.pop() },
                    text = stringResource(Res.string.goBack)
                )
            }
        }
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