@file:Suppress(
  "INVISIBLE_MEMBER",
  "INVISIBLE_REFERENCE",
  "EXPOSED_PARAMETER_TYPE"
)

package io.github.xxfast.decompose.router.app.utils // WORKAROUND: ComposeWindow and ComposeLayer are internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeWindow
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.HTMLTitleElement

private const val CANVAS_ELEMENT_ID = "ComposeTarget" // Hardwired into ComposeWindow

/**
 * A Skiko/Canvas-based top-level window using the browser's entire viewport. Supports resizing.
 * Taken from https://git.chrishatton.org/chris/homepage/-/blob/master/src/jsMain/kotlin/org/chrishatton/homepage/presentation/view/BrowserViewportWindow.kt
 * Credit to 'Oliver O' (https://github.com/OliverO2)
 */
fun BrowserViewportWindow(
  title: String = "Untitled",
  content: @Composable ComposeWindow.() -> Unit
) {
  val htmlHeadElement = document.head!!
  htmlHeadElement.appendChild(
    (document.createElement("style") as HTMLStyleElement).apply {
      type = "text/css"
      appendChild(
        document.createTextNode(
          """
                    html, body {
                        overflow: hidden;
                        margin: 0 !important;
                        padding: 0 !important;
                    }

                    #$CANVAS_ELEMENT_ID {
                        outline: none;
                    }
                    """.trimIndent()
        )
      )
    }
  )

  fun HTMLCanvasElement.fillViewportSize() {
    setAttribute("width", "${window.innerWidth}")
    setAttribute("height", "${window.innerHeight}")
  }

  val canvas = (document.getElementById(CANVAS_ELEMENT_ID) as HTMLCanvasElement).apply {
    fillViewportSize()
  }

  ComposeWindow().apply {
    window.addEventListener("resize", {
      canvas.fillViewportSize()
      layer.layer.attachTo(canvas)
      layer.layer.needRedraw()
      layer.setSize(canvas.width, canvas.height)
    })

    // WORKAROUND: ComposeWindow does not implement `setTitle(title)`
    val htmlTitleElement = (
      htmlHeadElement.getElementsByTagName("title").item(0)
        ?: document.createElement("title").also { htmlHeadElement.appendChild(it) }
      ) as HTMLTitleElement
    htmlTitleElement.textContent = title

    setContent {
      content(this)
    }
  }
}
