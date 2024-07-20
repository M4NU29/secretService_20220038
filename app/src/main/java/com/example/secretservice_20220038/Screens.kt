package com.example.secretservice_20220038

sealed class Screens(val screen: String) {
    data object Home : Screens("home")
    data object AddIncident : Screens("add")
    data object Profile : Screens("profile")
}
