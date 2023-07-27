package demo.com.hooks;

import demo.com.driver.DriverManager;
import demo.com.keywords.WebUI;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @BeforeAll
    public static void before_all() {
        System.out.println("================ BEFORE ALL ================");
//        PropertiesHelpers.loadAllFiles(); //Load Config and Locators
    }

    @AfterAll
    public static void after_all() {
        System.out.println("================ AFTER ALL ================");
        
    }

    @Before
    public void beforeScenario() {
        System.out.println("Starting Driver in Hooks: " + DriverManager.getDriver());
    }

    @After
    public void afterScenario(Scenario scenario) {
        //System.out.println("Stop Driver in Hooks: " + DriverManager.getDriver());
        WebUI.sleep(1);
        DriverManager.quit();
        WebUI.stopSoftAssertAll();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        //validate if scenario has failed then Screenshot
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot Failed");
        }
    }

}
