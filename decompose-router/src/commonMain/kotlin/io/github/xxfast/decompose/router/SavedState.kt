package io.github.xxfast.decompose.router

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.serialization.Serializable

/***
 * Handle to help the view models manage saved state
 */
class SavedStateHandle(default: @Serializable Any?): InstanceKeeper.Instance {
  private var savedState: @Serializable Any? = default
  val value: @Serializable Any? get() = savedState
  fun <T: @Serializable Any> get(): T? = savedState as? T
  fun set(value: @Serializable Any) { this.savedState = value }
  override fun onDestroy() { savedState = null }
}
