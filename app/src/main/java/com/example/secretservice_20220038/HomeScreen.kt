package com.example.secretservice_20220038

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.secretservice_20220038.models.Incident
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

@Composable
fun HomeScreen(realm: Realm) {
    val incidents = realm.query<Incident>().find()
    var selectedIncident by remember { mutableStateOf<Incident?>(null) }
    var showAddIncidentScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showAddIncidentScreen) {
        AddIncidentScreen(
            onSave = { incident ->
                realm.writeBlocking {
                    copyToRealm(incident)
                }
                showAddIncidentScreen = false
            },
            onCancel = { showAddIncidentScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                Text(
                    text = stringResource(R.string.title),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddIncidentScreen = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add incident")
                }
            }
        ) { padding ->
            when {
                incidents.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "There are no incidents",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        items(items = incidents) { incident ->
                            IncidentCard(
                                incident,
                                onClick = { selectedIncident = incident }
                            )
                        }
                    }
                }
            }
        }
    }

    selectedIncident?.let { incident ->
        IncidentDialog(
            incident,
            onDelete = {
                realm.writeBlocking {
                    val liveIncident = findLatest(incident)
                    if (liveIncident != null) {
                        delete(liveIncident)
                    }
                }
                selectedIncident = null
            },
            onDismiss = { selectedIncident = null }
        )
    }
}

@Composable
fun IncidentCard(incident: Incident, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row (
            modifier = Modifier.width(255.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            incident.image?.let {
                Image(
                    bitmap = BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap(),
                    contentDescription = incident.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp),
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = incident.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = incident.date,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun IncidentDialog(incident: Incident, onDelete: () -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Dialog(onDismissRequest = {
        mediaPlayer?.release()
        onDismiss()
    }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(8.dp)
        ) {
            Column {
                incident.image?.let {
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap(),
                        contentDescription = incident.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = incident.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = incident.date,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = incident.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                incident.audioPath?.let { audioPath ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            mediaPlayer?.release()
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(context, Uri.parse(audioPath))
                                prepare()
                                start()
                            }
                        }) {
                            Text("Play Audio")
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                            .size(40.dp),
                        onClick = {
                            mediaPlayer?.release()
                            onDelete()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        mediaPlayer?.release()
                        onDismiss()
                    }) {
                        Text(text = "Close")
                    }
                }
            }
        }
    }
}
