
# SelectorInjection
[![](https://jitpack.io/v/tianzhijiexian/SelectorInjection.svg)](https://jitpack.io/#tianzhijiexian/SelectorInjection)
![](https://img.shields.io/badge/minSdk-16-blue.svg)
[![](https://img.shields.io/github/stars/tianzhijiexian/SelectorInjection.svg)](https://github.com/tianzhijiexian/SelectorInjection)
[![](https://img.shields.io/github/forks/tianzhijiexian/SelectorInjection.svg)](https://github.com/tianzhijiexian/SelectorInjection/network)

本项目提供了 Android 中按钮的 SVG (vectorDrawable) 支持，可以支持在 Android 5.0 以下使用 SVG 图片和 Tint 着色方案。

---

### 依赖方式

1.添加JitPack仓库

```
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

2.添加依赖

>
implementation 'com.github.tianzhijiexian:SelectorInjection:Latest [Latest release](https://github.com/tianzhijiexian/SelectorInjection/releases) (*click it*)'

### 属性介绍

原则上是将原本`android:`前缀变成`app:`就可以支持 SVG (vectorDrawable) 和 Tint 了，下面是自定义控件和其支持属性的介绍：

**SelectorTextView：**

```
app:drawableLeft="@drawable/icon_facebook_svg"  
app:drawableLeftTint="@color/red"
```
属性格式：

- drawable(Top/Left/Right/Bottom)
- drawable(Top/Left/Right/Bottom)Tint

**SelectorImageButton：**

```
app:src="@drawable/icon_facebook_svg"
android:tint="@color/orange" 
```

**SelectorRadioButton：**

```
app:button="@drawable/icon_renren_svg" 
app:buttonTint="@color/red"
```

注：对于单选按钮的SVG和Tint支持

### 示例

View| Explain | Attribute
--- | --- | ---
SelectorTextView   | <div><img src="http://static.zybuluo.com/shark0017/o06fq4tk0dk35kznk965vf96/image_1cca7nhgh1khc1e6o1v8fjtqosb3h.png" width="300"></div>| app:drawableBottom="@drawable/icon_facebook_svg"<br>app:drawableBottomTint="@color/green"
SelectorImageButton | <div><img src="http://static.zybuluo.com/shark0017/idhyn2508hqtl7kulwqykod8/image_1cca8953dav3b2n1ork1hjnlh24b.png" width="300"></div>| app:src="@drawable/icon_robot_svg" <br>  android:tint="@color/orange" 
SelectorRadioButton | <div><img src="http://static.zybuluo.com/shark0017/c460uqsb4vd4dzoogmsk9lnp/image_1cca780lu12dcp0a1nhc16jcpfl1t.png" width="300"></div>| app:button="@drawable/icon_check_selector"  <br>  app:buttonTint="@color/orange"	

### 支持自动提示

![](http://static.zybuluo.com/shark0017/7zwuwmpqt8277pij1y0n9ujx/image_1cca9etom4krs9a89a1mpd1in355.png)

### 开发者 

![](https://avatars3.githubusercontent.com/u/9552155?v=3&s=460)

Jack Tony: <developer_kale@foxmail.com>  

### License

    Copyright 2016-2019 Jack Tony

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

