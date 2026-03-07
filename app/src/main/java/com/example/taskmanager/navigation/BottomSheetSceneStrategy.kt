package com.example.taskmanager.navigation

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

class SheetConfiguration @OptIn(ExperimentalMaterial3Api::class) constructor(
    val properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    val dragHandle: @Composable (() -> Unit)? = null,
    val skipPartiallyExpanded: Boolean = false,
    val modifier: Modifier = Modifier,
)


@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val sheetConfig: SheetConfiguration,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = sheetConfig.skipPartiallyExpanded
        )

        ModalBottomSheet(
            onDismissRequest = onBack,
            sheetState = sheetState,
            properties = sheetConfig.properties,
            dragHandle = sheetConfig.dragHandle,
            modifier = sheetConfig.modifier,
        ) {
            entry.Content()
        }
    }
}


class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()
        val config = lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? SheetConfiguration
        return config?.let {
            @Suppress("UNCHECKED_CAST")
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                sheetConfig = it,
                onBack = onBack
            )
        }
    }

    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        fun bottomSheet(
            properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
            dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
            skipPartiallyExpanded: Boolean = false,
            modifier: Modifier = Modifier
        ): Map<String, Any> = mapOf(
            BOTTOM_SHEET_KEY to SheetConfiguration(
                properties,
                dragHandle,
                skipPartiallyExpanded,
                modifier
            )
        )

        internal const val BOTTOM_SHEET_KEY = "bottomsheet"
    }
}