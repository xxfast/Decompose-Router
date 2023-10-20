//
//  HomeView.swift
//  ios
//
//  Created by Rajapaksage Isuru Rajapakse on 20/10/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import app

struct HomeView: UIViewControllerRepresentable {
  let routerContext: RouterContext
  
  func makeUIViewController(context: Context) -> UIViewController {
    return ApplicationKt.HomeUIViewController(routerContext: routerContext)
  }
  
  func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
