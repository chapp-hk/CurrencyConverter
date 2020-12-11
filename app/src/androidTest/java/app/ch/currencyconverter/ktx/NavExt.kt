package app.ch.currencyconverter.ktx

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.IdlingRegistry
import app.ch.currencyconverter.LoadingIdlingResource
import app.ch.currencyconverter.R
import app.ch.currencyconverter.core.lifecycle.LoadingState

inline fun <reified T : Fragment, reified V> launchNavFragment(
    navController: TestNavHostController,
    withIdling: Boolean = true,
    crossinline action: (Fragment) -> Unit = {}
) where V : ViewModel, V : LoadingState {
    launchFragmentInHiltContainer<T> {
        navController.setGraph(R.navigation.main_graph)
        viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                // The fragment’s view has just been created
                Navigation.setViewNavController(requireView(), navController)
            }
        }

        if (withIdling) {
            val viewModel = ViewModelProvider(this).get(V::class.java)
            IdlingRegistry.getInstance()
                .register(LoadingIdlingResource(viewModel))
        }

        action(this)
    }
}
