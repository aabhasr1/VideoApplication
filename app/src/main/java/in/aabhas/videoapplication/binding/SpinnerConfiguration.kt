package `in`.aabhas.videoapplication.binding

import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.databinding.BaseObservable

class SpinnerConfiguration(
    val adapter: SpinnerAdapter,
    val onSpinnerConfig: (spinner: Spinner) -> Unit
) : BaseObservable() {
    fun onSpinnerConfig(spinner: Spinner) {
        onSpinnerConfig.invoke(spinner)
    }
}