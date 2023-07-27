package com.primesoft.cryptanil.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.delegate.ActivityMvpDelegate
import com.hannesdorfmann.mosby3.mvp.delegate.ActivityMvpDelegateImpl
import com.hannesdorfmann.mosby3.mvp.delegate.MvpDelegateCallback

public abstract class AppMvpActivity<V : MvpView, P : MvpPresenter<V>>
    : AppCompatActivity(), MvpView, MvpDelegateCallback<V, P> {

    var appMvpDelegate: ActivityMvpDelegate<V, P>? = null
    var retainInstance: Boolean? = null
    var appPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate()?.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        getMvpDelegate()?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getMvpDelegate()?.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        getMvpDelegate()?.onPause()
    }

    override fun onResume() {
        super.onResume()
        getMvpDelegate()?.onResume()
    }

    override fun onStart() {
        super.onStart()
        getMvpDelegate()?.onStart()
    }

    override fun onStop() {
        super.onStop()
        getMvpDelegate()?.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        getMvpDelegate()?.onRestart()
    }

    override fun onContentChanged() {
        super.onContentChanged()
        getMvpDelegate()?.onContentChanged()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        getMvpDelegate()?.onPostCreate(savedInstanceState)
    }

    /**
     * Instantiate a presenter instance
     *
     * @return The [MvpPresenter] for this view
     */
    abstract override fun createPresenter(): P

    /**
     * Get the mvp delegate. This is internally used for creating presenter, attaching and detaching
     * view from presenter.
     *
     *
     * **Please note that only one instance of mvp delegate should be used per Activity
     * instance**.
     *
     *
     *
     *
     * Only override this method if you really know what you are doing.
     *
     *
     * @return [ActivityMvpDelegateImpl]
     */
    protected open fun getMvpDelegate(): ActivityMvpDelegate<V, P>? {
        if (appMvpDelegate == null) {
            appMvpDelegate = AppMvpDelegateImpl(this, this, true)
        }
        return appMvpDelegate
    }

    override fun getPresenter(): P {
        return appPresenter as P
    }

    override fun setPresenter(presenter: P) {
        appPresenter = presenter
    }

    override fun getMvpView(): V {
        return this as V
    }

}
