package io.github.xxfast.decompose.router

import kotlin.reflect.KClass

internal actual val KClass<*>.key: String get() =
  requireNotNull(qualifiedName) { "Unable to use name of $this as the default key"}
