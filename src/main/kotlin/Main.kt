// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() = Window {
    val composableScope = rememberCoroutineScope()

    Game.init()
    composableScope.launch {
        var lastTime = System.currentTimeMillis()
        val requiredDelay = 20L // ms
        while (true) {
            val newTime = System.currentTimeMillis()
            val deltaMillis = newTime - lastTime
            lastTime = newTime
            Game.update(deltaMillis / 1000f)
            println("REquired dely: $requiredDelay, deltamillis: $deltaMillis")
            delay(requiredDelay)
        }
    }
    MaterialTheme {
        val requester = remember { FocusRequester() }
        val frameTime = Game.frameTime.collectAsState()
        Canvas(modifier = Modifier.fillMaxSize()
            .focusRequester(requester)
            .focusable()
            .onKeyEvent {
                println("KeyEvent")
                val type = it.type
                val code = it.key.keyCode
                when (type) {
                    KeyEventType.KeyUp -> Game.onKeyUp(code)
                    KeyEventType.KeyDown -> Game.onKeyDown(code)
                }
                false
            }) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            if (frameTime.value > 0) {
                Game.render(this)
            }
        }
        LaunchedEffect(Unit) {
            requester.requestFocus()
        }
    }
}