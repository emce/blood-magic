
import GoogleSignIn
import SwiftUI
import Firebase
import FirebaseCore
import BloodMagic

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

      FirebaseApp.configure()
      return true
  }

  func application(_ app: UIApplication, open url: URL,
                options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        var handled: Bool

        handled = GIDSignIn.sharedInstance.handle(url)
        if handled {
          return true
        }
        return false
  }

}

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
            HelperKt.doInitKoin()
        }

    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL(perform: { url in
                 GIDSignIn.sharedInstance.handle(url)
             })
        }
    }
}