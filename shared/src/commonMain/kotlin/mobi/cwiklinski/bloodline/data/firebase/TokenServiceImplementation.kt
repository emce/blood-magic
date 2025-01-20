package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.TokenService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseToken
import mobi.cwiklinski.bloodline.domain.model.Token

class TokenServiceImplementation(private val db: FirebaseDatabase, val auth: FirebaseAuth) : TokenService {

    override fun addToken(id: String, date: LocalDate) = flow<Either<Token, Throwable>> {
        try {
            val mainRef = db.reference("token").child(auth.currentUser?.uid ?: "-")
            val newRef = mainRef.child(id)
            try {
                newRef.setValue(
                    FirebaseToken(
                        id = id,
                        year = date.year,
                        month = date.monthNumber,
                        day = date.dayOfMonth
                    )
                )
            } finally {
                emit(Either.Left(Token(id, date)))
            }
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }
}