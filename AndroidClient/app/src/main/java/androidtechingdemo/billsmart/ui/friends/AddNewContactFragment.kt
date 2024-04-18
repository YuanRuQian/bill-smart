package androidtechingdemo.billsmart.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidtechingdemo.billsmart.database.addNewFriendByEmail
import androidtechingdemo.billsmart.databinding.FragmentAddNewContactBinding
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.isValidEmail
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AddNewContactFragment : Fragment() {
  private lateinit var binding: FragmentAddNewContactBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentAddNewContactBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      btnAdd.isEnabled = false

      btnCancel.setOnClickListener {
        val navController = findNavController()
        navController.navigate(ScreenSetting.FRIENDS.label)
      }

      editTextEmail.addTextChangedListener {
        val isEmailValid = isValidEmail(it.toString())
        if (!isEmailValid) {
          textInputLayoutEmail.error = "Invalid email"
        } else {
          textInputLayoutEmail.error = null
        }
        btnAdd.isEnabled = isEmailValid
      }

      btnAdd.setOnClickListener {
        val email = editTextEmail.text.toString()
        addNewFriendByEmail(email).addOnCompleteListener {
          if (it.isSuccessful) {
            val navController = findNavController()
            navController.navigate(ScreenSetting.FRIENDS.label)
            Toast.makeText(context, it.result, Toast.LENGTH_LONG).show()
          } else {
            Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
          }
        }
      }
    }
  }
}
