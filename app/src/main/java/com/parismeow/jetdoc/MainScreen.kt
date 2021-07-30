package com.parismeow.jetdoc

import android.content.res.Configuration
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.parismeow.jetdoc.data.UploadAPI
import com.parismeow.jetdoc.ui.theme.JetDocTheme
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    val (fileUri, setFile) = remember { mutableStateOf<Uri>(Uri.EMPTY) }
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
        }) { innerPadding ->
        if (fileUri != Uri.EMPTY) {
            BodyContent(modifier = Modifier.padding(innerPadding), fileUri = fileUri)
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier, fileUri: Uri) {
    Column(modifier = modifier) {
        OneFile(fileUri = fileUri)
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@Composable
fun OneFile(modifier: Modifier = Modifier, fileUri: Uri) {
    val context = LocalContext.current
    val uploadAPI = UploadAPI()

    val file = DocumentFile.fromSingleUri(context, fileUri)
    if (file != null) {

        /*val fileBody = RequestBody.create(MediaType.parse(file.type!!), file!!)
        val body = MultipartBody.Builder().addFormDataPart("name", file.name!!)
            .addFormDataPart("sampleFile", file.name!!, fileBody).build()*/
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.PictureAsPdf, contentDescription = "PDF")
            file.name?.let { it1 ->
                Text(
                    text = it1,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .weight(1f),

                    )
            }
            IconButton(onClick = { /*uploadAPI.uploadDoc(body)*/ }) {
                Icon(
                    imageVector = Icons.Outlined.UploadFile,
                    contentDescription = "Upload File"
                )
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun LightBodyPreview() {
    JetDocTheme {
        OneFile(fileUri = Uri.EMPTY)
    }
}
