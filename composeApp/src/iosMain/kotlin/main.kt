import androidx.compose.ui.window.ComposeUIViewController
import com.lukascodes.planktimer.App
import com.lukascodes.planktimer.di.AppModule
import org.koin.compose.KoinApplication
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    KoinApplication(
        application = {
            modules(AppModule.module)
        }
    ) {
        App()
    }
}
