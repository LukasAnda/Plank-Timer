package com.lukascodes.planktimer.ui.base.uistate

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface StringDescription {
    @Composable
    fun text(): String

    data class Resource(private val resource: StringResource, val formatArgs: List<Any> = emptyList()) : StringDescription {
        constructor(resource: StringResource, vararg formatArgs: Any): this(resource, formatArgs.toList())
        @Composable
        override fun text(): String = stringResource(resource, *formatArgs.toTypedArray())
    }

    data class Value(private val value: String) : StringDescription {
        @Composable
        override fun text(): String = value
    }
}

fun StringResource.toDescription() = StringDescription.Resource(this)
fun String.toDescription() = StringDescription.Value(this)