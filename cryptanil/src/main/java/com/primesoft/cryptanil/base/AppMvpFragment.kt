package com.primesoft.cryptanil.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.delegate.FragmentMvpDelegate
import com.hannesdorfmann.mosby3.mvp.delegate.FragmentMvpDelegateImpl
import com.hannesdorfmann.mosby3.mvp.delegate.MvpDelegateCallback


/**
 * A Fragment that uses a [MvpPresenter] to implement a Model-View-Presenter architecture.
 *
 * @author Hannes Dorfmann
 * @since 1.0.0
 */
abstract class AppMvpFragment<V : MvpView, P : MvpPresenter<V>>(id: Int) :
    Fragment(id), MvpDelegateCallback<V, P>, MvpView {

    var appMvpDelegate: FragmentMvpDelegate<V, P>? = null

    /**
     * The presenter for this view. Will be instantiated with [.createPresenter]
     */
    lateinit var appPresenter: P

    /**
     * Creates a new presenter instance, if needed. Will reuse the previous presenter instance if
     * [.setRetainInstance] is set to true. This method will be called from
     * [.onViewCreated]
     */
    abstract override fun createPresenter(): P

    /**
     * Gets the mvp delegate. This is internally used for creating presenter, attaching and
     * detaching view from presenter.
     *
     *
     *
     * **Please note that only one instance of mvp delegate should be used per fragment instance**.
     *
     *
     *
     *
     * Only override this method if you really know what you are doing.
     *
     *
     * @return [FragmentMvpDelegateImpl]
     */
    fun getMvpDelegate(): FragmentMvpDelegate<V, P> {
        if (appMvpDelegate == null) {
            appMvpDelegate = AppFragmentMvpDelegateImpl(this, this, true, true)
        }
        return appMvpDelegate!!
    }

    override fun getPresenter(): P {
        return appPresenter
    }

    override fun setPresenter(presenter: P) {
        this.appPresenter = presenter
    }

    override fun getMvpView(): V {
        return this as V
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMvpDelegate().onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getMvpDelegate().onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        getMvpDelegate().onDestroy()
    }

    override fun onPause() {
        super.onPause()
        getMvpDelegate().onPause()
    }

    override fun onResume() {
        super.onResume()
        getMvpDelegate().onResume()
    }

    override fun onStart() {
        super.onStart()
        getMvpDelegate().onStart()
    }

    override fun onStop() {
        super.onStop()
        getMvpDelegate().onStop()
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        getMvpDelegate().onActivityCreated(savedInstanceState)
//    }


//    override fun onAttach(activity: Activity) {
//        super.onAttach(activity)
//        getMvpDelegate().onAttach(activity)
//    }

    override fun onDetach() {
        super.onDetach()
        getMvpDelegate().onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getMvpDelegate().onSaveInstanceState(outState)
    }

}