package main

import (
	"fmt"
	"time"
)

func main() {
	for i := 0; i <= 10; i++ {
		go func() {
			fmt.Println(i) //实际闭包中i是地址 匿名函数和中和外边共享的是同一个变量
		}()

		/*正确方法
		//方法一
		finalI := i //复制一个变量副本 (java写法)
		go func() {
			fmt.Println(finalI)
		}()

		//方法二
		go func(paramI int) { //作为参数传递到匿名函数中(实际也是等同于使用副本)
			fmt.Println(paramI)
		}(i)

		*/

	}

	time.Sleep(5 * time.Second)
}
