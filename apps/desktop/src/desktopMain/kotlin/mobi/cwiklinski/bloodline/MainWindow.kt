package mobi.cwiklinski.bloodline

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(600.dp, 500.dp)
        ),
        title = "Blood Magic",
        icon = painterResource(MoviesIcons.LauncherRed),
        alwaysOnTop = true,
        onKeyEvent = { false }
    ) {
        window.minimumSize = Dimension(600, 500)

        App()
    }
}

@Composable
private fun App() {
    KoinApplication(
        application = {
            modules(appKoinModule)
        }
    ) {
        val viewModel = koinInject<MainViewModel>()
        val themeData by viewModel.themeData.collectAsStateCommon()

        withViewModelStoreOwner {
            MoviesTheme(
                themeData = ThemeData(
                    appTheme = themeData.appTheme,
                    dynamicColors = false,
                    paletteKey = themeData.paletteKey,
                    seedColor = themeData.seedColor
                ),
                theme = themeData.appTheme,
                enableEdgeToEdge = { _,_ -> }
            ) {
                MainContent()
            }
        }
    }
}