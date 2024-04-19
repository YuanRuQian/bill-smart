package androidtechingdemo.billsmart.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidtechingdemo.billsmart.databinding.FragmentLoginBinding
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.BaseFragmentWithBackNavigation
import androidtechingdemo.billsmart.utils.isValidEmail
import androidtechingdemo.billsmart.utils.isValidPassword
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragmentWithBackNavigation() {
  private lateinit var auth: FirebaseAuth

  private lateinit var binding: FragmentLoginBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentLoginBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      buttonLogin.isEnabled = false

      editTextEmail.addTextChangedListener {
        val isEmailValid = isValidEmail(it.toString())
        val isPasswordValid = isValidPassword(editTextPassword.text.toString())
        if (!isEmailValid) {
          textInputLayoutEmail.error = "Invalid email"
        } else {
          textInputLayoutEmail.error = null
        }
        buttonLogin.isEnabled = isEmailValid && isPasswordValid
      }

      editTextPassword.addTextChangedListener {
        val isEmailValid = isValidEmail(editTextEmail.text.toString())
        val isPasswordValid = isValidPassword(it.toString())
        if (!isPasswordValid) {
          textInputLayoutPassword.error = "Password has to be at least 6 characters long"
        } else {
          textInputLayoutPassword.error = null
        }
        buttonLogin.isEnabled = isEmailValid && isPasswordValid
      }

      buttonLogin.setOnClickListener {
        val email = editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        signIn(email, password)
      }

      textViewRegisterLink.setOnClickListener {
        val navController = findNavController()
        navController.navigate(ScreenSetting.REGISTER.label)
      }
    }

    auth = FirebaseAuth.getInstance()
  }

  private fun signIn(email: String, password: String) {
    binding.buttonLogin.isEnabled = false
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(requireActivity()) { task ->
        if (!task.isSuccessful) {
          Toast.makeText(
            context,
            task.exception?.message,
            Toast.LENGTH_LONG,
          ).show()
        }
      }

    binding.buttonLogin.isEnabled = true
  }
}
