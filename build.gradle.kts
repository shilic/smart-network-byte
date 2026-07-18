import java.util.Properties
import kotlin.apply

// 1. 从 GRADLE_USER_HOME 读取全局 gradle.properties (存放 git 凭证) !!! 不要把密钥放到仓库里上传到 github
val globalProps = Properties().apply {
    gradle.gradleUserHomeDir.resolve("gradle.properties").takeIf(File::exists)?.reader()?.use(::load)
}
plugins {
    kotlin("jvm") version "2.1.0"
    /* 2. 应用 maven-publish 插件;
    * 将项目发布到 本地maven仓库、远程maven仓库、GitHub Packages 仓库 都需要使用该插件;
    * 启用后，需要先同步一次，再添加 publishing 节点 */
    `maven-publish`
    // 添加签名插件，用于GPG签名
    signing
}
// 3. 组织机构的名称必须是  io.github.你的github名称，除非你有你自己的域名，maven中心会校验你是否拥有这个域名，否则一律挂到github下
group = "io.github.shilic"
// 4. 版本号
version = "1.0.0"

// 定义仓库，构建脚本会从这里拉取依赖
repositories {
    mavenCentral()
}

// 5. 源码包 (可深入源码DEBUG)
tasks.register<Jar>("sourcesJar") {
    description = "源码包 (可深入源码DEBUG)"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
// 6. Javadoc 包 (可查看文档注释)
tasks.register<Jar>("javadocJar") {
    description = "Javadoc 包 (可查看文档注释)"
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().outputs)
}
// 7. 定义发布内容 (在添加 `maven-publish` 之后，需要同步一下 gradle 更改才不会语法报错)
publishing {
    /* 8. 定义一个标准的发布内容
     * 一个项目可以定义多个发布内容 (Multiple Publications)，例如发布不同的构件或为不同的用途提供不同的元数据。
     * 例如: 基本的jar(可调用代码)、 源码(可深入源码DEBUG)、 java-docs(可查看文档)  */
    publications {
        // 定义名为 maven 的发布内容，固定名称 maven
        create<MavenPublication>("maven") {
            // kotlin("jvm") 插件内部会应用 java 插件，所以软件组件名统一叫 "java"，没有 "kotlin" 这个组件。
            // 这不是"不可变"，而是 JVM 类库的标准写法——Java 和 Kotlin 都是同一个 `components["java"]。
            from(components["java"])
            // 源码包 (可深入源码DEBUG)
            artifact(tasks["sourcesJar"])
            // Javadoc 包 (可查看文档注释)
            artifact(tasks["javadocJar"])
            // 可以在这里自定义 POM 内容
            pom {
                name = "smart-network-byte"
                description = "更聪明的网络字节转换器"
                url.set("https://github.com/shilic/smart-network-byte")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("诚")
                        name.set("诚")
                        email.set("985478238@qq.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/shilic/smart-network-byte.git")
                    developerConnection.set("scm:git:ssh://github.com/shilic/smart-network-byte.git")
                    url.set("https://github.com/shilic/smart-network-byte")
                }
            }
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
        // 发布到 MavenCentral
        maven {
            // 仓库名称 (固定参数 MavenCentral, 不可变动)
            name = "MavenCentral"
            // ✅ 新 Central Portal 的 staging 地址，不是老的 s01
            // url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            //url = uri("https://central.sonatype.com/api/v1/publisher/bundle")
            credentials {
                username = globalProps.getProperty("centralPortalUsername") ?: System.getenv("CENTRAL_PORTAL_USERNAME") ?: ""
                password = globalProps.getProperty("centralPortalPassword") ?: System.getenv("CENTRAL_PORTAL_PASSWORD") ?: ""
            }
        }


        /*  // 使用 Gitea 自建的远程仓库
        maven {
            // 使用 Gitea 自建的远程仓库，名称强制指定为 Gitea
            name = "Gitea"
            url = uri("http://你的内网网址:你的端口号/api/packages/你的gitea名/maven")
            // http 链接需要强制使用 isAllowInsecureProtocol = true
            isAllowInsecureProtocol = true
            // 设置仓库凭证
            credentials(HttpHeaderCredentials::class) {
                // Gitea 规定，名称强制为 Authorization
                name = "Authorization"
                // Gitea 的个人访问令牌和 github 类似，到网站上自己去生成一个。
                value = "token ${globalProps.getProperty("gitea.token")}"
            }
            // 以下代码为固定的
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
        */
        /* // MyNexus
        maven {
            name = "MyNexus"
            url = uri("https://nexus.company.com/repository/maven-releases/")
            credentials {
                username = "your_username"
                password = "your_password"
            }
        }
        */
    }
}
// 和 GitHubPackages 多了一步，那就是签名。（需要先在插件里添加 signing 插件 ）
///* 方式 A：本地开发用 GnuPG（~/.gnupg 里有密钥）你愿意在机器上装 gpg、存私钥。*/
//signing {
//    useGpgCmd()  // 告诉 Gradle 我要用本机装的 gpg 程序
//    /*如果你已经在 gradle.properties 里写了
//    signing.gnupg.keyName=xxxxxx
//    signing.gnupg.passphrase=xxxxx
//    * */
//    sign(publishing.publications["maven"])
//}

// /* 方式 B：CI 友好，把私钥整个导出来走内存（推荐） */
signing {
    val signingKey = globalProps.getProperty("signingKey") ?: System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = globalProps.getProperty("signingPassword") ?: System.getenv("GPG_PASSPHRASE")
    // 测试可以正确打印密钥
    //    println("signingKey: $signingKey")
    //    println("signingPassword: $signingPassword")
    if (signingKey != null) {
        useInMemoryPgpKeys(null, signingKey, signingPassword)
    }
    sign(publishing.publications["maven"])
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