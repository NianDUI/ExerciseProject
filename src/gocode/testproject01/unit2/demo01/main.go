package main
import "fmt"
func main() {
	// 变量的声明和赋值
	var age int 
	age = 18
	fmt.Println("age =", age)

	var age2 int = 19
	fmt.Println("age2 =", age2)

	// 报错，浮点赋值为到int
	var num int = 12.56
	fmt.Println("num =", num)
}