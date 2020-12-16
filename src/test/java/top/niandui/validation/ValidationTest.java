package top.niandui.validation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.Min;

/**
 * ValidationTest
 * <p>目标类需要@Validated在类型级别用注释进行注释</p>
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/14 11:39
 */
@SpringBootTest
public class ValidationTest {

    @Test
    public void test01() {
        validReturn();
    }

    public @Min(1) int validReturn() {
        return 0;
    }
}
