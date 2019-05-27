package main

import (
	"encoding/json"
	"fmt"
)

func main() {

	newMap := make(map[string]interface{})

	jsonStr := `{
		"name":"ansj",
		"age":12
	}`

	//golang json 序列化只支持指针对象
	_ = json.Unmarshal([]byte(jsonStr), newMap)

	fmt.Println(newMap)
}