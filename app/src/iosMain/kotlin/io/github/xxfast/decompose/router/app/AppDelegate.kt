package io.github.xxfast.decompose.router.app

import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import io.github.xxfast.decompose.router.RouterContext
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationDelegateProtocolMeta
import platform.UIKit.UIResponder
import platform.UIKit.UIResponderMeta
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow

@OptIn(BetaInteropApi::class)
class AppDelegate @OverrideInit constructor() : UIResponder(), UIApplicationDelegateProtocol {
  companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

  private val backDispatcher = BackDispatcher()
  private val lifecycle = LifecycleRegistry()

  private val routerContext = RouterContext(
    lifecycle = lifecycle,
    backHandler = backDispatcher
  )

  private var _window: UIWindow? = null
  override fun window() = _window
  override fun setWindow(window: UIWindow?) {
    _window = window
  }

  @OptIn(ExperimentalForeignApi::class)
  override fun application(
    application: UIApplication,
    didFinishLaunchingWithOptions: Map<Any?, *>?
  ): Boolean {
    window = UIWindow(frame = UIScreen.mainScreen.bounds)
    window!!.rootViewController = MainUIController(routerContext)
    window!!.makeKeyAndVisible()
    return true
  }

  override fun applicationDidBecomeActive(application: UIApplication) {
    lifecycle.resume()
  }

  override fun applicationWillResignActive(application: UIApplication) {
    lifecycle.stop()

  }

  override fun applicationWillTerminate(application: UIApplication) {
    lifecycle.destroy()
  }
}
