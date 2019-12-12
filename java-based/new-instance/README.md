## 创建对象
> [csdn博客：创建对象的5种方式](https://blog.csdn.net/m0_38016268/article/details/82957536Java "创建对象的5种方式")

> [菜鸟教程：Java教程-序列化](https://www.runoob.com/java/java-serialization.html "Java教程-序列化")


- new对象
- 使用反射类中Class.newInstance()创建对象
- 使用反射类中Class类中的Constructor的newInstance()方法创建对象，如:Object.class.getConstructor.newInstance()
- 使用对象的克隆方法object.clone()方法，前提object类必须实现Cloneable类，让object类具有clone功能
- Java序列化，java提供一种将对象序列化机制，实质就是将一个对象转换成字节序列，字节序列依然保存该对象的数据，及对象类型信息和对象属性类型等内容。同时提供反序列化机制。我们通过反序列化的ObjectInputStream中的readObject方法创建对象。前提创建对象需要实现Serializable[ˈsɪˌriəˌlaɪzəbl]接口。





