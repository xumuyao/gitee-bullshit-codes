package main

import "fmt"

//参考 https://books.studygolang.com/advanced-go-programming-book/appendix/appendix-a-trap.html
func main() {
	fmt.Println(errMehthod())
}

func errMehthod() (err error) {
	if err := Bar(); err != nil {
		//在局部作用域中，命名的返回值内同名的局部变量屏蔽：
		//这里运行时会报错。编译不报错
		return
	}
	return
}

func Bar() error {
	return fmt.Errorf("fuck hongshu")
}
