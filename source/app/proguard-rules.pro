# Keep the Xposed entry points and hook callbacks.
-keep class com.golda.patchinstagram.ModuleMain { *; }
-keep class com.golda.patchinstagram.MainHook { *; }
-keep class com.golda.patchinstagram.Hooks$* { *; }
-keepclassmembers class * {
    @io.github.libxposed.api.annotations.* <methods>;
}
