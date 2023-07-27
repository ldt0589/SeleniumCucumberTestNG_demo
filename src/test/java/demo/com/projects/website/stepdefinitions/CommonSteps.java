package demo.com.projects.website.stepdefinitions;
import demo.com.hooks.TestContext;
import demo.com.projects.website.model.Product;
import demo.com.projects.website.pages.commonPage;
import demo.com.projects.website.utils.ResponseHandler;
import demo.com.utils.LogUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class CommonSteps {

    commonPage commonPage;

    public CommonSteps(TestContext testContext) {
        commonPage = testContext.getCommonPage();
    }

    @Given("Navigate to Test Page")
    public void navigateToTestPage() {
        commonPage.navigateToTestPage();
    }

    @Given("User opens form Filter")
    public void userOpensFormFilter() {
        commonPage.openFormFilter();
    }

    @When("User filters by {string} as {string}")
    public void userFiltersAs(String criteria, String value) {
        commonPage.selectColumnItem(criteria);
        commonPage.enterFilterValue(value);
    }

    @Then("Verify that filter result is correct with {string} and {string}")
    public void verifyThatFilterResultIsCorrect(String criteria, String value) {
        commonPage.verifyFilterResultIsCorrect(criteria, value);
    }

    @Then("Verify API response contains {int} objects")
    public void verifyAPIResponseContainsItems(int numberItem) {
        commonPage.requestGetAllProducts();
        commonPage.verifyGetAllProductsResponse(numberItem);
    }
}

