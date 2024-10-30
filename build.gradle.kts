plugins {
    id("java")
}

group = "com.test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val polyglotVersion = "24.1.1"

dependencies {

    implementation("org.graalvm.polyglot:polyglot:$polyglotVersion")
    implementation("org.graalvm.polyglot:js:$polyglotVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}