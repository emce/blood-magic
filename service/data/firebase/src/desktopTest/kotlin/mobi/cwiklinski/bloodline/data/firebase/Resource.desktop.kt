@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package mobi.cwiklinski.bloodline.data.firebase

import java.io.File

actual class Resource actual constructor(actual val name: String) {
    private val file = File("$RESOURCE_PATH/$name")

    actual fun exists(): Boolean = file.exists()

    actual fun readText(): String = file.readText()
}