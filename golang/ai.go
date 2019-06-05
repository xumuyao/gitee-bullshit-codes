package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

// AI 核心代码, 估值3个亿
func main() {
	scanner := bufio.NewScanner(os.Stdin)

	for scanner.Scan() {
		input := scanner.Text()
		input = strings.ReplaceAll(input, "吗", "")
		input = strings.ReplaceAll(input, "?", "!")
		input = strings.ReplaceAll(input, "？", "!")
		fmt.Println(input)
		fmt.Println()
	}
}
