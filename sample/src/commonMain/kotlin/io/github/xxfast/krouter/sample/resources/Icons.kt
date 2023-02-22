package io.github.xxfast.krouter.sample.resources

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.xxfast.krouter.sample.resources.icons.NewYorkTimes
import io.github.xxfast.krouter.sample.resources.icons.NewYorkTimesLogo

public object Icons

private var __Icons: List<ImageVector>? = null

public val Icons.Icons: List<ImageVector>
  get() {
    if (__Icons != null) {
      return __Icons!!
    }
    __Icons= listOf(NewYorkTimes, NewYorkTimesLogo)
    return __Icons!!
  }
