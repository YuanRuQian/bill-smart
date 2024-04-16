package androidtechingdemo.billsmart.ui.friends

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.databinding.FragmentFriendsBinding
import androidtechingdemo.billsmart.ui.MainActivity
import androidx.fragment.app.Fragment

class FriendsFragment : Fragment() {
  private lateinit var binding: FragmentFriendsBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentFriendsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      getHelloMessageButton.setOnClickListener {
        (requireActivity() as MainActivity).addMessage("Test local function call at ${System.currentTimeMillis()}")
          .addOnSuccessListener { result ->
            Log.d("FriendsFragment", "Hello world message: $result")
            friendsTitle.text = result
          }
          .addOnFailureListener { exception ->
            Log.d("FriendsFragment", "Error getting hello world message", exception)
            friendsTitle.error = exception.message
          }
      }
    }
  }
}
