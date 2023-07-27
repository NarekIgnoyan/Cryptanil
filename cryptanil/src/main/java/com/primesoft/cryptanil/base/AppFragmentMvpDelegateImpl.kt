package com.primesoft.cryptanil.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hannesdorfmann.mosby3.PresenterManager
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.delegate.FragmentMvpDelegate
import com.hannesdorfmann.mosby3.mvp.delegate.MvpDelegateCallback
import java.util.*


class AppFragmentMvpDelegateImpl<V : MvpView, P : MvpPresenter<V>>(
    var fragment: AppMvpFragment<V, P>?,
    private var delegateCallback: MvpDelegateCallback<V, P>?,
    keepPresenterDuringScreenOrientationChange: Boolean,
    var keepPresenterOnBackstack: Boolean
) : FragmentMvpDelegate<V, P> {

    val KEY_MOSBY_VIEW_ID = "com.hannesdorfmann.mosby3.fragment.mvp.id"

    var DEBUG = false
    private val DEBUG_TAG = "FragmentMvpVSDelegate"

    var keepPresenterInstanceDuringScreenOrientationChanges =
        keepPresenterDuringScreenOrientationChange
    private var onViewCreatedCalled = false
    var mosbyViewId: String? = null

    init {
        if (delegateCallback == null) {
            throw  NullPointerException("MvpDelegateCallback is null!");
        }
        if (fragment == null) {
            throw  NullPointerException("Fragment is null!");
        }
        if (!keepPresenterDuringScreenOrientationChange && keepPresenterOnBackstack) {
            throw  IllegalArgumentException(
                "It is not possible to keep the presenter on backstack, "
                        + "but NOT keep presenter through screen orientation changes. Keep presenter on backstack also "
                        + "requires keep presenter through screen orientation changes to be enabled"
            );
        }
    }

    private fun createViewIdAndCreatePresenter(): P {
        val presenter: P = delegateCallback!!.createPresenter()
            ?: throw NullPointerException(
                "Presenter returned from createPresenter() is null. Activity is " + getActivity()
            )
        if (keepPresenterInstanceDuringScreenOrientationChanges) {
            mosbyViewId = UUID.randomUUID().toString()
            PresenterManager.putPresenter(getActivity(), mosbyViewId!!, presenter)
        }
        return presenter
    }

    override fun onViewCreated(view: View?, bundle: Bundle?) {
        val presenter = getPresenter()
        presenter.attachView(getMvpView())
        if (DEBUG) {
            Log.d(
                DEBUG_TAG,
                "View" + getMvpView() + " attached to Presenter " + presenter
            )
        }
        onViewCreatedCalled = true
    }

    private fun getActivity(): FragmentActivity {
        return fragment?.activity
            ?: throw NullPointerException(
                "Activity returned by Fragment.getActivity() is null. Fragment is $fragment"
            )
    }

    private fun getPresenter(): P {
        val presenter = delegateCallback!!.presenter
            ?: throw NullPointerException("Presenter returned from getPresenter() is null")
        return presenter
    }

    private fun getMvpView(): V {
        val view = delegateCallback!!.mvpView
            ?: throw NullPointerException("View returned from getMvpView() is null")
        return view
    }

    fun retainPresenterInstance(
        activity: FragmentActivity, fragment: Fragment,
        keepPresenterInstanceDuringScreenOrientationChanges: Boolean,
        keepPresenterOnBackstack: Boolean
    ): Boolean {
        if (activity.isChangingConfigurations) {
            return keepPresenterInstanceDuringScreenOrientationChanges
        }
        if (activity.isFinishing) {
            return false
        }
        return if (keepPresenterOnBackstack && isFragmentInBackstack(activity, fragment)) {
            true
        } else !fragment.isRemoving
    }

    private fun isFragmentInBackstack(activity: FragmentActivity, fragment: Fragment): Boolean {

        val fragmentList = activity.supportFragmentManager.fragments

        for (existingFragment in fragmentList) {
            if (existingFragment != null && existingFragment.javaClass.isInstance(
                    fragment
                )
            ) {
                return true
            }
        }

        return false
    }

    override fun onDestroyView() {
        onViewCreatedCalled = false
        getPresenter().detachView()
        if (DEBUG) {
            Log.d(
                DEBUG_TAG, "detached MvpView from Presenter. MvpView "
                        + delegateCallback!!.mvpView
                        + "   Presenter: "
                        + getPresenter()
            )
        }
    }

    override fun onPause() {}

    override fun onResume() {}

    override fun onStart() {
        if (!onViewCreatedCalled) {
            throw IllegalStateException(
                "It seems that you are using "
                        + delegateCallback!!.javaClass.canonicalName
                        + " as headless (UI less) fragment (because onViewCreated() has not been called or maybe delegation misses that part). Having a Presenter without a View (UI) doesn't make sense. Simply use an usual fragment instead of an MvpFragment if you want to use a UI less Fragment"
            )
        }
    }

    override fun onStop() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {}

    override fun onAttach(activity: Activity?) {}

    override fun onDetach() {}

    override fun onSaveInstanceState(outState: Bundle?) {
        if (((keepPresenterInstanceDuringScreenOrientationChanges || keepPresenterOnBackstack)
                    && outState != null)
        ) {
            outState.putString(KEY_MOSBY_VIEW_ID, mosbyViewId)
            if (DEBUG) {
                Log.d(
                    DEBUG_TAG,
                    "Saving MosbyViewId into Bundle. ViewId: $mosbyViewId"
                )
            }
        }
    }

    override fun onCreate(bundle: Bundle?) {
        var presenter: P? = null
        if (bundle != null && keepPresenterInstanceDuringScreenOrientationChanges) {
            mosbyViewId = bundle.getString(KEY_MOSBY_VIEW_ID)
            if (DEBUG) {
                Log.d(
                    DEBUG_TAG,
                    "MosbyView ID = " + mosbyViewId + " for MvpView: " + delegateCallback!!.mvpView
                )
            }
            if ((mosbyViewId != null
                        && (PresenterManager.getPresenter<P>(getActivity(), mosbyViewId!!)
                    .also { presenter = it }) != null)
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
                        ("No presenter found although view Id was here: "
                                + mosbyViewId
                                + ". Most likely this was caused by a process death. New Presenter created"
                                + presenter
                                + " for view "
                                + getMvpView())
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
        if (presenter == null) {
            throw IllegalStateException(
                "Oops, Presenter is null. This seems to be a Mosby internal bug. Please report this issue here: https://github.com/sockeqwe/mosby/issues"
            )
        }
        delegateCallback!!.setPresenter(presenter)
    }

    override fun onDestroy() {
        val activity = getActivity()
        val retainPresenterInstance = retainPresenterInstance(
            activity, (fragment)!!,
            keepPresenterInstanceDuringScreenOrientationChanges, keepPresenterOnBackstack
        )
        val presenter = getPresenter()
        if (!retainPresenterInstance) {
            presenter.destroy()
            if (DEBUG) {
                Log.d(
                    DEBUG_TAG, ("Presenter destroyed. MvpView "
                            + delegateCallback!!.mvpView
                            + "   Presenter: "
                            + presenter)
                )
            }
        }
        if (!retainPresenterInstance && mosbyViewId != null) {
            // mosbyViewId is null if keepPresenterInstanceDuringScreenOrientationChanges  == false
            PresenterManager.remove(activity, mosbyViewId!!)
        }
    }

}