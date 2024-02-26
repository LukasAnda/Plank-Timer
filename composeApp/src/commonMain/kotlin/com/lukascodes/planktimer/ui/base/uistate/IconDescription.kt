package com.lukascodes.planktimer.ui.base.uistate

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

interface IconDescription {
    @Composable
    fun painter(): Painter

    data class Resource(private val resource: DrawableResource) : IconDescription {
        @Composable
        override fun painter(): Painter = painterResource(resource)
    }

    data class Icon(private val vector: ImageVector) : IconDescription {
        @Composable
        override fun painter(): Painter = rememberVectorPainter(vector)
    }
}