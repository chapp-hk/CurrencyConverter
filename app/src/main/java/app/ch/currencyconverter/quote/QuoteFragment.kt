package app.ch.currencyconverter.quote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.ch.currencyconverter.BR
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.Constants.ERROR_NETWORK
import app.ch.currencyconverter.core.Constants.KEY_CODE
import app.ch.currencyconverter.core.Constants.REQUEST_CURRENCY
import app.ch.currencyconverter.core.recyclerview.RecyclerViewAdapter
import app.ch.currencyconverter.databinding.FragmentQuoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QuoteFragment : Fragment(R.layout.fragment_quote) {

    private val viewModel by viewModels<QuoteViewModel>()
    private val adapter = RecyclerViewAdapter<QuoteListItem>(BR.listItem)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.apply {
                getQuotes()
                quoteList.collectLatest(adapter::submitList)
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

        registerEventObserver()
    }

    fun navigateToCurrency() {
        findNavController().navigate(R.id.to_currency)
    }

    private fun registerEventObserver() {
        setFragmentResultListener(REQUEST_CURRENCY, ::handleFragmentResult)

        viewModel.errorEvent.observe(
            viewLifecycleOwner,
            { it.runIfNotConsumed(::showError) }
        )
    }

    private fun handleFragmentResult(requestKey: String, bundle: Bundle) {
        when (requestKey) {
            REQUEST_CURRENCY -> viewModel.updateCurrencyCode(bundle.getString(KEY_CODE, ""))
        }
    }

    private fun showError(errorCode: Int) {
        Toast.makeText(
            requireContext(),
            when (errorCode) {
                ERROR_NETWORK -> R.string.error_network
                else -> R.string.error_api
            },
            Toast.LENGTH_LONG
        ).show()
    }
}
