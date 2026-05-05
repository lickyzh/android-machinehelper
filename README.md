# 机械小助手 (Machine Helper)

一款跨平台机械工程计算器应用，支持 iOS 和 Android。

## 功能模块

| 功能 | 说明 |
|------|------|
| 大写转换 | 阿拉伯数字金额转换为中文大写金额，银行日期格式转换 |
| 螺纹计算 | 螺纹中径、大径、小径计算，三针测量尺寸计算 |
| 重量计算 | 支持圆柱、圆管、方块三种形状，钢/球墨铸铁/铜/铝材质 |
| 拉力计算 | 根据拉断力和截面积计算拉断外径 |
| 公差计算 | 支持标准公差(IT)和JS公差计算 |

## 技术栈

### Android
- Kotlin + Jetpack Compose
- Material Design 3
- minSdk: 24, targetSdk: 34

### iOS
- Swift + SwiftUI
- iOS 14+

## 项目结构

```
├── ios/                          # iOS SwiftUI 项目
│   └── Machine Helper/
│       ├── App/                  # 视图文件
│       ├── Model/                # 数据模型
│       ├── Data/                 # 静态数据
│       └── Function/              # 公共函数
│
├── android/                      # Android Compose 项目
│   └── app/src/main/java/com/apat/machinehelper/
│       ├── ui/views/             # 视图组件
│       ├── ui/theme/             # 主题配置
│       ├── model/                # 数据模型
│       ├── data/                 # 静态数据
│       └── function/             # 公共函数
```

## Android 构建

```bash
cd android
gradle wrapper
./gradlew build
```

## iOS 构建

```bash
cd ios
open "Machine Helper.xcodeproj"
# 在 Xcode 中选择模拟器并运行
```

## 版本

- Android: 1.0.406.241
- iOS: 1.0.406.241