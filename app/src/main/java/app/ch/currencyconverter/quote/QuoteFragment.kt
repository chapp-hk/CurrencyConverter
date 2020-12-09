package app.ch.currencyconverter.quote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.recyclerview.RecyclerViewAdapter
import app.ch.currencyconverter.databinding.FragmentQuoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QuoteFragment : Fragment(R.layout.fragment_quote) {

    private val viewModel by viewModels<QuoteViewModel>()
    private val adapter = RecyclerViewAdapter<QuoteListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.apply {
                getQuotes()
                quoteList.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentQuoteBinding.bind(view).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.fragment = this
            it.viewModel = viewModel
            it.recyclerView.adapter = adapter
        }
    }

    fun navigateToCurrency() {
        findNavController().navigate(R.id.to_currency)
    }
}
