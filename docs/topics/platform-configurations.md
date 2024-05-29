# Platform configurations

## Android / WearOS

You will need to provide your default `RouterContext` from an `Activity`/`Fragment`

```kotlin
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val rootRouterContext: RouterContext = defaultRouterContext()
    setContent {
      CompositionLocalProvider(LocalRouterContext provides rootRouterContext) {
        // Your app goes here
      }
    }
  }
}
```

## Desktop

You will need to provide your default `RouterContext` at the `Application` level

```kotlin
fun main() {
  application {
    val windowState: WindowState = rememberWindowState()
    val rootRouterContext: RouterContext = defaultRouterContext(windowState = windowState)

    Window(..) {
      CompositionLocalProvider(LocalRouterContext provides rootRouterContext) {
        // Your app goes here
      }
    }
  }
}
```

## iOS

Create your default `RouterContext` outside `ComposeUIViewController`'s composable lambda and pass it in to `LocalRouterContext`

```kotlin
fun HomeUIViewController(routerContext: RouterContext): UIViewController =
  ComposeUIViewController {
    CompositionLocalProvider(
      LocalRouterContext provides routerContext,
    ) {
      // Your app goes here
    }
}
```

You will need to tie root `RouterContext`'s lifecycle to an `AppDelegate`.

> Important!
> To invoke decompose router's `defaultRouterContext()` from swift, you will need to export decompose-router from your shared module.
> 
> Refer to the sample [here](https://github.com/xxfast/Decompose-Router/blob/main/app/build.gradle.kts)
> ```kotlin
> kotlin {
>   ios {
>     binaries{
        framework {
>         export(libs.decompose.router)
>       }
>     }
>   }
> 
>   sourceSets {
>       val commonMain by getting {
>           dependencies {
>           // Only need to add this as api if you wish to add your own AppDelegate in swift
>           api(libs.decompose.router)
>       }
>   }
> }
> ```
{style=warning}

If you are using SwiftUI
```Swift
@main
struct SwiftUIApp: App {
  @UIApplicationDelegateAdaptor var delegate: AppDelegate
  @Environment(\.scenePhase)  var scenePhase: ScenePhase

  var defaultRouterContext: RouterContext { delegate.holder.defaultRouterContext }
  
  var body: some Scene {
    WindowGroup {
      HomeView(routerContext: defaultRouterContext)
    }
    .onChange(of: scenePhase) { newPhase in
        switch newPhase {
        case .background: defaultRouterContext.stop()
        case .inactive: defaultRouterContext.pause()
        case .active: defaultRouterContext.resume()
        @unknown default: break
        }
    }
  }
}

class DefaultRouterHolder : ObservableObject {
  let defaultRouterContext: RouterContext = DefaultRouterContextKt.defaultRouterContext()

  deinit {
    // Destroy the root component before it is deallocated
    defaultRouterContext.destroy()
  }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    let holder: DefaultRouterHolder = DefaultRouterHolder()
}



struct HomeView: UIViewControllerRepresentable {
  let routerContext: RouterContext
  
  func makeUIViewController(context: Context) -> UIViewController {
    return ApplicationKt.HomeUIViewController(routerContext: routerContext)
  }
  
  func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

```
{collapsible="true" collapsed-title="@main struct SwiftUIApp: App {}"}

If you are using UIKit
```Swift
@UIApplicationMain
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
```
{collapsible="true" collapsed-title="@UIApplicationMain class UIKitAppDelegate: UIResponder, UIApplicationDelegate { }"}

## Web
Create your default RouterContext once and supply with `CompositionLocalProvider` 

### JsBrowser
```kotlin
fun main() {
  val rootRouterContext: RouterContext = defaultRouterContext()

  onWasmReady {
    BrowserViewportWindow("App") {
      CompositionLocalProvider(LocalRouterContext provides rootRouterContext) {
        MaterialTheme {
          // Your app goes here
        }
      }
    }
  }
}
```

### WasmJsBrowser
```kotlin
fun main() {
  val rootRouterContext: RouterContext = defaultRouterContext()

  BrowserViewportWindow("App") {
    CompositionLocalProvider(LocalRouterContext provides rootRouterContext) {
      MaterialTheme {
        // Your app goes here
      }
    }
  }
}
```
