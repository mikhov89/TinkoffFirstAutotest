import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by mikho on 20.06.2017.
 * Класс для инициализации драйвера и других методов BeforeTest
 */

public class Before {

    /**
     * Created by mikho on 20.06.2017.
     * В лучае отсутвия системной переменной для ChromeDriver прописывает относительный путь для переменной "webdriver.chrome.driver"
     * Примечание: если переменной нет, то нужно вызывать этот метод в BeforeTest и создавать объект WebDriver внути каждого теста
     */
    public static void setProperties()  {
        Properties props = new Properties();
        try{
            FileInputStream fin = new FileInputStream("src\\test\\resources\\config\\application.properties");
            props.load(fin);
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    System.out.println(props.getProperty("chromeDriverPath"));
        System.setProperty("webdriver.chrome.driver", props.getProperty("chromeDriverPath"));
        System.setProperty("webdriver.gecko.driver", props.getProperty("FFDriverPath"));
    }
    /**
     * Created by mikho on 20.06.2017.
     * Созхдание вебдрайвера и переход на стартовую
     */


    public static WebDriver beforeTest()  {
        setProperties();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.tinkoff.ru/ ");
        return driver;
}
}