[TOC]

### 使用

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.alanplus:httplib:1.0'
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation 'com.squareup.okio:okio:1.14.0'
}
```