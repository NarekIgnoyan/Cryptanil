package com.primesoft.cryptanil.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.adapter.TypeAdapter
import com.primesoft.cryptanil.databinding.SearchFragmentLayoutBinding
import com.primesoft.cryptanil.models.TypeItem
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.DATA_KEY
import com.primesoft.cryptanil.utils.extensions.serializable
import com.primesoft.cryptanil.utils.extensions.setDebounce
import com.primesoft.cryptanil.utils.extensions.viewBinding

class SearchFragment : Fragment(R.layout.search_fragment_layout) {

    private val binding by viewBinding(SearchFragmentLayoutBinding::bind)

    var adapter: TypeAdapter? = null
    var data: ArrayList<TypeItem>? = null
    var typeClickListener: ActionOne<TypeItem>? = null

    companion object {
        fun getInstance(searchData: ArrayList<TypeItem>) = SearchFragment().apply {
            arguments = Bundle()
            arguments?.putSerializable(DATA_KEY, searchData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.serializable(DATA_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setUpRV()
        setUpSearch()
    }

    private fun setClickListeners() {
        binding.toolbar.onActionClicked {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    fun setTypeSelectListener(listener: ActionOne<TypeItem>?) {
        typeClickListener = listener
    }

    private fun setUpRV() {
        adapter = TypeAdapter(typeClickListener)
        binding.typesRV.layoutManager = LinearLayoutManager(context)
        adapter?.setData(data)
        binding.typesRV.adapter = adapter
    }

    private fun setUpSearch() {
        binding.typeSearchET.setDebounce({ input ->
            val searchedData = ArrayList<TypeItem>()

            if (input.trim().isEmpty()) {
                adapter?.setData(data)
            } else {
                data?.forEach {
                    if (it.getType().contains(input, true)) {
                        searchedData.add(it)
                    }
                }
                adapter?.setData(searchedData)
            }
        })
    }

}