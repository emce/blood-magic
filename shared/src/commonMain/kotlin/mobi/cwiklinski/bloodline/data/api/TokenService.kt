package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.model.Token

interface TokenService {

    fun addToken(
        id: String,
        date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    ): Flow<Either<Token, Throwable>>

}