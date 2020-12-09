package app.ch.currencyconverter.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.recyclerview.RecyclerViewAdapter
import app.ch.currencyconverter.databinding.DialogCurrencyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CurrencyDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<CurrencyViewModel>()
    private val adapter = RecyclerViewAdapter<CurrencyListItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DialogCurrencyBinding.bind(view).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.recyclerView.adapter = adapter
        }

        registerEventObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.apply {
                getCurrencies()
                currencyList.collectLatest(adapter::submitList)
            }
        }
    }

    private fun registerEventObserver() {
        viewModel.currencySelectEvent.observe(
            viewLifecycleOwner,
            { it.runIfNotConsumed(::currencySelected) }
        )
    }

    private fun currencySelected(code: String) {
        setFragmentResult("currencyCode", bundleOf("code" to code))
        dismiss()
    }
}
