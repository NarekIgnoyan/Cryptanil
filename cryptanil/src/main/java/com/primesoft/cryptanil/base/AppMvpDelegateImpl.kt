package com.primesoft.cryptanil.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.hannesdorfmann.mosby3.PresenterManager
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.delegate.ActivityMvpDelegate
import com.hannesdorfmann.mosby3.mvp.delegate.ActivityMvpDelegateImpl
import com.hannesdorfmann.mosby3.mvp.delegate.MvpDelegateCallback
import java.util.*

class AppMvpDelegateImpl<V : MvpView, P : MvpPresenter<V>>(
    var activity: Activity?,
    var delegateCallback: MvpDelegateCallback<V, P>?,
    var keepPresenterInstance: Boolean
) : ActivityMvpDelegate<V, P> {

    val KEY_MOSBY_VIEW_ID = "com.hannesdorfmann.mosby3.activity.mvp.id"

    var DEBUG = false
    val DEBUG_TAG = "ActivityMvpDelegateImpl"

    var mosbyViewId: String? = null

    init {
        if (activity == null) {
            throw NullPointerException("Activity is null!")
        }
        if (delegateCallback == null) {
            throw NullPointerException("MvpDelegateCallback is null!")
        }
    }

    private fun createViewIdAndCreatePresenter(): P {
        val presenter = delegateCallback!!.createPresenter()
            ?: throw NullPointerException(
                "Presenter returned from createPresenter() is null. Activity is $activity"
            )
        if (keepPresenterInstance) {
            mosbyViewId = UUID.randomUUID().toString()
            PresenterManager.putPresenter(activity!!, mosbyViewId!!, presenter)
        }
        return presenter
    }

    override fun onCreate(bundle: Bundle?) {
        var presenter: P? = null
        if (bundle != null && keepPresenterInstance) {
            mosbyViewId = bundle.getString(KEY_MOSBY_VIEW_ID)
            if (DEBUG) {
                Log.d(
                    DEBUG_TAG,
                    "MosbyView ID = " + mosbyViewId + " for MvpView: " + delegateCallback!!.mvpView
                )
            }
            if (mosbyViewId != null
                && PresenterManager.getPresenter<P>(activity!!, mosbyViewId!!)
                    .also { presenter = it } != null
            ) {
                //
                // Presenter restored from cache
                //
                if (DEBUG) {
                    Log.d(
                        DEBUG_TAG,
                        "Reused presenter " + presenter + " for view " + delegateCallback!!.mvpView
                    )
                }
            } else {
                //
                // No presenter found in cache, most likely caused by process death
                //
                presenter = createViewIdAndCreatePresenter()
                if (DEBUG) {
                    Log.d(
                        DEBUG_TAG,
                        "No presenter found although view Id was here: "
                                + mosbyViewId
                                + ". Most likely this was caused by a process death. New Presenter created"
                                + presenter
                                + " for view "
                                + getMvpView()
                    )
                }
            }
        } else {
            //
            // Activity starting first time, so create a new presenter
            //
            presenter = createViewIdAndCreatePresenter()
            if (DEBUG) {
                Log.d(
                    DEBUG_TAG,
                    "New presenter " + presenter + " for view " + getMvpView()
                )
            }
        }
        checkNotNull(presenter) { "Oops, Presenter is null. This seems to be a Mosby internal bug. Please report this issue here: https://github.com/sockeqwe/mosby/issues" }
        delegateCallback!!.setPresenter(presenter)
        getPresenter().attachView(getMvpView())
        if (DEBUG) {
            Log.d(
                DEBUG_TAG,
                "View" + getMvpView() + " attached to Presenter " + presenter
            )
        }
    }

    private fun getPresenter(): P {
        return delegateCallback!!.presenter
            ?: throw NullPointerException("Presenter returned from getPresenter() is null")
    }

    private fun getMvpView(): V {
        return delegateCallback!!.mvpView
            ?: throw NullPointerException("View returned from getMvpView() is null")
    }

    override fun onDestroy() {
        val retainPresenterInstance =
            retainPresenterInstance(keepPresenterInstance, activity!!)

        getPresenter().detachView()
        if (!retainPresenterInstance) {
            getPresenter().destroy()
        }
        if (!retainPresenterInstance && mosbyViewId != null) {
            PresenterManager.remove(activity!!, mosbyViewId!!)
        }
        if (DEBUG) {
            if (retainPresenterInstance) {
                Log.d(
                    DEBUG_TAG, "View"
                            + getMvpView()
                            + " destroyed temporarily. View detached from presenter "
                            + getPresenter()
                )
            } else {
                Log.d(
                    DEBUG_TAG, "View"
                            + getMvpView()
                            + " destroyed permanently. View detached permanently from presenter "
                            + getPresenter()
                )
            }
        }
    }

    override fun onPause() {}

    override fun onResume() {}

    override fun onStart() {}

    override fun onStop() {}

    override fun onRestart() {

    }

    override fun onContentChanged() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (keepPresenterInstance && outState != null) {
            outState.putString(KEY_MOSBY_VIEW_ID, mosbyViewId)
            if (ActivityMvpDelegateImpl.DEBUG) {
                Log.d(
                    DEBUG_TAG,
                    "Saving MosbyViewId into Bundle. ViewId: " + mosbyViewId + " for view " + getMvpView()
                )
            }
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {

    }


    companion object {
        fun retainPresenterInstance(keepPresenterInstance: Boolean, activity: Activity): Boolean {
            return keepPresenterInstance && (activity.isChangingConfigurations
                    || !activity.isFinishing)
        }
    }

}