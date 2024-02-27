import androidx.compose.ui.window.ComposeUIViewController
import com.lukascodes.planktimer.App
import com.lukascodes.planktimer.di.AppModule
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(
    analyticsProvider: AnalyticsProvider,
): UIViewController = ComposeUIViewController {
    KoinApplication(
        application = {
            modules(
                AppModule.module,
                module {
                    single<AnalyticsProvider> { analyticsProvider }
                },
            )
        }
    ) {
        App()
    }
}
