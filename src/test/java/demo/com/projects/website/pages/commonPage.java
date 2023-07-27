package demo.com.projects.website.pages;

import demo.com.constants.FrameworkConstants;
import demo.com.hooks.TestContext;
import demo.com.keywords.WebUI;
import demo.com.projects.website.model.Product;
import demo.com.projects.website.utils.ResponseHandler;
import demo.com.utils.LogUtils;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class commonPage {
    private static By buttonMenu = By.xpath("//div[text()='Categories']/../following::div[1]/button");
    private static By itemFilter = By.xpath("//li[text()='Filter']");
    private static By dropdownColumn = By.xpath("//div[contains(@class,'columnSelect')]//select");
    private static By dropdownOperator = By.xpath("//div[contains(@class,'operatorSelect')]//select");
    private static By inputFilterValue = By.xpath("//input[@placeholder='Filter value']");
//    private static By listCategories = By.xpath("//div[@data-field='categories' and @role='cell']");
    private static By buttonPrevPage = By.xpath("//button[@title='Previous page']");
    private static By buttonNextPage = By.xpath("//button[@title='Next page']");

    private static By headerCategory = By.xpath("//div[@class='MuiDataGrid-columnHeaderTitle' and text()='Categories']");

    private static Response response;
    public void navigateToTestPage() {
        WebUI.navigateToTestPage();
    }

    public void openFormFilter(){
        WebUI.hoverOnElement(headerCategory);
        WebUI.clickElement(buttonMenu);
        WebUI.clickElement(itemFilter);
    }

    public void selectColumnItem(String text){
        WebUI.selectOptionByText(dropdownColumn, text);
    }

    public void selectOperatorItem(String text){
        WebUI.selectOptionByText(dropdownOperator, text);
    }

    public void enterFilterValue(String value) {
        WebUI.setText(inputFilterValue, value);
    }

    public void goNextPage(){
        if(WebUI.isElementEnable(buttonNextPage))
            WebUI.clickElement(buttonNextPage);
    }

    public void verifyFilterResultIsCorrect(String criteria, String value) {
        boolean canNextPage=true;
        do{
            if(!WebUI.isElementEnable(buttonNextPage)) canNextPage=false;
            List<WebElement> listFilterResult = WebUI.getWebElements(By.xpath("//div[@data-field='" + criteria.toLowerCase() + "' and @role='cell']"));
            for(WebElement resultItem:listFilterResult){
                String resultIemText = resultItem.getText().trim();
                boolean result = resultIemText.contains(value.trim());
                LogUtils.info("[Cell.getText(): " + resultIemText);
                Assert.assertTrue(result, "The actual text is " + resultIemText + " not contains " + value.trim());
            }
            goNextPage();
        }while (canNextPage);
    }

    public void requestGetAllProducts() {
        response = TestContext.requestSetup()
                .when().log().all()
                .get("/test-site-qa/products.json")
                .then().log().all().extract().response();
    }

    public void verifyGetAllProductsResponse(int numberItem) {
        List<String> listId = response.getBody().jsonPath().getList("id");
        assertEquals("Number of Products is "+numberItem, listId.size(), numberItem);
        LogUtils.info("Verify Product Request response correctly");
    }
}
