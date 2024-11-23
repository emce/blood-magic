package mobi.cwiklinski.bloodline.data.filed

import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

fun Profile.withEmail(newEmail: String) = Profile(
    id,
    name,
    newEmail,
    avatar,
    sex,
    notification,
    starting,
    centerId
)

fun Profile.withName(newName: String) = Profile(
    id,
    newName,
    email,
    avatar,
    sex,
    notification,
    starting,
    centerId
)

fun Profile.withData(
    newName: String,
    newAvatar: String,
    newSex: Sex,
    newNotification: Boolean,
    newStarting: Int,
    newCenterId: String
) = Profile(
    id,
    newName,
    email,
    newAvatar,
    newSex,
    newNotification,
    newStarting,
    newCenterId
)