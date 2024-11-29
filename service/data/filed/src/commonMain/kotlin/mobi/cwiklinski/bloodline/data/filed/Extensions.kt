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

fun Profile.withAvatar(newAvatar: String) = Profile(
    id,
    name,
    email,
    newAvatar,
    sex,
    notification,
    starting,
    centerId
)

fun Profile.withSex(newSex: Sex) = Profile(
    id,
    name,
    email,
    avatar,
    newSex,
    notification,
    starting,
    centerId
)

fun Profile.withNotification(newNotification: Boolean) = Profile(
    id,
    name,
    email,
    avatar,
    sex,
    newNotification,
    starting,
    centerId
)

fun Profile.withStarting(newStarting: Int) = Profile(
    id,
    name,
    email,
    avatar,
    sex,
    notification,
    newStarting,
    centerId
)

fun Profile.withCenter(newCenter: String) = Profile(
    id,
    name,
    email,
    avatar,
    sex,
    notification,
    starting,
    newCenter
)

fun Profile.withData(
    newName: String,
    newEmail: String,
    newAvatar: String,
    newSex: Sex,
    newNotification: Boolean,
    newStarting: Int,
    newCenterId: String
) = Profile(
    id,
    newName,
    newEmail,
    newAvatar,
    newSex,
    newNotification,
    newStarting,
    newCenterId
)