package androidtechingdemo.billsmart.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.databinding.FragmentRegisterBinding
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.isValidEmail
import androidtechingdemo.billsmart.utils.isValidPassword
import androidtechingdemo.billsmart.utils.isValidUsername
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment() {

  private lateinit var binding: FragmentRegisterBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentRegisterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.buttonRegister.isEnabled = false

    binding.editTextUsername.addTextChangedListener {
      val isUsernameValid = isValidUsername(it.toString())
      val isEmailValid = isValidEmail(binding.editTextEmail.text.toString())
      val isPasswordValid = isValidPassword(binding.editTextPassword.text.toString())
      if (!isUsernameValid) {
        binding.textInputLayoutUsername.error = "Username cannot contain spaces"
      } else {
        binding.textInputLayoutUsername.error = null
      }
      binding.buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
    }

    binding.editTextEmail.addTextChangedListener {
      val isUsernameValid = isValidUsername(binding.editTextUsername.text.toString())
      val isEmailValid = isValidEmail(it.toString())
      val isPasswordValid = isValidPassword(binding.editTextPassword.text.toString())
      if (!isEmailValid) {
        binding.textInputLayoutEmail.error = "Invalid email"
      } else {
        binding.textInputLayoutEmail.error = null
      }
      binding.buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
    }

    binding.editTextPassword.addTextChangedListener {
      val isUsernameValid = isValidUsername(binding.editTextUsername.text.toString())
      val isEmailValid = isValidEmail(binding.editTextEmail.text.toString())
      val isPasswordValid = isValidPassword(it.toString())
      if (!isPasswordValid) {
        binding.textInputLayoutPassword.error = "Password has to be at least 6 characters long"
      } else {
        binding.textInputLayoutPassword.error = null
      }
      binding.buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
    }

    binding.buttonRegister.setOnClickListener {
      // TODO: Implement registration logic
    }

    binding.textViewSignInLink.setOnClickListener {
      val navController = findNavController()
      navController.navigate(ScreenSetting.LOGIN.label)
    }
  }
}
