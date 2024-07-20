package com.example.secretservice_20220038

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.realm.kotlin.Realm

@Composable
fun ProfileScreen(realm: Realm) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Text(
                text = stringResource(R.string.title_profile),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.my_name),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(92.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.my_name),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = stringResource(id = R.string.my_id),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
            item {
                Text(
                    text = stringResource(id = R.string.message),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    text = stringResource(id = R.string.author),
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Button(onClick = { showConfirmDialog = true }) {
                        Text(text = "Clear all data")
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Emergency cleanup") },
            text = { Text(text = "Are you sure you want to delete all data? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        realm.writeBlocking { deleteAll() }
                        showConfirmDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
