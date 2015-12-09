# MockServer
更好的数据模拟，通过注解编程的方式将值写入进model，对业务代码没有侵入性。

## 实体类都需要继承`IMockModel`接口
```java
public class SimpleData implements IMockModel
```
## 支持类型
Int、String、Double、以及分别对应的数组

## 调用入口
```java
AnnotationAnalyzer<IMockModel> dataAnnotationProcessor = new AnnotationAnalyzer<>();
dataAnnotationProcessor.fieldAnnotationValue(MODEL);
```
