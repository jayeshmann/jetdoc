package com.parismeow.jetdoc

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Files
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.parismeow.jetdoc.ui.theme.JetDocTheme

class MainActivity : AppCompatActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetDocTheme {
                val multiplePermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                )
                RequestPermissions(
                    multiplePermissionsState = multiplePermissionsState,
                    navigateToSettingsScreen =
                    {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                        )
                    }
                ) {
                    Surface(color = MaterialTheme.colors.background) {
                        MainScreen(onFABClick = { openFile(Uri.parse("/storage/emulated/0/")) })
                    }
                }

            }
        }
    }

    // Request code for selecting a PDF document.
    private fun openFile(
        pickerInitialUri: Uri = Uri.parse("/storage/emulated/0/")
    ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }
        println("intent:$intent")
        println("PICK_PDF:$PICK_PDF_FILE")
        startActivityForResult(intent, PICK_PDF_FILE)
    }
}

const val PICK_PDF_FILE = 2


@Composable
fun MainScreen(modifier: Modifier = Modifier, onFABClick: () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "JetDoc") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onFABClick() }) {
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

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun LightPreview() {
    JetDocTheme {
        MainScreen() {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DarkPreview() {
    JetDocTheme {
        MainScreen() {}
    }
}
