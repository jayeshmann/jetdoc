package com.parismeow.jetdoc

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import kotlin.reflect.KFunction1

@Composable
fun MainScreen(
    docItem: Uri,
    onDocItemChange: (Uri) -> Unit,
    onDocUpload: (Context, String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            onDocItemChange(it)
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
        }) { innerPadding ->
        if (docItem != Uri.EMPTY) {
            BodyContent(
                modifier = Modifier.padding(innerPadding),
                docItem = docItem,
                onDocUpload = onDocUpload
            )
        }
    }
}

@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    docItem: Uri,
    onDocUpload: (Context, String) -> Unit
) {
    Column(modifier = modifier) {
        OneFile(docItem = docItem, onDocUpload = onDocUpload)
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@Composable
fun OneFile(modifier: Modifier = Modifier, docItem: Uri, onDocUpload: (Context, String) -> Unit) {
    val context = LocalContext.current
    val file = DocumentFile.fromSingleUri(context, docItem)
    if (file != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            file.name?.let { it1 ->
                Icon(imageVector = Icons.Outlined.PictureAsPdf, contentDescription = "PDF")
                Text(
                    text = it1,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .weight(1f),

                    )
                IconButton(onClick = { onDocUpload(context, file.name!!) }) {
                    Icon(
                        imageVector = Icons.Outlined.UploadFile,
                        contentDescription = "Upload File"
                    )
                }
            }
        }
    }
}


/*@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
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
}*/
