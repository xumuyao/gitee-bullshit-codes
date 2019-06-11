package main

import "fmt"

type Slice []int

func NewSlice() Slice {
	return make(Slice, 0)
}

func (s *Slice) Add(elem int) *Slice {
	*s = append(*s, elem)
	fmt.Print(elem)
	return s
}

func main() {
	s := NewSlice()
	defer s.Add(1).Add(2) // 这里先执行前面的Add,最后一个不执行，函数结束后再执行最后一个Add 执行结果 132
	// defer s.Add(1).Add(2).Add(4) // 这里执行的结果是 1234
	s.Add(3)
}
