package `in`.aabhas.videoapplication.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

open class BaseViewModel<E : BaseEvents> : ViewModel(), KoinComponent {
    lateinit var event: E

    fun setEventHandler(eventHandler: E) {
        this.event = eventHandler
    }

    fun launchIO(call: suspend (scope: CoroutineScope) -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            call.invoke(this)
        }
    }

    fun launchMain(call: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            call.invoke()
        }
    }

    fun launchComputation(call: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Default) {
            call.invoke()
        }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}
