package com.lukascodes.planktimer.ui.base.viewmodel

import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.StringDescription
import com.lukascodes.planktimer.ui.base.uistate.toDescription
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.jetbrains.compose.resources.StringResource
import plank_timer.composeapp.generated.resources.Res

abstract class BaseViewModel<State, Event : UiEvent, Direction : UiDirection>() : ViewModel() {

    protected val uiState = MutableStateFlow<UiState<State?>>(
        UiState(data = null, loading = LoadingState(modal = null))
    )

    private val _direction = MutableSharedFlow<Direction>()
    val direction = _direction.asSharedFlow()

    private val lifecycleStartedFlow = channelFlow {
        onLifecycleStarted()
        launch {
        }
        send(Unit)
    }

    val state: Flow<UiState<State?>> by lazy {
        combine(uiState, lifecycleStartedFlow) { uiState, _ ->
            uiState
        }
    }

    open fun ProducerScope<Unit>.onLifecycleStarted() {}

    open fun onEvent(event: Event) {

    }

    protected fun navigate(direction: Direction) {
        viewModelScope.launch { _direction.emit(direction) }
    }
}

/**
 * State passed from ViewModels to UI
 *
 * @param Data type of data class passed as success state
 * @property data data class passed as success state
 * @property loading state holding all types of loading
 * @property alert state holding data for alert/error dialog
 */
data class UiState<Data>(
    val data: Data,
    val loading: LoadingState = LoadingState(),
    val alert: AlertState? = null,
)

interface UiEvent {

}

interface UiDirection {

}

data class LoadingState(
    val modal: Modal? = null,
    val showNonModal: Boolean = false,
) {

    data class Modal(
        val message: String,
    )
}

data class AlertState(
    val title: StringDescription?,
    val message: StringDescription,
    val primaryButton: ButtonState,
)

fun <Data> MutableStateFlow<UiState<Data>>.updateData(action: (Data) -> Data) {
    return update {
        it.copy(data = action(it.data))
    }
}

fun <Data> MutableStateFlow<UiState<Data>>.updateLoading(action: (LoadingState) -> LoadingState) {
    return update {
        it.copy(loading = action(it.loading))
    }
}

fun <Data> MutableStateFlow<UiState<Data>>.updateAlert(action: (AlertState?) -> AlertState?) {
    return update {
        it.copy(alert = action(it.alert))
    }
}

fun <Data> MutableStateFlow<UiState<Data>>.clearAlert() {
    return update {
        it.copy(alert = null)
    }
}

fun <Data> MutableStateFlow<UiState<Data>>.setAlert(
    message: StringDescription,
    title: StringDescription = Res.string.empty.toDescription(),
    primaryButtonText: StringDescription = Res.string.back.toDescription(),
) {
    return updateAlert {
        AlertState(
            title = title,
            message = message,
            primaryButton = ButtonState.Text(
                text = primaryButtonText,
                testId = "primary",
                screenId = "alert"
            )
        )
    }
}