package androidtechingdemo.billsmart.database

sealed class CollectionNames(val name: String) {

  object USERS : CollectionNames("users")

  object FRIENDS : CollectionNames("friends")
}
