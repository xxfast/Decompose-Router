package io.github.xxfast.decompose.router

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent

@OptIn(ExperimentalDecomposeApi::class)
fun ComponentActivity.retainedComponentContext(): ComponentContext =
  retainedComponent { it }

@OptIn(ExperimentalDecomposeApi::class)
fun Fragment.retainedComponentContext(): ComponentContext =
  retainedComponent { it }
