package com.example.secretservice_20220038

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.secretservice_20220038.App.Companion.realm
import com.example.secretservice_20220038.ui.theme.SecretService_20220038Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecretService_20220038Theme {
                Main()
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val navigationController = rememberNavController()
    var selected by rememberSaveable { mutableIntStateOf(R.string.navbar_home) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                NavigationBarItem(
                    selected = selected == R.string.navbar_home,
                    onClick = {
                        selected = R.string.navbar_home
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = if (selected == R.string.navbar_home) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.navbar_home),
                            color = if (selected == R.string.navbar_home) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                NavigationBarItem(
                    selected = selected == R.string.navbar_add,
                    onClick = {
                        selected = R.string.navbar_add
                        navigationController.navigate(Screens.AddIncident.screen) {
                            popUpTo(0)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = if (selected == R.string.navbar_add) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.navbar_add),
                            color = if (selected == R.string.navbar_add) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                NavigationBarItem(
                    selected = selected == R.string.navbar_profile,
                    onClick = {
                        selected = R.string.navbar_profile
                        navigationController.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = if (selected == R.string.navbar_profile) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.navbar_profile),
                            color = if (selected == R.string.navbar_profile) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }
    ) {padding ->
        NavHost(
            navController = navigationController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screens.Home.screen) { HomeScreen(realm) }
            composable(Screens.AddIncident.screen) {
                AddIncidentScreen(
                    onSave = { incident ->
                        realm.writeBlocking {
                            copyToRealm(incident)
                        }
                    }
                )
            }
            composable(Screens.Profile.screen) { ProfileScreen(realm) }
        }
    }
}
