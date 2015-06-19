# SelectorInjection    
一个强大的selector注入器。它可以给view注入selector状态，免去了你写selector的步骤。  
如果你的需求很简单，不妨试试这篇文章讲到的一张图片实现selector的方法：[http://www.cnblogs.com/tianzhijiexian/p/4505190.html](http://www.cnblogs.com/tianzhijiexian/p/4505190.html)
  
![image](./demoPic/logo.png)  
 
--  

### 前言   
之前特别讨厌写selector，所以最近想要干掉它，借鉴了[https://github.com/hanks-zyh/SelectorButton](https://github.com/hanks-zyh/SelectorButton)和[https://github.com/navasmdc/MaterialDesignLibrary](https://github.com/navasmdc/MaterialDesignLibrary)两个开源库的写法，最终融汇贯通，产生了能将颜色和形状两两组合的selector注入器。  
现在这个注入器支持且**不仅仅**支持下列的按钮，你可以通过layer-list和shape的组合产生更多的按钮。  
![image](./demoPic/view.png) 
 
## 添加依赖  
  
1.添加JitPack仓库
  
```  
repositories {
	maven {
		url "https://jitpack.io"
	}
}
```   

2.添加依赖  
```  
dependencies {
		compile 'com.github.tianzhijiexian:SelectorInjection:1.0.1'
}    
```  

### 使用方式 
我们可以根据需要将其注入到任何一个view中去，本项目中将其注入到了ImageButton和TextView中，你可以参考这二者的实现来做自己的自定义view。

**PS**:SelectorImageButton、SelectorTextView已经在lib库中写好，可以直接引用。  

支持的attribute   
```xml  
<!-- 普通状态的颜色 -->
    <attr name="normal_color" format="color" />
    <!-- 按下后的颜色 -->
    <attr name="pressed_color" format="color" />
    <!-- 选中后的颜色 -->
    <attr name="checked_color" format="color" />
    
    <!-- 常规状态下-->
    <attr name="normal_drawable" format="reference" />
    <!-- 按下/获得焦点 -->
    <attr name="pressed_drawable" format="reference" />
    <!-- 选中时 -->
    <attr name="checked_drawable" format="reference" />

    <!-- 边界描边的颜色 -->
    <attr name="stroke_color" format="color" />
    <!-- 外界描边的宽度 -->
    <attr name="stroke_width" format="dimension" />
    <!-- 按下后的描边 -->
    <attr name="pressed_stroke_color" format="color" />
    <attr name="pressed_stroke_width" format="dimension" />
    <!-- 选中后的描边 -->
    <attr name="checked_stroke_color" format="color" />
    <attr name="checked_stroke_width" format="dimension" />
    
    <!-- 是否是自动计算的 -->
    <attr name="isSmart" format="boolean" />

    <!-- 是否将图片设置到src中 -->
    <attr name="isSrc" format="boolean" />
```  

在xml中使用
```xml
	<kale.ui.view.SelectorImageButton
        app:normal_drawable="@drawable/kale"
        app:pressed_drawable="@drawable/pressed_drawable"
        />  
```   

### 还有更多  
你以为它仅仅是干掉了selector么？当然不是，它还对shape有着巨牛逼的支持。我们定义一个shape，然后传入一个normalColor，它会自动将normalColor填充到shape中并且自动产生按下后的点击效果。而shape中我们可以做的事情就多了，比如添加个边框、加个虚线什么的。但shape仅仅能画的是简单且扁平化的形状，能不能让它支持阴影什么的呢？当然可以，SelectorInjection还支持了layer-list。你可以将阴影的框架和shape进行叠加，这样就能产生很多其他的效果了。至于具体的做法嘛，看看demo就明白了。  


### 开发者
![](https://avatars3.githubusercontent.com/u/9552155?v=3&s=460)

Jack Tony: <developer_kale@.com>  


### License

    Copyright 2015 Jack Tony

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 
