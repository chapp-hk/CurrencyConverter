package app.ch.currencyconverter.ktx

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import app.ch.currencyconverter.R

inline fun <reified T : Fragment> launchNavFragment(
    navController: TestNavHostController,
    crossinline action: (Fragment) -> Unit = {}
) {
    launchFragmentInHiltContainer<T> {
        navController.setGraph(R.navigation.main_graph)
        viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                // The fragment’s view has just been created
                Navigation.setViewNavController(requireView(), navController)
            }
        }

        action(this)
    }
}
