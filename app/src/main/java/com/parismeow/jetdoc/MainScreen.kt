package com.parismeow.jetdoc

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parismeow.jetdoc.ui.theme.JetDocTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    val (file, setFile) = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            setFile(it)
        })
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "JetDoc") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { fileLauncher.launch(arrayOf("application/pdf")) }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(cutoutShape = CircleShape) {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Outlined.Menu, contentDescription = "Dummy Menu")
                }
                // The actions should be at the end of the BottomAppBar
                Spacer(Modifier.weight(weight = 1f, fill = true))
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Outlined.Search, contentDescription = "Dummy Search")
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Outlined.MoreVert, contentDescription = "Dummy Options")
                }
            }
        }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            file.let {
                Row(modifier = Modifier.fillMaxHeight()) {
                    Text(text = it.toString(), style = MaterialTheme.typography.body2)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun LightPreview() {
    JetDocTheme {
        MainScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DarkPreview() {
    JetDocTheme {
        MainScreen()
    }
}
