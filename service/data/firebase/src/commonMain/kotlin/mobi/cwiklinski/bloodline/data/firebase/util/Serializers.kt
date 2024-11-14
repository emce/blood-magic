@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package mobi.cwiklinski.bloodline.data.firebase.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Boolean::class)
object BooleanIntSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BooleanInt", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(if (value) "1" else "0")
    }

    override fun deserialize(decoder: Decoder): Boolean {
        return decoder.decodeString() == "1"
    }
}


object IntToBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("disqualification", PrimitiveKind.BOOLEAN)

    override fun deserialize(decoder: Decoder) = try {
        decoder.decodeInt() == 1
    } catch (e: Exception) {
        try {
            decoder.decodeString() == "true" || decoder.decodeString() == "1"
        } catch (e: Exception) {
            decoder.decodeBoolean()
        }
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeBoolean(value)
    }

}