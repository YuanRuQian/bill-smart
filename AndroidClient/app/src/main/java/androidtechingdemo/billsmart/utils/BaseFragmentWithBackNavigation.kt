package androidtechingdemo.billsmart.utils

import android.os.Bundle
import android.util.Log
import androidtechingdemo.billsmart.ui.MainActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

open class BaseFragmentWithBackNavigation : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    requireActivity().onBackPressedDispatcher.addCallback(this) {
//      onBackPressed()
//    }
  }

  // Override this method to handle back button press event
  open fun onBackPressed() {
    Log.d("BaseFragment", "onBackPressed")
    val prevScreen = findNavController().previousBackStackEntry
    Log.d("BaseFragment", "prevScreen: ${prevScreen?.destination}")
    (requireActivity() as MainActivity).changeBottomNavigationCheckedItem(
      prevScreen?.destination?.route ?: ""
    )
    findNavController().popBackStack()
  }
}
