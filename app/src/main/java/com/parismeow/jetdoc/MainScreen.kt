package com.parismeow.jetdoc

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Composable
fun MainScreen(
    docItem: Uri,
    onDocItemChange: (Uri) -> Unit,
    onDocUpload: (Context, String, (Float) -> Unit) -> Unit,
    snackbarMsg: String,
    hideSnackbar: () -> Unit,
    uploadProgress: Float,
    resetProgress: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1.05F * uploadProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            if (it != null) {
                onDocItemChange(it)
            }
        })
    val coroutineScope = rememberCoroutineScope()
    if (snackbarMsg.isNotBlank()) {
        LaunchedEffect(key1 = docItem, block = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = snackbarMsg)
                hideSnackbar()
            }
        })
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text(text = "JetDoc") }) },
        snackbarHost = {
            // reuse default SnackbarHost to have default animation and timing handling
            SnackbarHost(it) { data ->
                // custom snackbar with the custom border
                Snackbar(
                    modifier = Modifier.border(2.dp, MaterialTheme.colors.secondary),
                    snackbarData = data
                )
            }
        },
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
        LinearProgressIndicator(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            progress = animatedProgress,
            color = MaterialTheme.colors.primary,
        )
        Spacer(Modifier.requiredHeight(30.dp))
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
    onDocUpload: (Context, String, (Float) -> Unit) -> Unit
) {
    Column(modifier = modifier) {
        OneFile(docItem = docItem, onDocUpload = onDocUpload)
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@Composable
fun OneFile(
    modifier: Modifier = Modifier,
    docItem: Uri,
    onDocUpload: (Context, String, (Float) -> Unit) -> Unit
) {
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
                IconButton(
                    onClick = {
                        onDocUpload(context, file.name!!) {
                            println("Progress: $it%")
                        }
                    }
                ) {
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
