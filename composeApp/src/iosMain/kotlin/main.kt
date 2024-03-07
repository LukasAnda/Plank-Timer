import androidx.compose.ui.window.ComposeUIViewController
import com.lukascodes.planktimer.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}