package androidtechingdemo.billsmart.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.databinding.FragmentLoginBinding
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.isValidEmail
import androidtechingdemo.billsmart.utils.isValidPassword
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

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

    binding.buttonLogin.isEnabled = false

    binding.editTextEmail.addTextChangedListener {
      val isEmailValid = isValidEmail(it.toString())
      val isPasswordValid = isValidPassword(binding.editTextPassword.text.toString())
      if (!isEmailValid) {
        binding.textInputLayoutEmail.error = "Invalid email"
      } else {
        binding.textInputLayoutEmail.error = null
      }
      binding.buttonLogin.isEnabled = isEmailValid && isPasswordValid
    }

    binding.editTextPassword.addTextChangedListener {
      val isEmailValid = isValidEmail(binding.editTextEmail.text.toString())
      val isPasswordValid = isValidPassword(it.toString())
      if (!isPasswordValid) {
        binding.textInputLayoutPassword.error = "Password has to be at least 6 characters long"
      } else {
        binding.textInputLayoutPassword.error = null
      }
      binding.buttonLogin.isEnabled = isEmailValid && isPasswordValid
    }

    binding.buttonLogin.setOnClickListener {
      // TODO: Implement login logic
    }

    binding.textViewRegisterLink.setOnClickListener {
      val navController = findNavController()
      navController.navigate(ScreenSetting.REGISTER.label)
    }
  }
}
