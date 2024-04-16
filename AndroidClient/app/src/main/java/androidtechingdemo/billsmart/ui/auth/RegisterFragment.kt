package androidtechingdemo.billsmart.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidtechingdemo.billsmart.databinding.FragmentRegisterBinding
import androidtechingdemo.billsmart.ui.MainActivity
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.isValidEmail
import androidtechingdemo.billsmart.utils.isValidPassword
import androidtechingdemo.billsmart.utils.isValidUsername
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
  private lateinit var auth: FirebaseAuth

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

    with(binding) {
      buttonRegister.isEnabled = false

      editTextUsername.addTextChangedListener {
        val isUsernameValid = isValidUsername(it.toString())
        val isEmailValid = isValidEmail(editTextEmail.text.toString())
        val isPasswordValid = isValidPassword(editTextPassword.text.toString())
        if (!isUsernameValid) {
          textInputLayoutUsername.error = "Username cannot contain spaces"
        } else {
          textInputLayoutUsername.error = null
        }
        buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
      }

      editTextEmail.addTextChangedListener {
        val isUsernameValid = isValidUsername(editTextUsername.text.toString())
        val isEmailValid = isValidEmail(it.toString())
        val isPasswordValid = isValidPassword(editTextPassword.text.toString())
        if (!isEmailValid) {
          textInputLayoutEmail.error = "Invalid email"
        } else {
          textInputLayoutEmail.error = null
        }
        buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
      }

      editTextPassword.addTextChangedListener {
        val isUsernameValid = isValidUsername(editTextUsername.text.toString())
        val isEmailValid = isValidEmail(editTextEmail.text.toString())
        val isPasswordValid = isValidPassword(it.toString())
        if (!isPasswordValid) {
          textInputLayoutPassword.error = "Password has to be at least 6 characters long"
        } else {
          textInputLayoutPassword.error = null
        }
        buttonRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
      }

      buttonRegister.setOnClickListener {
        val username = editTextUsername.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        createAccount(username, email, password)
      }

      textViewSignInLink.setOnClickListener {
        val navController = findNavController()
        navController.navigate(ScreenSetting.LOGIN.label)
      }
    }

    auth = FirebaseAuth.getInstance()
  }

  private fun createAccount(username: String, email: String, password: String) {
    binding.buttonRegister.isEnabled = false

    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(requireActivity()) { task ->
        if (task.isSuccessful) {
          val user = auth.currentUser

          (requireActivity() as MainActivity).addNewUserInfo(
            username, email, user!!.uid,
          ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
              Toast.makeText(context, task.result, Toast.LENGTH_LONG).show()
            } else {
              Toast.makeText(
                context,
                task.exception?.message,
                Toast.LENGTH_LONG,
              ).show()
            }
          }
        } else {
          Toast.makeText(
            context,
            task.exception?.message,
            Toast.LENGTH_LONG,
          ).show()
        }
      }

    binding.buttonRegister.isEnabled = true
  }
}
