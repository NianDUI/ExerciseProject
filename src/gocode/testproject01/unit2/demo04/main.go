package main
import (
	"fmt"
	"unsafe"
)

func main() {
	var num1 float32 = 3.14 // 3.14
	fmt.Println(num1)
	fmt.Printf("%T\n", num1) // float32
	fmt.Println(unsafe.Sizeof(num1)) // 4
	var num2 float32 = -3.14 // -3.14
	fmt.Println(num2)
	var num3 float32 = 314E-2 // 3.14
	fmt.Println(num3)
	var num4 float32 = 314E+2 // 31400
	fmt.Println(num4)
	var num5 float32 = 314e+2 // 31400
	fmt.Println(num5)

	var num6 float64 = 314e+2 // 31400
	fmt.Println(num6)

	var num7 float32 = 256.00000906 // 256 // 精度丢失，建议使用 float64
	fmt.Println(num7)
	var num8 float64 = 256.00000906 // 256.00000906
	fmt.Println(num8)

	var num9 = 3.17
	fmt.Printf("num9默认类型为：%T\n", num9) // 默认为 float64
}