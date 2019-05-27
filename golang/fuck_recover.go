package main

import "fmt"

var m = 0

func main() {
	//defer 方法中的参数方法会被执行时候县渲染。 这个时候返回的是0 而不是1
	defer doSome(getValue())
	m = 1
	fmt.Println("end")
}

func doSome(value int) {
	fmt.Println(value)
}

func getValue() int {
	return m 
}
