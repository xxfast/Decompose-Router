package io.github.xxfast.decompose.screen.list

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class ListState(val items: List<Int>? = Loading): Parcelable

val Loading: Nothing? = null
