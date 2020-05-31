package `in`.aabhas.videoapplication.ui.base

import `in`.aabhas.videoapplication.utils.UiUtils
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.core.KoinComponent

abstract class BaseActivity : AppCompatActivity(), KoinComponent, BaseEvents {

    private lateinit var baseBinding: ViewDataBinding

    override val parentContext: Context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView(this, layoutResource)
        setEventHandler()
        setBindings()
        initUi(savedInstanceState)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun getBinding(): ViewDataBinding {
        return baseBinding
    }


    @get:LayoutRes
    abstract val layoutResource: Int

    abstract fun setBindings()
    abstract fun getViewModel(): BaseViewModel<*>?
    abstract fun initUi(savedInstanceState: Bundle?)
    abstract fun setEventHandler()

    inline fun <reified T> lazyBinding(): Lazy<T> = lazy { getBinding() as T }

    override fun onGeneralError(message: String) {
        UiUtils.runOnUiThread {
            UiUtils.showSnackBarMessage(baseBinding.root as ViewGroup, message)
        }
    }

    override fun onGeneralError(exception: Exception) {
        exception.message?.let {
            onGeneralError(it)
        }
    }

    override fun showErrorWithRetry(exception: Exception, call: () -> Unit) {
        exception.message?.let {
            showErrorWithRetry(it, call)
        }
    }

    override fun showErrorWithRetry(message: String, call: () -> Unit) {
        UiUtils.runOnUiThread {
            UiUtils.showRetrySnackbar(baseBinding.root as ViewGroup, message, call)
        }
    }

    override fun showToast(message: String) {
        UiUtils.runOnUiThread {
            UiUtils.showToast(this, message)
        }
    }
}