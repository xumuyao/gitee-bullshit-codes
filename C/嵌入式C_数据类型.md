# 嵌入式编程中的数据类型

举个例子：
```
void delay(uint8_t i)
{
	uint8_t j  = 100;
    while(i -- )
    {
        while(j -- );
    }
}
 
uint8_t sum_func(uint8_t a,uint8_t b)
{
    return a + b;
}


void main(void)
{
	
    ...
    delay(256);
    sum = sum_func(200,200);
    ...
}

```

**碰到还好，就怕运行的时候所有参数都是在范围之内，其实很大程度上代码已经飞起来了，而且你还不知道。活在梦里**
