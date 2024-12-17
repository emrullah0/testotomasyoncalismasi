package Pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.NoSuchElementException;

public class OpenPositionsPage extends Page{

    private final By navBarOpenPage = By.cssSelector("#offcanvasNavbar > div.offcanvas-body > ul > li:nth-child(3) > a");
    private final By openPositionsButton = By.cssSelector("div.openPositions");
    private final By unitElements = By.cssSelector("#myUL2 > div > li > div > label");
    private final By positionsElements = By.cssSelector("#filterOpenPositions > div > div > div > h3");
    public final String jsonFilePath = "C:\\Users\\emrul\\OneDrive\\Masaüstü\\BaykarVakaÇalışması\\BaykarTestOtomasyon\\src\\test\\resources\\UnitAndPositionTestData.json";
    private Map<String, List<String>> UnitAndPositionsMap = new LinkedHashMap<>();
    private   JavascriptExecutor js = (JavascriptExecutor) driver;
    public WebElement targetElement;


    public OpenPositionsPage(WebDriver driver) {
        super(driver);
    }
    public void goOpenPositionsPage() throws InterruptedException {
        WebElement openPositions = driver.findElement(navBarOpenPage);
        pageWait().until(ExpectedConditions.visibilityOf(openPositions));
        openPositions.click();
        waitUntilPageLoaded();
        pageWait().until(ExpectedConditions.elementToBeClickable(openPositionsButton));
        driver.findElement(openPositionsButton).click();
        waitUntilPageLoaded();
        targetElement = driver.findElement(By.cssSelector("#filterIcon"));
        js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
        Thread.sleep(1000);
    }

    public void getUnitAndPositons() throws InterruptedException {
        for (int i = 0; i < driver.findElements(unitElements).size(); i++) {
            List<WebElement> unitList = driver.findElements(unitElements); //
            WebElement unitElement = unitList.get(i);
            pageWait().until(ExpectedConditions.elementToBeClickable(unitElement));
            unitElement.click();
            waitUntilPageLoaded();
            Thread.sleep(1000);
            List<String> positionsText = new ArrayList<>();

            boolean hasNextPage = true;
            while (hasNextPage) {
                List<WebElement> positionsList = driver.findElements(positionsElements);
                for (WebElement positionElement : positionsList) {
                    try {
                        positionsText.add(positionElement.getText());
                    } catch (StaleElementReferenceException e) {
                        positionsList = driver.findElements(positionsElements);
                        positionsText.add(positionElement.getText());
                    }
                }
                try {
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(500);
                    List<WebElement> nextPageButtons = driver.findElements(By.id("nextPageLink"));
                    if (!nextPageButtons.isEmpty()) {
                        WebElement nextPageButton = nextPageButtons.get(0);
                        pageWait().until(ExpectedConditions.elementToBeClickable(nextPageButton));
                        nextPageButton.click();
                        waitUntilPageLoaded();
                        js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
                        Thread.sleep(500);
                        nextPageButtons.clear();
                    } else {
                        hasNextPage = false;
                    }
                } catch (NoSuchElementException e) {
                    hasNextPage = false; //
                }
            }

            UnitAndPositionsMap.put(unitElement.getText(), positionsText);
            js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
            Thread.sleep(500);
            unitElement.click();
            WebElement filterbar = driver.findElement(By.cssSelector("#myUL2"));
            js.executeScript("arguments[0].scrollTop +=30;", filterbar);
            waitUntilPageLoaded();
            Thread.sleep(1000);
        }


    }
    public void writeJsonFile(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), UnitAndPositionsMap);

        }catch (IOException e){
            System.out.println("Hata oluştu" + e.getMessage());
        }
    }
}



