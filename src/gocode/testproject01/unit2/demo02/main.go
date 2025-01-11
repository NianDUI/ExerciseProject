package main

import "fmt"

// 全局变量
var n7 = 100
var n8 = 9.7

// 一次行声明
var (
	n9  = 500
	n10 = "netty"
)

func main() {
	// 局部变量
	var num int = 18
	fmt.Println(num)

	var num2 int
	fmt.Println(num2) // 0，默认值

	var num3 = "10.23"
	fmt.Println(num3)

	sex := "男"
	fmt.Println(sex)

	fmt.Println("---------------")
	var n1, n2, n3 int
	fmt.Println(n1, n2, n3)

	var n4, name, n5 = 10, "jack", 7.8
	fmt.Println(n4, name, n5)

	n6, height := 6.9, 100.6
	fmt.Println(n6, height)

	fmt.Println(n7, n8)
	fmt.Println(n9, n10)

	fmt.Println(13, 015, 0xD, 0b1101)
}
