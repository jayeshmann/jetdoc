package com.parismeow.jetdoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.parismeow.jetdoc.ui.theme.JetDocTheme

class MainActivity : AppCompatActivity() {

    private val docViewModel by viewModels<DocViewModel>()

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
                        MainActivityScreen(docViewModel = docViewModel)
                    }
                }

            }
        }
    }

}

@Composable
fun MainActivityScreen(docViewModel: DocViewModel) {
    MainScreen(
        docItem = docViewModel.docItem,
        onDocItemChange = docViewModel::onDocItemChange,
        onDocUpload = docViewModel::onDocUpload
    )
}
