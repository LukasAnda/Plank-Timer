import UIKit
import FirebaseCore
import SwiftUI
import ComposeApp
import FirebaseAnalytics

@main
struct iosApp: App {
    init() {
        FirebaseApp.configure()
        KoinHelperKt.doInitKoin(analyticsProvider: FirebaseAnalyticsProvider())
    }
    
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
        MainKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

class FirebaseAnalyticsProvider : AnalyticsProvider {
    
    func logEvent(eventName: String, eventParams: [String : Any]) {
        Analytics.logEvent(eventName, parameters: eventParams)
    }
    
    func logScreenView(screenName: String) {
        let params: [String: Any] = [AnalyticsParameterScreenName: screenName, AnalyticsParameterScreenClass: "Main"]
        Analytics.logEvent(AnalyticsEventScreenView, parameters: params)
    }
}
