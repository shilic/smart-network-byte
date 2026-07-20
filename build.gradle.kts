import java.util.Properties
import kotlin.apply

plugins {
    kotlin("jvm") version "2.2.0"
    /* 对应 publishing 节点; 使用传统方式发布软件包 */
    `maven-publish`
    // 添加原生签名插件，用于GPG签名
    signing
    /* 使用社区插件 com.vanniktech.maven.publish 发布软件包:
    * JVM : 必须 17 以上 (在项目结构中修改SDK级别, 不是修改语言级别)，
    * Kotlin : 2.2.0以上；
    * gradle : com.vanniktech.maven.publish 插件 0.36.0 调用了 ProjectLayout.getSettingsDirectory() 方法，该方法在 Gradle 8.12 才引入;
    * 我TM服了。*/
    id("com.vanniktech.maven.publish") version "0.36.0"
}
/* ======================= 填写个人信息 ============================= */
/** 从 settings.gradle.kts 文件取值过来 */
val artifactId: String = rootProject.name
/* 组织机构的名称必须是 io.github.<你的github名称>，除非你有你自己的域名; maven中心会校验你是否拥有这个域名，否则一律挂到 github 下 */
group = "io.github.shilic"
/* 版本号  !!! 严禁 -SNAPSHOT */
version = "1.0.0"
/** 提取个人的链接，方便统一修改 */
val myGit: String = "github.com/shilic/$artifactId"
/** 复用我的POM */
val myPom: MavenPom.() -> Unit = {
    name = artifactId
    description = "更聪明的网络字节转换器"
    url = "https://$myGit"
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
        url = "https://$myGit"
        connection = "scm:git:git://$myGit.git"
        developerConnection = "scm:git:ssh://$myGit.git"
    }
}
// 定义仓库，构建脚本会从这里拉取依赖
repositories {
    mavenCentral()
}
/* 使用 mavenPublishing 发布到 Maven Central，签名、源码包、文档包均由插件自动处理 */
mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), artifactId, version.toString())
    pom(myPom)
}
/* 追加 GitHubPackages 发布目标; com.vanniktech.maven.publish 插件 已经打包了发布内容，所以这里只需要追加远程仓库。 */
afterEvaluate {
    /* 从 GRADLE_USER_HOME 读取全局 gradle.properties (存放 git 凭证) !!! 不要把密钥放到仓库里上传到 github */
    val globalProps: Properties = Properties().apply {
        gradle.gradleUserHomeDir.resolve("gradle.properties")
            .takeIf(File::exists)?.reader()?.use(::load)
    }
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/shilic/${artifactId}")
                credentials {
                    username = globalProps.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR") ?: ""
                    password = globalProps.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN") ?: ""
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
                 authentication {create("header", HttpHeaderAuthentication::class)}
             }
             */
        }
    }
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