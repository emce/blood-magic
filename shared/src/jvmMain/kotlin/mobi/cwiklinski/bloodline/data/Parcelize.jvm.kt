package mobi.cwiklinski.bloodline.data

actual interface Parcelable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
actual annotation class IgnoredOnParcel

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
actual annotation class Parcelize

actual interface Parceler<P>

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
actual annotation class TypeParceler<T, P : Parceler<in T>>