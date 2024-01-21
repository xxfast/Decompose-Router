package io.github.xxfast.decompose

import kotlin.reflect.KClass

// TODO: Given that we don't have tree-shaking on js - yet, should be safe to use simpleName here
actual val KClass<*>.key: String get() =
  requireNotNull(simpleName) { "Unable to use name of $this as the default key"}
