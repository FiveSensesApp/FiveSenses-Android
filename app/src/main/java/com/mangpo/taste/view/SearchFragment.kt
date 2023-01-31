package com.mangpo.taste.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentSearchBinding
import com.mangpo.taste.util.SpfUtils.getStrSpf
import com.mangpo.taste.util.SpfUtils.removeSpf
import com.mangpo.taste.util.SpfUtils.writeSpf
import com.mangpo.taste.view.adpater.RecentSearchTermRVAdapter

class SearchFragment : BaseNoVMFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var recentSearchTermRVAdapter: RecentSearchTermRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@SearchFragment
        }
    }

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
    }

    private fun initAdapter() {
        recentSearchTermRVAdapter = RecentSearchTermRVAdapter()
        recentSearchTermRVAdapter.setMyEventListener(object: RecentSearchTermRVAdapter.MyEventListener {
            override fun onSearch(keyword: String) {
                goSearchResultFragment(keyword)
            }

            override fun onDelete(position: Int) {
                val recentSearchTerms = Gson().fromJson<ArrayList<String>>(getStrSpf("recentSearchTerms"), ArrayList::class.java)?: arrayListOf<String>()
                recentSearchTerms.apply {
                    removeAt(position)
                    writeSpf("recentSearchTerms", this.toString())
                }
            }
        })
        recentSearchTermRVAdapter.setRecentSearchTerm(Gson().fromJson<ArrayList<String>>(getStrSpf("recentSearchTerms"), ArrayList::class.java)?: arrayListOf<String>())
        binding.searchRv.adapter = recentSearchTermRVAdapter
    }

    private fun setMyEventListener() {
        binding.searchSearchEt.setOnKeyListener { view, keyCode, keyEvent ->
            if(keyCode== KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                val keyword = (view as EditText).text.toString()

                val recentSearchTerms = Gson().fromJson<ArrayList<String>>(getStrSpf("recentSearchTerms"), ArrayList::class.java)?: arrayListOf<String>()
                recentSearchTerms.apply {
                    if (size >= 10) {
                        removeAt(lastIndex)
                    }
                    add(0, keyword)
                    writeSpf("recentSearchTerms", this.toString())
                }

                (requireActivity() as MainActivity).hideKeyboard(view)

                (view as EditText).text.clear()

                goSearchResultFragment(keyword)
            }

            false
        }
    }

    private fun goSearchResultFragment(keyword: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(keyword)
        findNavController().navigate(action)
    }

    fun popBackStack() {
        findNavController().popBackStack()
    }

    fun allDelete() {
        removeSpf("recentSearchTerms")
        recentSearchTermRVAdapter.clearData()
    }
}