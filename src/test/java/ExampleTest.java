import com.example.LoginApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) // 指定测试运行器 [[1]][[3]]
@SpringBootTest(classes = LoginApplication.class)
public class ExampleTest {

    @Test
    public void testPrintMessage() {
        System.out.println("执行测试方法，输出内容示例"); // 直接打印内容
        // 可添加其他逻辑，例如调用服务层方法并验证结果
    }
}