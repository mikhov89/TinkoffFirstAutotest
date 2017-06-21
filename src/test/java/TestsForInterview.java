import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ElementWorker;

import java.util.ArrayList;
import java.util.List;

import static utils.ElementWorker.checkVisibilityAndGet;

/**
 * Created by mikhov on 20.06.2017.
 * Реализовать тест согласно следующему сценарию:
 * 1.	Переходом по адресу https://www.tinkoff.ru/ загрузить стартовую страницу Tinkoff Bank.
 * 2.	Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
 * 3.	В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на  страницу выбора поставщиков услуг.
 * 4.	Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).
 * 5.	Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
 * 6.	На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
 * 7.	Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке (и их содержимое), которые появляются под соответствующим полем ввода в результате ввода некорректных данных.
 * 8.	Повторить шаг (2).
 * 9.	В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).
 * 10.	Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
 * 11.	Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“. Убедиться, что загруженная страница та же, что и страница, загруженная в результате шага (5).
 * 12.	Выполнить шаги (2) и (3).
 * 13.	В списке регионов выбрать “г. Санкт-Петербург”.
 * 14.	Убедится, что в списке поставщиков на странице выбора поставщиков услуг отсутствует искомый.
 */
public class TestsForInterview {

    WebDriver driver = Before.beforeTest();
    String forSearch;
    String neededPage;

    /**
     * Created by mikhov on 21.06.2017.
     * Проверка перехода к странице платежей ЖКУ-Москва, сохранение имени искомого жлемента и проверки заполнения полей
     */
    @Test(priority = 0)
    public void tbFirstTest() throws Exception {

        getToComPaymentsPage(driver);
        regionCheck(driver, "Москв");
        List<String> list = getFirstAndRememberName(driver);
        forSearch = list.get(0);
        neededPage = list.get(1);
        fieldsCheck(driver);

    }


    /**
     * Created by mikhov on 21.06.2017.
     * Переход к ЖКУ-Москва через поиск, переход и проверка открытой страницы на совпадение
     */
    @Test(priority = 1)
    public void tbSTest() throws Exception {
        checkVisibilityAndGet(driver, "//div[3]//span[text()=\"Платежи\"]").click();
        checkVisibilityAndGet(driver, "//input[contains(@class, \"search\")]").sendKeys(forSearch);
        WebElement needed = checkVisibilityAndGet(driver, "//span[@class =\"ui-link ui-search-flat__item\"][1]//div[@class=\"ui-search-flat__title-box\"]");
        Assert.assertEquals(forSearch, needed.getText());
        needed.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.urlToBe(neededPage));

    }

    /**
     * Created by mikhov on 21.06.2017.
     * Переключение региона на Санкт-Петербург и проверка, что там нет ЖКУ-Москва
     */
    @Test(priority = 2)
    public void tbTTest() throws Exception {
        getToComPaymentsPage(driver);
        regionCheck(driver, "Санкт");
        WebElement neededElement = checkVisibilityAndGet(driver, "//ul/li[1]/span[2]/a/span");
        Assert.assertNotEquals(forSearch, neededElement.getText());
        driver.quit();
    }

    public void getToComPaymentsPage(WebDriver driver) {

        checkVisibilityAndGet(driver, "//div[3]//span[text()=\"Платежи\"]").click();
        checkVisibilityAndGet(driver, "//span[text()=\"Коммунальные платежи\"]").click();
        ElementWorker.titleCheck(driver, "Оплата ЖКХ без комиссии. Коммунальные платежи онлайн");
    }

    public void regionCheck(WebDriver driver, String neededRegion) throws InterruptedException {
        WebElement region = checkVisibilityAndGet(driver, "//span[@class = \"ui-link payment-page__title_inner\"]");
        String regionName = region.getText();
        if (regionName.contains(neededRegion)) {
            System.out.println("Регион уже был указан как " + regionName);
        } else {
            region.click();
            checkVisibilityAndGet(driver, "//input[contains(@class, \"ui-input__field\")]");
            driver.findElement(By.xpath("//input[contains(@class, \"ui-input__field\")]")).sendKeys(neededRegion);
            WebElement newRegion = checkVisibilityAndGet(driver, "//span[contains(text(),\"" + neededRegion + "\")]");
            System.out.println("Выбран регион " + newRegion.getText());
            newRegion.click();

        }
    }

    public void fieldsCheck(WebDriver driver) throws Exception {
        WebElement payerCode = checkVisibilityAndGet(driver, " .//*[@id='payerCode']");
        WebElement providerPeriod = checkVisibilityAndGet(driver, "//input[@name=\"provider-period\"]");
        WebElement assureSum = checkVisibilityAndGet(driver, "//input[following-sibling::span[contains(text(), \"Сумма добровольного страхования жилья из квитанции за ЖКУ в Москве,\")]]");
        WebElement paymentSum = checkVisibilityAndGet(driver, "//input[following-sibling::*[contains(text(), \"Сумма платежа\")]]");
        //div[@class="ui-form-field-error-message"][text()="Поле неправильно заполнено"]
        ElementWorker.checkErrorExistenceAndFillRight(driver, payerCode, "Поле неправильно заполнено", "fdsfsd", "0123456789");
        ElementWorker.checkErrorExistenceAndFillRight(driver, providerPeriod, "Поле заполнено некорректно", "3202", "05.2017");
        ElementWorker.checkErrorExistenceAndFillRight(driver, assureSum, "", "dsfsd", "1111");
        //   ElementWorker.checkErrorExistenceAndFillRight(driver, paymentSum, "Максимальная сумма перевода - 15 000", "25772422", "10000");
    }

    public List<String> getFirstAndRememberName(WebDriver driver) {
        WebElement neededElement = checkVisibilityAndGet(driver, "//ul/li[1]/span[2]/a/span");
        List<String> neededList = new ArrayList<String>();
        neededList.add(0, neededElement.getText());
        neededElement.click();
        checkVisibilityAndGet(driver, "//span[text()=\"Оплатить ЖКУ в Москве\"]").click();
        neededList.add(1, driver.getCurrentUrl());
        return neededList;
    }
}
