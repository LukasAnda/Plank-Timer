import UIKit
import SwiftUI
import ComposeApp
import FirebaseAnalytics

@main
struct iosApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController(analyticsProvider: FirebaseAnalyticsProvider())
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

class FirebaseAnalyticsProvider : AnalyticsProvider {
    func logEvent(eventName: String, eventParams: [String : Any]) {
        Analytics.logEvent("button_clicked", parameters: eventParams)
    }
    
    func logScreenView(screenName: String) {
        var params: [String: Any] = [AnalyticsParameterScreenName: screenName, AnalyticsParameterScreenClass: "Main"]
        Analytics.logEvent(AnalyticsEventScreenView, parameters: params)
    }
}
