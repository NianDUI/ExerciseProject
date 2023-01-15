package top.niandui.html;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

/**
 * 应用启动自动加载
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 23:22
 */
public class SeleniumTest01 {
    static String url = "https://www.147xs.org/book/150820/226230160.html";
    static String pathname = "D:\\JavaWorkSpace\\IdeaProjects\\fictionweb\\config\\chromedriver.exe";


    public static void main(String[] args) throws Exception {
//        WebDriverManager.chromedriver().setup();
//        WebDriverManager.edgedriver().setup();
        WebDriverManager.firefoxdriver().setup();

//        System.getProperties().setProperty("webdriver.chrome.driver", pathname);
        ChromeOptions chromeOptions = new ChromeOptions();
//        WebDriver driver = new ChromeDriver(chromeOptions);
//        WebDriver driver = new EdgeDriver();
        WebDriver driver = new FirefoxDriver();
        try {
            driver.get(url);
            System.out.println("driver.getPageSource() = \n" + driver.getPageSource());
            /*Document document = Jsoup.parse(driver.getPageSource());
            Elements elements = document.selectXpath("//div[@id='content']/p/text()");
            for (Element element : elements) {
                String text = element.text();
                System.out.println("text = " + text);
            }*/

            /*JXDocument jxDocument = JXDocument.create(driver.getPageSource());
            List<JXNode> jxNodes = jxDocument.selN("//div[@id='content']/p/text()");
            for (JXNode jxNode : jxNodes) {
                String text = jxNode.asString();
                System.out.println("text = " + text);
            }*/
            List<WebElement> elements = driver.findElements(By.xpath("//div[@id='content']/p"));
            for (WebElement element : elements) {
                String text = element.getText();
                System.out.println("text = " + text);
            }
            List<WebElement> as = driver.findElements(By.xpath("//div[@class='bottem2']//a"));

            //移动到底部
           ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            //移动到指定元素，且元素底部和窗口底部对齐 参考 https://www.cnblogs.com/testway/p/6693140.html
            /*((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);"
                    , driver.findElements(By.xpath("//div[@class='bottem2']//a")).get(2)
            );*/
            as.get(2).click();
//            Thread.sleep(1000);
            System.out.println("driver.getPageSource() = \n" + driver.getPageSource());

            as = driver.findElements(By.xpath("//div[@class='bottem2']//a"));

            //移动到底部
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            as.get(2).click();
            System.out.println("driver.getPageSource() = \n" + driver.getPageSource());

        } finally {
            driver.quit();
        }

    }

}
