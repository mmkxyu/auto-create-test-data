# autotest
## 说明

该工程作用是快速造数据，按照你想要的规则将造好的测试数据存入csv,方便postman，jmeter等工具做接口自动化<br>
我们知道当我们做接口自动化的时候，制作入参的csv表格手动填写很麻烦，特别是当项目某个字段的规则变了以后，那么所有涉及到这个字段的正常和异常用例数据可能都需要改变，维护的工作量比较大
该工具就是帮助你用代码的方式去造csv数据，一旦接口的字段规则变了，只需要变动生成数据的代码规则即可

## 制作完成的表格效果如图

![自动化造csv测试数据](https://github.com/mmkxyu/autotest/blob/master/src/main/resources/img/data.jpg)

## 工程中各类作用

该工程直接运行ExecutionMain类即可
* DataEntity：数据实体类，即测试用例表头的各字段

* CsvWriterExtend：java写csv文件类，是基于CsvWriter工具包的基础上，优化了一些功能：<br>
1：解决CsvWriter写入csv文件，打开后中文乱码问题<br>
2：解决CsvWriter写入csv文件，末尾行总会多出一个/r或/n即空格行问题<br>
3：解决CsvWriter写入csv文件，空字符串""(不处于第一列时)不被写入显示null的问题<br>

* CreateDataModel：创建数据类，即创建符合规则的测试用例数据，并写入csv文件中<br>

* ExecutionMain：执行类，执行该类即可生成一个csv文件<br>

## 备注

此处给出postman通过使用该csv文件进行测试的结果
![postman使用自动化造的csv文件进行接口自动化](https://github.com/mmkxyu/autotest/blob/master/src/main/resources/img/data1.jpg)
