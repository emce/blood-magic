package mobi.cwiklinski.bloodline

@Composable
fun NativeMainContent() {
    KoinApplication(
        application = {
            modules(appKoinModule)
        }
    ) {
        val viewModel = koinInject<MainViewModel>()
        val themeData by viewModel.themeData.collectAsStateCommon()
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