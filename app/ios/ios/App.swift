//
//  App.swift
//  ios
//
//  Created by Rajapaksage Isuru Rajapakse on 20/10/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

@main
struct Application: App {
  @UIApplicationDelegateAdaptor var delegate: AppDelegate

  var body: some Scene {
    WindowGroup {
      HomeView(routerContext: delegate.rootRouterContext)
    }
  }
}
