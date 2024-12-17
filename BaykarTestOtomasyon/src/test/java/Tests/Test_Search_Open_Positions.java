package Tests;

import Pages.OpenPositionsPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Test_Search_Open_Positions extends Test_Page {

    private OpenPositionsPage openPositionsPage;
    private By unit = By.cssSelector("#searchInput");
    private final By unitElements = By.cssSelector("#myUL2 > div > li > div > label");
    private By position = By.cssSelector("#search");
    private final By positionsElements = By.cssSelector("#filterOpenPositions > div > div > div > h3");
    WebElement positionSearchBox;



    @DataProvider(name = "UnitAndPositionTestData")
    public Object[][] UnitAndPositionTestData() throws IOException {
        LinkedHashMap<String, List<String>> UnitAndPositionDataMap = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            UnitAndPositionDataMap = objectMapper.readValue(new File(openPositionsPage.jsonFilePath), LinkedHashMap.class);
            Object[][] testData = new Object[UnitAndPositionDataMap.size()][2];
            int i = 0;
            for (Map.Entry<String, List<String>> entry : UnitAndPositionDataMap.entrySet()) {
                testData[i][0] = entry.getKey();
                testData[i][1] = entry.getValue();
                i++;
            }
            return testData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @BeforeTest
    public void setUpPage() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        openPositionsPage = new OpenPositionsPage(driver);

    }

    @Test(priority = 1)
    public void openBaykarCareerPage() {
        openPositionsPage.openPage("https://kariyer.baykartech.com");
        openPositionsPage.waitUntilPageLoaded();
        String pageUrl = driver.getCurrentUrl();
        Assert.assertEquals(pageUrl, "https://kariyer.baykartech.com/tr/");;
    }

    @Test(priority = 2,dependsOnMethods =  "openBaykarCareerPage")
    public void goToOpenPositionsPage() throws InterruptedException{
        openPositionsPage.goOpenPositionsPage();
        Assert.assertTrue(driver.getCurrentUrl().contains( "open-positions"),"Yanlış sayfa açılmıştır. Açılan sayfa url : " + driver.getCurrentUrl());
    }
    @Test(priority = 3, dependsOnMethods ="goToOpenPositionsPage")
    public void getUnitsAndPositions() throws InterruptedException {
       openPositionsPage.getUnitAndPositons();

    }
    @Test(priority = 4,dependsOnMethods ="getUnitsAndPositions" )
    public void statusWriteJsonFile(){
        openPositionsPage.writeJsonFile();
    }

    @Test(priority = 5, dependsOnMethods = "statusWriteJsonFile", dataProvider = "UnitAndPositionTestData")
    public void searchUnitAndPosition(String Unit, List<String> Positions) throws InterruptedException{
        WebElement unitSearchBox = driver.findElement(unit);
        unitSearchBox.clear();
        unitSearchBox.sendKeys(Unit);
        Thread.sleep(500);
        openPositionsPage.pageWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(unitElements));
        List<WebElement> unitList = driver.findElements(unitElements);
        boolean unitFound = false;
        for (WebElement unitElement : unitList) {
            if (unitElement.getText().contains(Unit)) {
                unitFound = true;
                unitElement.click();
                break; }
        }
        Assert.assertTrue(unitFound, "Aranan birim ile sonuçlar uyuşmamaktadır");
        Thread.sleep(500);
        for (String Position : Positions) {
            positionSearchBox = driver.findElement(position);
            positionSearchBox.clear();
            positionSearchBox.sendKeys(Position);
            Thread.sleep(2000);
            openPositionsPage.pageWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(positionsElements));
            List<WebElement> positionElement = driver.findElements(positionsElements);
            boolean positionFound = false;
            for (WebElement element : positionElement) {
                if (element.getText().contains(Position)) {
                    positionFound = true;
                    break;
                }
            }
            Assert.assertTrue(positionFound, "Aranan pozisyon ile sonuçlar uyuşmamaktadır " + "Aranan Pozisyon : " + Position  );
            openPositionsPage.pageWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(positionsElements));
        }
        positionSearchBox.clear();


    }

}
