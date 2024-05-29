class JSPlatform: Platform {
    override val name: String = "Compose for Web kotlin/JS"
}

actual fun getPlatform(): Platform = JSPlatform()