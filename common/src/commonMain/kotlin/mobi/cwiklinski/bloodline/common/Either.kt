package mobi.cwiklinski.bloodline.common

sealed class Either<out L, out R> {
    data class Left<out T>(val value: T) : Either<T, Nothing>()
    data class Right<out T>(val value: T) : Either<Nothing, T>()

    fun <S> mapRight(mapping: (R) -> S): Either<L, S> = when (this) {
        is Left<L> -> this
        is Right<R> -> Right(mapping(value))
    }

    fun <S> mapLeft(mapping: (L) -> S): Either<S, R> = when (this) {
        is Left<L> -> Left(mapping(value))
        is Right<R> -> this
    }
}

inline fun <L, R, T> Either<L, R>.fold(left: (L) -> T, right: (R) -> T): T =
    when (this) {
        is Either.Left -> left(value)
        is Either.Right -> right(value)
    }

inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
    fold(left = { this as Either.Left }, right = f)

inline fun <L, R, T> Either<L, R>.map(f: (R) -> T): Either<L, T> =
    flatMap { Either.Right(f(it)) }