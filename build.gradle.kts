import java.util.Properties
import kotlin.apply

/* 1. 从 GRADLE_USER_HOME 读取全局 gradle.properties (存放 git 凭证) !!! 不要把密钥放到仓库里上传到 github */
val globalProps = Properties().apply {
    gradle.gradleUserHomeDir.resolve("gradle.properties").takeIf(File::exists)?.reader()?.use(::load)
}
plugins {
    kotlin("jvm") version "2.2.0"
    // 对应 publishing 节点
    `maven-publish`
    // 使用社区插件发布软件包，必须 JVM 17 以上(在项目结构中修改)，Kotlin 2.2.0以上；gradle 也有最新版，我TM服了。
    id("com.vanniktech.maven.publish") version "0.36.0"
}
// 3. 组织机构的名称必须是  io.github.你的github名称，除非你有你自己的域名，maven中心会校验你是否拥有这个域名，否则一律挂到github下
group = "io.github.shilic"
// 4. 版本号  !!! 严禁 -SNAPSHOT
version = "1.0.0"

// 定义仓库，构建脚本会从这里拉取依赖
repositories {
    mavenCentral()
}
// maven中央仓库规定，必须携带源码包和文档包
java {
    withSourcesJar()
    withJavadocJar()
}
/** 我的POM */
val myPom: MavenPom.() -> Unit = {
    name = "smart-network-byte"
    description = "更聪明的网络字节转换器"
    url = "https://github.com/shilic/smart-network-byte"
    licenses {
        license {
            name = "The Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
        }
    }
    developers {
        developer {
            id = "诚"
            name = "诚"
            email = "985478238@qq.com"
        }
    }
    scm {
        url = "https://github.com/shilic/smart-network-byte"
        connection = "scm:git:git://github.com/shilic/smart-network-byte.git"
        developerConnection = "scm:git:ssh://github.com/shilic/smart-network-byte.git"
    }
}
/* 7. 定义发布内容 (在添加 `maven-publish` 之后，需要同步一下 gradle 更改才不会语法报错) */
publishing {
    /* 8. 定义一个标准的发布内容
     * 一个项目可以定义多个发布内容 (Multiple Publications)，例如发布不同的构件或为不同的用途提供不同的元数据。
     * 例如: 基本的jar(可调用代码)、 源码(可深入源码DEBUG)、 java-docs(可查看文档)  */
    publications {
        // 定义名为 maven 的发布内容; 名称和 sign(publishing.publications["maven"]) 一致
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            pom(myPom)
        }
    }
    // 9. 定义将要发布的远程仓库（发布到哪里？）
    repositories {
        // 发布到 GitHubPackages
        maven {
            // 仓库名称 (固定参数 GitHubPackages, 不可变动 ; 该存储库指向 GitHub Packages)
            name = "GitHubPackages"
            // 仓库 github URL
            url = uri("https://maven.pkg.github.com/shilic/smart-network-byte")
            // 设置仓库凭证
            credentials {
                // 使用推荐的写法，从 GRADLE_USER_HOME 读取全局 gradle.properties (存放 git 凭证)
                username = globalProps.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR") ?: ""
                password = globalProps.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
}
// <module directory>/build.gradle.kts
/* 使用 mavenPublishing 节点发布软件包，需要先使用  id("com.vanniktech.maven.publish") 插件 */
mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(
        groupId = group.toString(),
        artifactId = "smart-network-byte",
        version = version.toString()
    )
    pom(myPom)
}
// 项目依赖
dependencies {
    testImplementation(kotlin("test"))
}
// 测试任务，脚本构建的时候，会自动跑一遍所有的测试项；保证单元测试不出bug
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}