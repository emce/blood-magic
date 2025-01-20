package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.TokenService
import mobi.cwiklinski.bloodline.domain.model.Token

class TokenServiceImplementation : TokenService {

    private val _memory = mutableListOf<Token>()

    init {
        _memory.addAll(DummyData.TOKENS)
    }

    override fun addToken(id: String, date: LocalDate): Flow<Either<Token, Throwable>> {
        val newToken = Token(id, date)
        _memory.add(newToken)
        return flowOf(Either.Left(newToken))
    }
}