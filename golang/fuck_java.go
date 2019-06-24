package main

import "fmt"

// 傻逼 Java 程序员会如下，把代码写成Java格式的异常

func main() {
	defer Catch()
	Foo()
}

func Foo() {
	Exception("fuck java")
}

func Exception(msg string) {
	panic(msg)
}

func Catch() {
	r := recover()
	fmt.Println(r)
}
