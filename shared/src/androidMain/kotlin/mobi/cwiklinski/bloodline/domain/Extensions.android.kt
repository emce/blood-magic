package mobi.cwiklinski.bloodline.domain

import android.os.Parcel
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.data.Parceler

actual object LocalDateParceler : Parceler<LocalDate> {

    override fun create(parcel: Parcel): LocalDate {
        return parcel.readString()?.let {
            LocalDate.parse(it)
        } ?: LocalDate(0, 0, 0)
    }

    override fun LocalDate.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}