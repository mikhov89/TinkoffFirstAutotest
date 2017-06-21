package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by mikho on 20.06.2017.
 * Вынесены основные методы, которые могут быть использованы не только в этих тестах
 */
public class ElementWorker {
    /**
     * Првоерка существует ли элемент без падения тестов
     *
     * @param driver - текущий вебдраййвер
     * @param path   - xpath до элемента
     */

    public static boolean findCheck(WebDriver driver, String path) {
        if (driver.findElements(By.xpath(path)).size() > 0)
            return true;
        else
            return false;
    }

    /**
     * Прилось создать метод, вместо стандатрного обхявления Webelement, так как было много падений теста из-за отсутствия ожидания
     *
     * @param driver - текущий вебдраййвер
     * @param path   - xpath до элемента
     */
    public static WebElement checkVisibilityAndGet(WebDriver driver, String path) {
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(path)));
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
        WebElement wl = driver.findElement(By.xpath(path));
        return wl;
    }

    /**
     * Вынесена проверка заголовка для упрощения чтения кода
     *
     * @param driver - текущий вебдраййвер
     * @param title  - ожидаемый заголовок
     */
    public static void titleCheck(WebDriver driver, String title) {

        new WebDriverWait(driver, 10).until(ExpectedConditions.titleIs(title));
    }

    /**
     * Проверка появления ошибки с определенным текстом
     *
     * @param driver  - текущий вебдрайвер
     * @param message - сообщение об ошибке
     */
    public static void checkErrorExistence(WebDriver driver, String message) throws Exception {
        try {
            (new WebDriverWait(driver, 10)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("//div[contains(@class,\"ui-form-field-error-message\")][contains(text(),\"" + message + "\")]")));
        } catch (TimeoutException ex) {
            throw new Exception("Предупреждение с текстом \"" + message + "\" не появилось");
        }

    }

    /**
     * Проверка появления ошибки с определенным текстом
     *
     * @param driver  - текущий вебдрайвер
     * @param message - сообщение об ошибке
     * @param w       - вебэлемент (input) для которого проверяется появление ошибки
     * @param wrong   - строка с неверным заполением
     * @param wright  - верное заполнение после проверки ошибки
     */
    public static void checkErrorExistenceAndFillRight(WebDriver driver, WebElement w, String message, String wrong, String wright) throws Exception {
        w.sendKeys(wrong);
        w.submit();
        checkErrorExistence(driver, message);
        w.clear();
        w.sendKeys(wright);
        System.out.println("Проверка элемента " + w + " прошла успешно");
    }

}