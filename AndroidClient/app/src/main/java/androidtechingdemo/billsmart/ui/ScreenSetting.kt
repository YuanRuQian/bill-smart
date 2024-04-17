package androidtechingdemo.billsmart.ui

sealed class ScreenSetting(val label: String) {
  object LOGIN : ScreenSetting("Login")

  object REGISTER : ScreenSetting("Register")

  object FRIENDS : ScreenSetting("Friends")

  object ADDNEWCONTACT : ScreenSetting("AddNewContact")

  object GROUPS : ScreenSetting("Groups")

  object EXPENSE : ScreenSetting("Expense")

  object ACTIVITY : ScreenSetting("Activity")

  object ACCOUNT : ScreenSetting("Account")
}
