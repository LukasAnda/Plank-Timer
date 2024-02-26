package com.lukascodes.planktimer.ui.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.settings.uistate.TimeTickerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimeTicker(
    state: TimeTickerState,
    modifier: Modifier = Modifier,
    onTimeChanged: (Duration) -> Unit = {},
) {
    var hours by remember { mutableStateOf(state.hours.toInt()) }
    var minutes by remember { mutableStateOf(state.minutes.toInt()) }
    var seconds by remember { mutableStateOf(state.seconds.toInt()) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        InfiniteCircularList(
            itemBoxSize = DpSize(80.dp, 60.dp),
            items = state.hourValues,
            initialItem = state.hours,
            textStyle = MaterialTheme.typography.headlineLarge,
            textColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
        ) { _, item ->
            hours = item.toInt()
            onTimeChanged(hours.hours + minutes.minutes + seconds.seconds)
        }
        Text(
            text = ":",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        InfiniteCircularList(
            itemBoxSize = DpSize(80.dp, 60.dp),
            items = state.minuteValues,
            initialItem = state.minutes,
            textStyle = MaterialTheme.typography.headlineLarge,
            textColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
        ) { _, item ->
            minutes = item.toInt()
            onTimeChanged(hours.hours + minutes.minutes + seconds.seconds)
        }
        Text(
            text = ":",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        InfiniteCircularList(
            itemBoxSize = DpSize(80.dp, 60.dp),
            items = state.secondValues,
            initialItem = state.seconds,
            textStyle = MaterialTheme.typography.headlineLarge,
            textColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
        ) { _, item ->
            seconds = item.toInt()
            onTimeChanged(hours.hours + minutes.minutes + seconds.seconds)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> InfiniteCircularList(
    itemBoxSize: DpSize,
    items: List<T>,
    initialItem: T,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    modifier: Modifier = Modifier,
    itemScaleFact: Float = 1f,
    numberOfDisplayedItems: Int = 3,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val itemHalfHeight = LocalDensity.current.run { itemBoxSize.height.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = modifier
            .width(itemBoxSize.width)
            .height(itemBoxSize.height * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = Int.MAX_VALUE,
            itemContent = { i ->
                val item = itemsState[i % itemsState.size]
                Box(
                    modifier = Modifier
                        .height(itemBoxSize.height)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val y = coordinates.positionInParent().y - itemHalfHeight
                            val parentHalfHeight =
                                (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                            val isSelected =
                                (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                            if (isSelected && lastSelectedIndex != i) {
                                onItemSelected(i % itemsState.size, item)
                                lastSelectedIndex = i
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.scale(if (lastSelectedIndex == i) itemScaleFact else 0.7f),
                        text = item.toString(),
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = textStyle.fontSize
                    )
                }
            }
        )
    }
}