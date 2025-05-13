# ================================================================================
# 程序主入口
# ================================================================================

-keep class com.nocircle.app.MainKt {
    public static void main(java.lang.String[]);
}
-keep class * implements com.nocircle.common.navigation.NoRoute { *; }
-keep class * implements com.nocircle.common.config.ConfigKey { *; }
-keep class com.nocircle.common.room.** { *; }
-keep class com.nocircle.common.navigation.** { *; }

# ================================================================================
# Kotlin 配置
# ================================================================================

-keep class kotlin.coroutines.** { *; }
-keep class kotlin.reflect.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }

-keepclassmembers class * {
    public <init>(...);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class ** {
    *** lambda$(...);
}

# ================================================================================
# Compose Multiplatform 配置
# ================================================================================

-keep class androidx.** { *; }
-keep class org.jetbrains.compose.** { *; }

# ================================================================================
# Kotlin Metadata 配置
# ================================================================================

-keepattributes *Annotation*
-keepattributes EnclosingMethod, InnerClasses, Signature
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# ================================================================================
# Ktor 配置
# ================================================================================

-keep class io.ktor.client.** { *; }
-keep class io.ktor.network.** { *; }
-keep class io.ktor.websocket.** { *; }
-keep class io.ktor.serialization.** { *; }
-keep class io.ktor.utils.io.** { *; }

# ================================================================================
# Koin 配置
# ================================================================================

-keep class org.koin.** { *; }

# ================================================================================
# Coil3 配置
# ================================================================================

-keep class coil3.** { *; }
-keep class okio.** { *; }

# ================================================================================
# Kotlin Serialization 配置
# ================================================================================

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$* Companion;
}

-keepnames @kotlinx.serialization.internal.NamedCompanion class *

-if @kotlinx.serialization.internal.NamedCompanion class *
-keepclassmembernames class * {
    static <1> *;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
    public static <1>$$serializer INSTANCE;
	public static kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers public class **$$serializer {
    private ** descriptor;
}
-keep @kotlinx.serialization.Serializable class ** { *; }

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1>$Companion {
    public kotlinx.serialization.KSerializer serializer(...);
}

# ================================================================================
# 更多参数配置
# ================================================================================

-optimizationpasses 2
-dontnote **
-dontwarn **
-ignorewarnings