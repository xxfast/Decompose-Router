import SwiftUI
import app

//@UIApplicationMain
class UIKitAppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?
  
  var rootRouterContext = DefaultRouterContextKt.defaultRouterContext()
  
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    window = UIWindow(frame: UIScreen.main.bounds)
    let mainViewController = ApplicationKt.HomeUIViewController(routerContext: rootRouterContext)
    window?.rootViewController = mainViewController
    window?.makeKeyAndVisible()
    return true
  }
  
  func applicationDidBecomeActive(_ application: UIApplication) {
    rootRouterContext.resume()
  }
  
  func applicationWillResignActive(_ application: UIApplication) {
    rootRouterContext.stop()
  }
  
  func applicationWillTerminate(_ application: UIApplication) {
    rootRouterContext.destroy()
  }
}

