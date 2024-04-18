package androidtechingdemo.billsmart.utils

import android.os.Bundle
import android.util.Log
import androidtechingdemo.billsmart.ui.MainActivity
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

open class BaseFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Ensure the fragment can receive key events
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      // Handle back button press event
      onBackPressed()
    }
  }

  // Override this method to handle back button press event
  open fun onBackPressed() {
    Log.d("BaseFragment", "onBackPressed")
    val prevScreen = findNavController().previousBackStackEntry
    Log.d("BaseFragment", "prevScreen: ${prevScreen?.destination}")
    (requireActivity() as MainActivity).changeBottomNavigationCheckedItem(prevScreen?.destination?.route ?: "")
    findNavController().popBackStack()
  }
}

