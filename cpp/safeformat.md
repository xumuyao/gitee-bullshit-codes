# C 风格格式化类型不匹配

在 C,C++ 语言中，`printf` 是被使用很广泛的函数，但也是一些 BUG 的来源，通常如果使用默认的 `printf` `snprintf` `fprintf`，如果格式字符串中期望得到的数据类型与参数类型不一致，编译器通常会报告警告，但如果像下面一样自己封装这样的函数，稍不注意，则容易出现类型不匹配而导致 BUG。

```c++
int my_vsnprintf(char *buf,size_t buflen,const char *fmt,va_list ap);
int my_snprintf(char *buf,size_t buflen, const char *fmt,...);
```

此问题的根源在于在使用 `va_arg` 展开参数时，只能根据占位符获得参数类型，参数类型如果不匹配，则可能溢出，或者预期得到字符串，但参数类型却不是 C-Style 字符串，则可能会导致程序崩溃。

如果要封装这样的变参函数，应该使用编译器 `attribute` 特性描述函数，或者使用 C++ 变参模板取代 C 风格的变参函数。

在 git-srv 中， `wink::StrFormat` 便使用变参模板实现了类型安全的格式化。
