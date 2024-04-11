package androidtechingdemo.billsmart.ui

sealed class ScreenSetting(val label: String) {
  object LOGIN : ScreenSetting("Login")

  object REGISTER : ScreenSetting("Register")

  object HOME : ScreenSetting("Home")
}
