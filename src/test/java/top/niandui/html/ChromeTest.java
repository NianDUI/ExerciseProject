//package top.niandui.html;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Before;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriverService;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//
//import java.io.IOException;
//
///**
// * 应用启动自动加载
// *
// * @author liyongda
// * @version 1.0
// * @date 2020/11/6 23:22
// */
//public class ChromeTest {
//    private static ChromeDriverService service;
//    private WebDriver driver;
//
//    @BeforeClass
//
//
//    public static void createAndStartService() throws IOException {
//        service = new ChromeDriverService.Builder()
//                .usingDriverExecutable(new File("path/to/my/chromedriver.exe"))
//                .usingAnyFreePort().build();
//        service.start();
//    }
//
//    @AfterClass
//    public static void createAndStopService() {
//        service.stop();
//    }
//
//    @Before
//    public void createDriver() {
//        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
//    }
//
//    @After
//    public void quitDriver() {
//        driver.quit();
//    }
//
//    @Test
//
//    public void testGoogleSearch() {
//        driver.get("http://www.google.com");
//        WebElement searchBox = driver.findElement(By.name("q"));
//        searchBox.sendKeys("webdriver");
//        searchBox.quit();
//    }
//}
