## unsigned integers in c/cpp
先看一段代码
```c++
/// 通常情况下以下遍历容器代码不会有什么问题
for (int i = 0; i < container.size(); i++) 
{
/// 但是，编译器会提示提示警告：原因是size()返回的类型是size_t
/// 而size_t 定义为 unsigned integers 的别名
}

/// 因此，一般的做法是这样
/// 将 i 定义为 size_t 类型
for (size_t i = 0; i < container.size(); i++) 
{
/// 至此没有什么问题
}

/// 但是，当反向遍历容器的时候
/// 代码可能是这样
for (size_t i = container.size()-1; i >= 0; i--) 
{
/// 而这样，代码就出问题了——死循环
/// 原因是 integers overflow，这里是 整数下溢
/// 而，unsigned integers在ISO标准中保证不会下溢(通过modulo wrapping)
/// 所以，编译器会保证 i >= 0 永远成立
/// modulo wrapping可以简单理解为 i 到0之后，再重新更新为 intergers 表示的最大值(UINT_MAX/ULONG_MAX)
}

/// 所以，反向遍历容器时
/// 应该这样
for (int i = container.size()-1; i >= 0; i--) {}
/// 或者这样
for (auto it = container.end() -1; it != container.begin(); it--) {}
/// 亦或者这样
for (auto it = container.rbegin(); it != container.rend(); it++) {}
```