package com.lukascodes.planktimer.extensions

import net.sergeych.sprintf.sprintf
import kotlin.time.Duration

fun Duration.formatToTime() = this.toComponents { hours, minutes, seconds, _ ->
    "%02d:%02d:%02d".sprintf(hours, minutes, seconds)
}