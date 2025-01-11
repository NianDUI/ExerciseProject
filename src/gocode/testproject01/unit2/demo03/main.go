package main

// import "fmt"
// import "unsafe"
import (
	"fmt"
	"unsafe"
)

func main() {
	var num1 int8 = 23
	fmt.Println(num1)

	var num2 uint8 = 230
	fmt.Println(num2)

	var num3 = 28
	// 打印变量类型
	fmt.Printf("num3的类型为：%T", num3) // num3的类型为：int，默认为 int（根据系统来 int32 还是 int64）
	fmt.Println()
	// 变量占用的字节数
	fmt.Println(unsafe.Sizeof(num3)) // 8


	// 表示学生年龄
	var age byte = 18 // 等价 var age uint8 = 18
	fmt.Println(age)


	fmt.Println("\n--------------")
	var num32 rune = 10 // == int32
	fmt.Println(num32)
}
