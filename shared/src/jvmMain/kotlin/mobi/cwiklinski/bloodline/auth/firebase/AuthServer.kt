package mobi.cwiklinski.bloodline.auth.firebase

import androidx.compose.ui.text.intl.Locale
import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.publicvalue.multiplatform.oidc.appsupport.webserver.Webserver
import org.publicvalue.multiplatform.oidc.flows.AuthCodeResult

class AuthServer(
    val createResponse: suspend RoutingContext.() -> Unit = {
        call.respondText(
            status = HttpStatusCode.OK,
            text = getRespondMessage(),
            contentType = ContentType.parse("text/html")
        )
    }) : Webserver {
    private var server: CIOApplicationEngine? = null

    override suspend fun startAndWaitForRedirect(port: Int, redirectPath: String)
            = buildServer(httpPort = port, redirectPath = redirectPath)

    override suspend fun stop() {
        server?.stop()
    }

    private fun buildServer(httpPort: Int, redirectPath: String): AuthCodeResult {
        var call: ApplicationRequest? = null
        embeddedServer(CIO, configure = {
            connector {
                host = "localhost"
                port = httpPort
            }
        }, module = {
            routing {
                get(redirectPath) {
                    createResponse()
                    call = this.call.request
                    server?.stop()
                }
            }
        }).apply {
            server = engine
            start(wait = true)
        }
        Logger.d("AuthServer Response: ${call?.queryParameters?.entries()}")
        val code = call?.queryParameters?.get("code")
        val state = call?.queryParameters?.get("state")
        return AuthCodeResult(code, state)
    }

    companion object {
        fun getRespondMessage(): String {
            val language = Locale.current.language
            if (language.startsWith("en")) {
                return getHTML("Authorization redirect successful", "You may now close this page and return to your app.")
            } else {
                return getHTML("Autoryzacja udana", "Można zamknąć to okno przeglądarki i wrócić do aplikacji.")
            }
        }

        private fun getHTML(title: String, message: String) = """
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="utf-8">
                    <title>$title</title>
                  </head>
                  <body>
                    <h1>$message</h1>
                  </body>
                </html>
            """.trimIndent()
    }
}

