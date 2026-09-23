package com.jairllalen.reclamos_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jairllalen.reclamos_android.ui.entitytype.EntityTypeListScreen
import com.jairllalen.reclamos_android.ui.entitytype.EntityTypeFormScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ENTITY_TYPE_LIST
    ) {

        // =====================================================
        // LISTADO
        // =====================================================

        composable(
            route = Routes.ENTITY_TYPE_LIST
        ) {

            EntityTypeListScreen(
                onCreate = {
                    navController.navigate(
                        Routes.ENTITY_TYPE_FORM
                    )
                },

                onEdit = { id ->
                    navController.navigate(
                        "entity_type_form/$id"
                    )
                }
            )
        }

        // =====================================================
        // NUEVO
        // =====================================================

        composable(
            route = Routes.ENTITY_TYPE_FORM
        ) {

            EntityTypeFormScreen(
                entityTypeId = null,

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // EDITAR
        // =====================================================

        composable(
            route = Routes.ENTITY_TYPE_FORM_WITH_ID,

            arguments = listOf(
                navArgument("entityTypeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val entityTypeId =
                backStackEntry.arguments?.getInt("entityTypeId")

            EntityTypeFormScreen(
                entityTypeId = entityTypeId,

                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}