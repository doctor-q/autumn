## 路由
```
cc.doctor.framework.web.route.RequestMapping注解
```
## 参数提取
``` 
cc.doctor.framework.web.handler.parser.Param
获取servlet的参数绑定，可使用别名
cc.doctor.framework.web.handler.parser.Attribute
获取servlet的Attribute绑定，可使用别名
cc.doctor.framework.web.handler.parser.Cookie
获取http的cookie绑定，可使用别名
cc.doctor.framework.web.handler.parser.Header
获取http的header绑定，可使用别名
cc.doctor.framework.web.handler.parser.Unpack
包装参数，可以使用前后置钩子
cc.doctor.framework.web.handler.parser.Form
包装参数
cc.doctor.framework.web.handler.parser.JsonParam
通过json反序列化绑定
```
## 输入参数转化
``` 
cc.doctor.framework.web.handler.in.DataMapper
数据映射
cc.doctor.framework.web.handler.in.DateParse
字符串转时间
cc.doctor.framework.web.handler.in.EnumDataMapper
枚举映射
cc.doctor.framework.web.handler.in.File
文件映射，没写
cc.doctor.framework.web.handler.in.ListSplit
字符串分割转列表
```
## 输出参数转化
```
cc.doctor.framework.web.handler.out.DateFormat
时间格式化输出
cc.doctor.framework.web.handler.out.ListJoin
列表转字符串
```
## 视图解析器
``` 
cc.doctor.framework.web.handler.resolver.json.JsonResolver
输出json
cc.doctor.framework.web.handler.resolver.modelview.ExcelViewResolver
输出excel
cc.doctor.framework.web.handler.resolver.modelview.FreemarkerViewResolver
freemarker渲染
cc.doctor.framework.web.handler.resolver.modelview.VelocityViewResolver
velocity渲染
```
## aop
``` 
cc.doctor.framework.web.aop.AopHandler
根据注解实现方法前置和后置aop
```

