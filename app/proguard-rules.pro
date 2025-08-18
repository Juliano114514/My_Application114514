# 保留基本配置
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# 保留应用入口点
-keep class com.example.my_application114514.MainActivity { *; }
-keep class com.example.my_application114514.PlayActivity { *; }
-keep class com.example.my_application114514.service.MusicService { *; }

# 保留数据类
-keep class com.example.my_application114514.data.** { *; }
-keep class com.example.my_application114514.adapter.** { *; }

# Glide图片加载库
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# 保留Compose相关类
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** {
    *;
}

# 移除日志输出（生产环境）
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# 优化字符串
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# 移除未使用的代码
-dontwarn **