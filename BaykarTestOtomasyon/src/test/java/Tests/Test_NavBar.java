package Tests;

import Pages.MainPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class Test_NavBar extends Test_Page {

    private MainPage mainPage;
    private Map<String, List<String>> navBarElemenetsMap;

    @BeforeTest
    public void setUpPage() {

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        mainPage = new MainPage(driver);

    }

    @Test(priority = 1)
    public void openBaykarCareerPage() {
        mainPage.openPage("https://kariyer.baykartech.com");
        mainPage.waitUntilPageLoaded();
        String pageUrl = driver.getCurrentUrl();
        Assert.assertEquals(pageUrl, "https://kariyer.baykartech.com/tr/");;
    }

    @Test(priority = 2,dependsOnMethods = "openBaykarCareerPage")
    public void readNavBarElements()  {
        navBarElemenetsMap = mainPage.findNavBarElements();
        System.out.println("Tespit Edilen NavBar Elementleri :" + navBarElemenetsMap);
    }

    @Test(priority = 3, dependsOnMethods = "readNavBarElements")
    public void testNavBar() throws InterruptedException, MalformedURLException {
        for (Map.Entry<String, List<String>> entry : navBarElemenetsMap.entrySet()) {
            String navBarText = entry.getKey();
            List<String> dropdownHeaderTexts = entry.getValue();
            WebElement navBarHeader = driver.findElement(By.linkText(navBarText));

            if(!dropdownHeaderTexts.isEmpty()) {
                for (String dropdownHeaderText : dropdownHeaderTexts) {
                    navBarHeader.click();
                    WebElement dropdownHeader = driver.findElement(By.linkText(dropdownHeaderText));
                    String dropdownHeaderUrl = dropdownHeader.getAttribute("href");
                    mainPage.pageWait().until(ExpectedConditions.visibilityOf(dropdownHeader));
                    dropdownHeader.click();
                    mainPage.pageWait().until(ExpectedConditions.invisibilityOf(dropdownHeader));
                    mainPage.waitUntilPageLoaded();
                    Assert.assertTrue(driver.getCurrentUrl().contains(dropdownHeaderUrl));
                    Thread.sleep(200);
                    driver.navigate().back();
                    mainPage.waitUntilPageLoaded();
                    mainPage.pageWait().until(ExpectedConditions.presenceOfElementLocated(mainPage.navbarSelector));
                }
            }else {
               String navBarHeaderUrl = navBarHeader.getAttribute("href");
                String navBarHeaderUrlPart = new URL(navBarHeaderUrl).getPath();
                Thread.sleep(200);
                navBarHeader.click();
                mainPage.waitUntilPageLoaded();
                boolean urlMatch = (driver.getCurrentUrl().contains(navBarHeaderUrl) || driver.getCurrentUrl().contains("/tr/hesaplar/login/?next=" + navBarHeaderUrlPart));
                Assert.assertTrue(urlMatch);
                Thread.sleep(200);
                driver.get("https://kariyer.baykartech.com");
                mainPage.waitUntilPageLoaded();
            }
        }
    }

}
