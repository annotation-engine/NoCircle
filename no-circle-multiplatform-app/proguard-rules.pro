-keep class com.nocircle.app.MainKt {
    public static void main(java.lang.String[]);
}

# 1. Kotlin 标准库保留
-keep class kotlin.coroutines.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlin.reflect.** { *; }

# 1. 保留 Compose 核心
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# =============================
# 基础配置
# =============================

# 保留 Kotlin 相关
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# 保留 Kotlin 的注解、元数据和调试信息
-keepattributes *Annotation*
-keepattributes SourceFile, LineNumberTable, EnclosingMethod, InnerClasses, Signature, Exceptions

-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# 保留所有构造函数（供反射使用，例如 Koin）
-keepclassmembers class * {
    public <init>(...);
}

# 保留 Enum 的标准方法（Kotlin 和 Compose 中常见）
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留可能被反射调用的 lambda 表达式
-keepclassmembers class ** {
    *** lambda$(...);
}


# =============================
# Jetpack Compose（含 Desktop）
# =============================

# Jetpack Compose 相关
#-keep class androidx.compose.** { *; }
#-dontwarn androidx.compose.**

# JetBrains Compose for Desktop
-keep class org.jetbrains.compose.** { *; }
-dontwarn org.jetbrains.compose.**

# 保留 Material Design 组件
-keep class androidx.** { *; }


# =============================
# Ktor 网络框架
# =============================

-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# 保留 ktor 相关成员
-keepclassmembers class io.ktor.** {
    *;
}


# =============================
# Koin 依赖注入
# =============================

-keep class org.koin.** { *; }
-dontwarn org.koin.**


# =============================
# Coil 3（用于 Compose Multiplatform 的图片加载）
# =============================

-keep class coil3.** { *; }
-dontwarn coil3.**


############################################################

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$* Companion;
}

# Keep names for named companion object from obfuscation
# Names of a class and of a field are important in lookup of named companion in runtime
-keepnames @kotlinx.serialization.internal.NamedCompanion class *
-if @kotlinx.serialization.internal.NamedCompanion class *
-keepclassmembernames class * {
    static <1> *;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Don't print notes about potential mistakes or omissions in the configuration for kotlinx-serialization classes
# See also https://github.com/Kotlin/kotlinx.serialization/issues/1900
-dontnote kotlinx.serialization.**

# Serialization core uses `java.lang.ClassValue` for caching inside these specified classes.
# If there is no `java.lang.ClassValue` (for example, in Android), then R8/ProGuard will print a warning.
# However, since in this case they will not be used, we can disable these warnings
-dontwarn kotlinx.serialization.internal.ClassValueReferences

# disable optimisation for descriptor field because in some versions of ProGuard, optimization generates incorrect bytecode that causes a verification error
# see https://github.com/Kotlin/kotlinx.serialization/issues/2719
-keepclassmembers public class **$$serializer {
    private ** descriptor;
}

############################################################

# 保留 Kotlin Metadata（必须）
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# =============================
# 优化配置（按需可启用）
# =============================

-optimizationpasses 1
# -allowaccessmodification
# -mergeinterfacesaggressively

# =============================
# 日志与提示（开发期间建议保留输出）
# =============================

# 构建时禁用所有注释和警告输出（正式打包时可开启）
-dontnote
-dontwarn
-ignorewarnings

-keep class * implements com.nocircle.common.navigation.NoRoute { *; }