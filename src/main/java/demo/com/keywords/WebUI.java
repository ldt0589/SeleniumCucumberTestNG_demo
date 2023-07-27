/*
 * Copyright (c) for Demo Automation
 * Automation Framework Selenium
 */

package demo.com.keywords;

import demo.com.utils.DateUtils;
import demo.com.utils.LogUtils;
import demo.com.constants.FrameworkConstants;
import demo.com.driver.DriverManager;
import demo.com.helpers.Helpers;
import demo.com.report.ExtentReportManager;
import demo.com.report.ExtentTestManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.*;

/**
 * Keyword WebUI là class chung làm thư viện xử lý sẵn với nhiều hàm custom từ Selenium và Java.
 * Trả về là một Class chứa các hàm Static. Gọi lại dùng bằng cách lấy tên class chấm tên hàm (WebUI.method)
 */
public class WebUI {

    private static SoftAssert softAssert = new SoftAssert();

    public static void stopSoftAssertAll() {
        softAssert.assertAll();
    }

    public static void smartWait() {
        if (FrameworkConstants.ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
    }

    public static void addScreenshotToReport(String screenshotName) {
        if (FrameworkConstants.SCREENSHOT_ALL_STEPS.equals(FrameworkConstants.YES)) {
            if (ExtentTestManager.getExtentTest() != null)
                ExtentReportManager.addScreenShot(Helpers.makeSlug(screenshotName));
        }
    }

    public static String getPathDownloadDirectory() {
        String path = "";
        String machine_name = System.getProperty("user.home");
        path = machine_name + File.separator + "Downloads";
        return path;
    }

    public static boolean verifyFileContainsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getPathDownloadDirectory();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                flag = false;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(fileName)) {
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    public static boolean verifyFileEqualsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getPathDownloadDirectory();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                flag = false;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(fileName)) {
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là một đối tượng WebElement
     */
    public static WebElement getWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là Danh sách đối tượng WebElement
     */
    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }


    /**
     * Chờ đợi ép buộc với đơn vị là Giây
     *
     * @param second là số nguyên dương tương ứng số Giây
     */
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Get Page Title")
    public static String getPageTitle() {
        smartWait();
        String title = DriverManager.getDriver().getTitle();
        LogUtils.info("Get Page Title: " + DriverManager.getDriver().getTitle());
        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Get Page Title: " + DriverManager.getDriver().getTitle());
        }
        return title;
    }

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param objectListItem là locator của list item dạng đối tượng By
     * @param text           giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    @Step("Select Option Dynamic by Text {0}")
    public static boolean selectOptionDynamic(By objectListItem, String text) {
        smartWait();
        try {
            List<WebElement> elements = getWebElements(objectListItem);

            for (WebElement element : elements) {
                LogUtils.info("element.getText(): " + element.getText());
                String actualElementText = element.getText().toLowerCase().replaceAll("\\s+", "");
                String expectedElementText = text.toLowerCase().replaceAll("\\s+", "");
                if (actualElementText.contains(expectedElementText)){
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            e.getMessage();
        }
        return false;
    }
    @Step("Switch to Last Window")
    public static void switchToLastWindow() {
        smartWait();
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
    }

    @Step("Verify element exists {0}")
    public static boolean verifyElementExists(By by) {
        smartWait();

        boolean res;
        List<WebElement> elementList = getWebElements(by);
        if (elementList.size() > 0) {
            res = true;
            LogUtils.info("Element existing");
        } else {
            res = false;
            LogUtils.error("Element not exists");

        }
        return res;
    }

    @Step("Verify text of an element [Equals]")
    public static boolean verifyElementTextEquals(By by, String text) {
        smartWait();
        waitForElementVisible(by);

        String actualElementText = getTextElement(by).toLowerCase().replaceAll("\\s+", "");
        String expectedElementText = text.toLowerCase().replaceAll("\\s+", "");
        LogUtils.info("actualElementText: " + actualElementText);
        LogUtils.info("expectedElementText: " + expectedElementText);

        boolean result = actualElementText.equals(expectedElementText);

        if (result == true) {
            LogUtils.info("Verify text of an element [Equals]: " + result);
        } else {
            LogUtils.warn("Verify text of an element [Equals]: " + result);
        }

//        Assert.assertEquals(getTextElement(by).trim(), text.trim(), "The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.warning("Verify text of an element [Equals] : " + result);
            ExtentReportManager.warning("The actual text is '" + getTextElement(by).trim() + "' not equals '" + text.trim() + "'");
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return result;
    }

    @Step("Verify text {1} of element [Contains] {0}")
    public static boolean verifyElementTextContains(By by, String text) {
        smartWait();
        waitForElementVisible(by);
        String actualElementText = getTextElement(by).toLowerCase().replaceAll("\\s+", "");
        String expectedElementText = text.toLowerCase().replaceAll("\\s+", "");
        LogUtils.info("actual Phone: " + actualElementText);
        LogUtils.info("expected Phone: " + expectedElementText);

        boolean result = actualElementText.contains(expectedElementText);
//        boolean result = getTextElement(by).trim().contains(text.trim());

        if (result == true) {
            LogUtils.info("Verify text of an element [Contains]: " + result);
        } else {
            LogUtils.warn("Verify text of an element [Contains]: " + result);
        }

        Assert.assertTrue(result, "The actual text is " + getTextElement(by).trim() + " not contains " + text.trim());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.info("Verify text of an element [Contains] : " + result);
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

        return result;
    }
    @Step("Verify element present {0}")
    public static boolean verifyElementPresent(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUtils.info("Verify element present " + by);
            if (ExtentTestManager.getExtentTest() != null) {
                ExtentReportManager.info("Verify element present " + by);
            }
            addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
            return true;
        } catch (Exception e) {
            LogUtils.info("The element does NOT present. " + e.getMessage());
            Assert.fail("The element does NOT present. " + e.getMessage());
            return false;
        }
    }

    @Step("Verify element visible {0}")
    public static boolean isElementVisible(By by, long timeout) {
        smartWait();

        try {
            Boolean isVisible = DriverManager.getDriver().findElements(by).size() > 0;
            LogUtils.info("element gets visible: " + isVisible);
            return isVisible;
//            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
//            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
//            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify element enable {0}")
    public static boolean isElementEnable(By by) {
        smartWait();
        sleep(6);
        try {
            Boolean isEnable = DriverManager.getDriver().findElement(by).isEnabled();
            LogUtils.info("element gets enable: " + isEnable);
            return isEnable;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify element visible {0} with timeout {1} second")
    public static boolean verifyElementVisible(By by, long timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            LogUtils.info("Verify element visible " + by);
            return true;
        } catch (Exception e) {
            LogUtils.error("The element is not visible. " + e.getMessage());
            Assert.fail("The element is NOT visible. " + by);
            return false;
        }
    }

    @Step("Scroll to element {0}")
    public static void scrollToElementToTop(By by) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
        LogUtils.info("Scroll to element " + by);
    }

    @Step("Scroll to element {0}")
    public static void scrollToElementToBottom(By by) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", getWebElement(by));
        LogUtils.info("Scroll to element " + by);
    }

    /**
     *
     */
    @Step("Navigate To Test Page")
    public static void navigateToTestPage() {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);

        DriverManager.getDriver().get(FrameworkConstants.TEST_PAGE_URL);
        waitForPageLoaded();

        LogUtils.info("Open Test Page URL: " + FrameworkConstants.TEST_PAGE_URL);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Open Test Page URL: " + FrameworkConstants.TEST_PAGE_URL);
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
    }

    @Step("Select Option by Value {0}")
    public static void selectOptionByValue(By by, String value) {
        smartWait();

        Select select = new Select(getWebElement(by));
        select.selectByValue(value);
    }

    @Step("Select Option by Text {0}")
    public static void selectOptionByText(By by, String text) {
        smartWait();
        Select select = new Select(getWebElement(by));
        select.selectByVisibleText(text);
    }

    @Step("Hover on element {0}")
    public static boolean hoverOnElement(By by) {
        smartWait();

        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).perform();
            LogUtils.info("Hover on element " + by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Open Booking Page")
    public static void openBookingPage() {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);

        DriverManager.getDriver().get(FrameworkConstants.ADMIN_BOOKING_PAGE_URL);
        waitForPageLoaded();

        LogUtils.info("Open Booking Page URL: " + FrameworkConstants.ADMIN_BOOKING_PAGE_URL);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Open Admin URL: " + FrameworkConstants.ADMIN_BOOKING_PAGE_URL);
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());
    }

    /**
     * Open website with URL
     *
     * @param URL
     */
    @Step("Open website with URL {0}")
    public static void getURL(String URL) {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);

        DriverManager.getDriver().get(URL);
        waitForPageLoaded();

        LogUtils.info("Open URL: " + URL);

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Open URL: " + URL);
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Điền giá trị vào ô Text
     *
     * @param by    an element of object type By
     * @param value giá trị cần điền vào ô text
     */
    @Step("Set text on textbox")
    public static void setText(By by, String value) {
        waitForElementVisible(by).clear();
        waitForElementVisible(by).sendKeys(value);
        LogUtils.info("Set text " + value + " on " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Set text " + value + " on " + by.toString());
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click on element
     *
     * @param by an element of object type By
     */
    @Step("Click on the element {0}")
    public static void clickElement(By by) {
        waitForElementVisible(by).click();
        LogUtils.info("Clicked on the element " + by.toString());

        if (ExtentTestManager.getExtentTest() != null) {
            ExtentReportManager.pass("Clicked on the element " + by.toString());
        }
        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Get text of an element
     *
     * @param by an element of object type By
     * @return text of a element
     */
    @Step("Get text of element {0}")
    public static String getTextElement(By by) {
        smartWait();
        return waitForElementVisible(by).getText().trim();
    }

    /**
     * Lấy giá trị từ thuộc tính của element
     *
     * @param by            an element of object type By
     * @param attributeName tên thuộc tính
     * @return giá trị thuộc tính của element
     */
    @Step("Get attribute {1} of element {0}")
    public static String getAttributeElement(By by, String attributeName) {
        smartWait();
        return waitForElementVisible(by).getAttribute(attributeName);
    }
    //Wait Element

    /**
     * Chờ đợi element sẵn sàng hiển thị để thao tác theo thời gian tuỳ ý
     *
     * @param by      an element of object type By
     * @param timeOut thời gian chờ tối đa
     * @return một đối tượng WebElement đã sẵn sàng để thao tác
     */
    public static WebElement waitForElementVisible(By by, long timeOut) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));

            boolean check = verifyElementVisible(by, timeOut);
            if (check == true) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else {
                scrollToElementToTop(by);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            }
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
            LogUtils.error("Timeout waiting for the element Visible. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để thao tác
     *
     * @param by an element of object type By
     * @return một đối tượng WebElement đã sẵn sàng để thao tác
     */
    public static WebElement waitForElementVisible(By by) {
        smartWait();
        waitForElementPresent(by);

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT*20), Duration.ofMillis(500));
            boolean check = isElementVisible(by, 1);
            if (check == true) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else {
                scrollToElementToBottom(by);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            }
        } catch (Throwable error) {
            LogUtils.error("Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để CLICK
     *
     * @param by an element of object type By
     * @return một đối tượng WebElement đã sẵn sàng để CLICK
     */
    public static WebElement waitForElementClickable(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT*20), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.elementToBeClickable(getWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element ready to click. " + by.toString());
            LogUtils.error("Timeout waiting for the element ready to click. " + by.toString());
        }
        return null;
    }

    /**
     * Chờ đợi element sẵn sàng tồn tại trong DOM theo thời gian tuỳ ý
     *
     * @param by an element of object type By
     * @return một đối tượng WebElement đã tồn tại
     */
    public static WebElement waitForElementPresent(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT*10), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("Element not exist. " + by.toString());
            Assert.fail("Element not exist. " + by.toString());
        }
        return null;
    }

    /**
     * Kiểm tra giá trị từ thuộc tính của một element có đúng hay không
     *
     * @param by        an element of object type By
     * @param attribute tên thuộc tính
     * @param value     giá trị
     * @return true/false
     */
    @Step("Verify element {0} with attribute {1} has value is {2}")
    public static boolean verifyElementAttributeValue(By by, String attribute, String value) {
        smartWait();

        waitForElementVisible(by);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.attributeToBe(by, attribute, value));
            return true;
        } catch (Throwable error) {
            LogUtils.error("Object: " + by.toString() + ". Not found value: " + value + " with attribute type: " + attribute + ". Actual get Attribute value is: " + getAttributeElement(by, attribute));
            Assert.fail("Object: " + by.toString() + ". Not found value: " + value + " with attribute type: " + attribute + ". Actual get Attribute value is: " + getAttributeElement(by, attribute));
            return false;
        }
    }

    // Wait Page loaded

    /**
     * Chờ đợi trang tải xong (Javascript) với thời gian mặc định từ config
     */
    public static void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_PAGE_LOADED), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            LogUtils.info("Javascript in NOT Ready!");
            //Wait for Javascript to load
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                error.printStackTrace();
                Assert.fail("Timeout waiting for page load (Javascript). (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }
    }

}