package io.github.xxfast.decompose.router

import kotlin.reflect.KClass

actual val KClass<*>.key: String get() =
  requireNotNull(qualifiedName) { "Unable to use name of $this as the default key"}
