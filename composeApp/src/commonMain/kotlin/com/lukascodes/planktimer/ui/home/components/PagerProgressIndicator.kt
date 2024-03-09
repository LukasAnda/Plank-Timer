@file:OptIn(ExperimentalFoundationApi::class)

package com.lukascodes.planktimer.ui.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times


@Composable
fun WormPageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    indicatorSize: DpSize = DpSize(6.dp, 6.dp),
    selectedIndicatorSize: DpSize = DpSize(18.dp, 6.dp),
    color: Color = Color.White,
    spacing: Dp = indicatorSize.width,
) {
    val count = pagerState.pageCount

    val totalWidth = ((pagerState.pageCount - 1) * (indicatorSize.width + spacing)) + selectedIndicatorSize.width

    Canvas(modifier = modifier.width(width = totalWidth)) {
        val spacingPx = spacing.toPx()
        val dotWidth = indicatorSize.width.toPx()
        val dotHeight = indicatorSize.height.toPx()
        val radius = CornerRadius(indicatorSize.width.toPx(), indicatorSize.width.toPx())

        val activeDotWidth = selectedIndicatorSize.width.toPx()
        var x = 0f
        val y = center.y

        repeat(count) { i ->
            val posOffset = pagerState.pageOffset
            val dotOffset = posOffset % 1
            val current = posOffset.toInt()

            val factor = (dotOffset * (activeDotWidth - dotWidth))

            val calculatedWidth = when {
                i == current -> activeDotWidth - factor
                i - 1 == current || (i == 0 && posOffset > count - 1) -> dotWidth + factor
                else -> dotWidth
            }

            drawIndicator(x, y, calculatedWidth, dotHeight, radius, color)
            x += calculatedWidth + spacingPx
        }
    }
}

private val PagerState.pageOffset: Float
    get() = this.currentPage + this.currentPageOffsetFraction

private fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color,
) {
    val rect = RoundRect(
        x,
        y - height / 2,
        x + width,
        y + height / 2,
        radius
    )
    val path = Path().apply { addRoundRect(rect) }
    drawPath(path = path, color = color)
}