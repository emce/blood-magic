package mobi.cwiklinski.bloodline

import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Resource actual constructor(actual val name: String) {

    actual fun exists() = File("$RESOURCE_PATH/$name").exists()

    actual fun readText() = File("$RESOURCE_PATH/$name").readText()

}