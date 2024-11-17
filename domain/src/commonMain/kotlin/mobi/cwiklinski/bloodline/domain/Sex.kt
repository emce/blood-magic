package mobi.cwiklinski.bloodline.domain

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SexStringSerializer::class)
enum class Sex(val sex: String) {
    MALE("m"),
    FEMALE("f");

    fun isFemale() = this == FEMALE

    companion object {
        fun fromSex(sex: String) = Sex.entries.first { it.sex == sex }
    }
}

@OptIn(ExperimentalSerializationApi::class)
object SexStringSerializer : KSerializer<Sex> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SexString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Sex) {
        encoder.encodeString(if (value == Sex.FEMALE) Sex.FEMALE.sex else Sex.MALE.sex)
    }

    override fun deserialize(decoder: Decoder): Sex {
        return Sex.fromSex(decoder.decodeString())
    }

}