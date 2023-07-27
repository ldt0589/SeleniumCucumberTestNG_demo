/*
 * Copyright (c) for Demo .
 * Automation Framework Selenium - Demo
 */

package demo.com.driver;

import demo.com.constants.FrameworkConstants;
import demo.com.enums.Target;
import demo.com.exceptions.TargetNotValidException;
import org.openqa.selenium.WebDriver;

public class TargetFactory {

    public WebDriver createInstance() {
        Target target = Target.valueOf(FrameworkConstants.TARGET.toUpperCase());
        WebDriver webdriver;

        switch (target) {
            case LOCAL:
                //Create new driver from Enum setup in BrowserFactory class
                webdriver = BrowserFactory.valueOf(FrameworkConstants.BROWSER.toUpperCase()).createDriver();
                break;
            default:
                throw new TargetNotValidException(target.toString());
        }
        return webdriver;
    }

    public WebDriver createInstance(String browser) {
        Target target = Target.valueOf(FrameworkConstants.TARGET.toUpperCase());
        WebDriver webdriver;

        switch (target) {
            case LOCAL:
                //Create new driver from Enum setup in BrowserFactory class
                webdriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
                break;
            default:
                throw new TargetNotValidException(target.toString());
        }
        return webdriver;
    }

}