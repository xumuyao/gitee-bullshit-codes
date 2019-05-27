package main

import "fmt"

var s = 0
func main() {
	for i := 0; i < 5; i++ {
		//正经人不会吧defer写在循环中
		defer printS()
		s = i
	}
}

func printS() {
	fmt.Println(s)
}

