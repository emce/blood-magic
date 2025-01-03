package mobi.cwiklinski.bloodline.auth.firebase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.path
import mobi.cwiklinski.bloodline.common.Either

class AuthServer(private val httpClient: HttpClient) {

    suspend fun authRequest(authCode: String): Either<String, Error> {
        httpClient.use { client ->
            val response = client.post(getUrl("auth")) {
                contentType(ContentType.Application.Json)
                setBody(authCode)
            }
            return when (response.status.value) {
                in 200..299 -> {
                    Either.Left(response.body())
                }

                else -> Either.Right(Error())
            }
        }
    }

    suspend fun profileGetRequest(jwtTokenValue: String): Either<String, Error> {
        httpClient.use { client ->
            val response = client.get(getUrl("profile")) {
                headers {
                    append("Authorization", "Bearer $jwtTokenValue")
                }
            }
            return if (response.status == HttpStatusCode.Accepted) {
                Either.Left(response.body())
            } else {
                Either.Right(Error())
            }
        }
    }

    private fun getUrl(path: String) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = "localhost",
        port = 8080
    ).path(path).toString()
}

