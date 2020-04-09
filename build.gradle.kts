plugins {
    java
    kotlin("jvm") version "1.3.71"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.exposed:exposed-core:0.23.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.23.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.23.1")
    implementation("org.slf4j:slf4j-log4j12:2.0.0-alpha1")
    implementation("com.h2database:h2:1.4.200")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}