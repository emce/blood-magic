
import GoogleSignIn
import SwiftUI
import Firebase
import FirebaseCore
import BloodMagic

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

      FirebaseApp.configure()

      NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
                  showPushNotification: true,
                  askNotificationPermissionOnStart: true)
            )
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

  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
  }

  func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
   }

}

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
            HelperKt.doStartKoin()
        }

    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL(perform: { url in
                 GIDSignIn.sharedInstance.handle(url)
             })
        }
    }
}
