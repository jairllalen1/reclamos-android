package com.jairllalen.reclamos_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jairllalen.reclamos_android.ui.navigation.AppNavigation
import com.jairllalen.reclamos_android.ui.theme.ReclamosAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ReclamosAndroidTheme {

                AppNavigation()
            }
        }
    }
}