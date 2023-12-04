//
//  App.swift
//  ios
//
//  Created by Rajapaksage Isuru Rajapakse on 20/10/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import app

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

