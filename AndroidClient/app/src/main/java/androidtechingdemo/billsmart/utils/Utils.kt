package androidtechingdemo.billsmart.utils

fun isValidEmail(email: String): Boolean {
  return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
  return password.length >= 6
}

fun isValidUsername(username: String): Boolean {
  return Regex("^[^\\s]*\\S[^\\s]*\$").matches(username)
}
