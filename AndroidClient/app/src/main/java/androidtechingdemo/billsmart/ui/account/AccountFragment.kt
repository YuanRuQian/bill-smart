package androidtechingdemo.billsmart.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidtechingdemo.billsmart.databinding.FragmentAccountBinding
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AccountFragment : Fragment() {

  private lateinit var auth: FirebaseAuth

  private lateinit var binding: FragmentAccountBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentAccountBinding.inflate(inflater, container, false)
    auth = Firebase.auth
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      buttonSignOut.setOnClickListener {
        signOut()
      }
    }
  }

  private fun signOut() {
    val displayName = auth.currentUser?.displayName
    auth.signOut()
    Toast.makeText(
      requireContext(),
      "Goodbye, $displayName!",
      Toast.LENGTH_LONG,
    ).show()
  }
}
