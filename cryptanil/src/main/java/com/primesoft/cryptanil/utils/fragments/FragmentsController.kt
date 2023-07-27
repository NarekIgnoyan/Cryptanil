package com.primesoft.cryptanil.utils.fragments

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.primesoft.cryptanil.R

class FragmentsController {

    companion object {

        fun addFragment(
            manager: FragmentManager, @IdRes containerId: Int,
            newFragment: Fragment,
            tag: String,
            withAnimation: Boolean = false,
            forceAdd: Boolean = false,
            addToBackStack: Boolean
        ) {
            val transaction = manager.beginTransaction()

            if (canAddFragment(manager, newFragment) || forceAdd) {
                transaction.add(containerId, newFragment, tag)

                if (withAnimation)
                    transaction.setCustomAnimations(
                        R.animator.fragment_slide_top,
                        R.animator.fragment_slide_bottom
                    )

                if (addToBackStack)
                    transaction.addToBackStack(tag)

                transaction
                    .show(newFragment)
                    .commit()
            } else {
                if (withAnimation)
                    transaction.setCustomAnimations(
                        R.animator.fragment_slide_top,
                        R.animator.fragment_slide_bottom
                    )

                transaction.show(newFragment)
                    .commit()

            }
        }

        private fun canAddFragment(manager: FragmentManager, fragment: Fragment): Boolean {
            var canAddNewFragment = true

            val fragmentList = manager.fragments
            for (existingFragment in fragmentList) {
                if (existingFragment != null && existingFragment.javaClass.isInstance(
                        fragment
                    )
                ) {
                    canAddNewFragment = false
                }
            }

            return canAddNewFragment
        }

        fun removeFragment(manager: FragmentManager?, tag: String) {
            manager?.beginTransaction()?.remove(manager.findFragmentByTag(tag)!!)?.commit()
        }

        fun popAllFragmentBackStack(fragmentManager: FragmentManager?) {
            for (i in 0 until (fragmentManager?.backStackEntryCount ?: 0)) {
                fragmentManager?.popBackStack()
            }
        }

        fun haveBackStack(fragmentManager: FragmentManager) =
            fragmentManager.backStackEntryCount == 0

    }

}