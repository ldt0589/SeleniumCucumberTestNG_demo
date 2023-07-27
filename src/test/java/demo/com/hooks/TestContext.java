package demo.com.hooks;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import demo.com.driver.DriverManager;
import demo.com.driver.TargetFactory;
import demo.com.projects.website.pages.commonPage;
import demo.com.projects.website.utils.RestAssuredRequestFilter;
import demo.com.utils.LogUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private WebDriver driver;

    public TestContext() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
//        driver = ThreadGuard.protect(new TargetFactory().createInstance());
        driver = new TargetFactory().createInstance();
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
        LogUtils.info("Driver in TestContext: " + getDriver());
    }
    private commonPage commonPage;

    public commonPage getCommonPage() {
        if (commonPage == null) {
            commonPage = new commonPage();
        }
        return commonPage;
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private static final String CONTENT_TYPE = "application/json";

    public static RequestSpecification requestSetup() {
        RestAssured.reset();
        Options options = Options.builder().logStacktrace().build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);
        RestAssured.baseURI = "https://lcjg-betalabs.github.io";
        return RestAssured.given()
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE);
    }
}
