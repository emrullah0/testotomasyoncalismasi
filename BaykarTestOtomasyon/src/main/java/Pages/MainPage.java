package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;

public class MainPage extends Page {

    public By navbarSelector = By.cssSelector("ul.navbar-nav > li > a , div.offcanvas-body > ul > div > li");
    public By dropdownSelector = By.cssSelector("ul.navbar-nav > li >ul.dropdown-menu > li > a.dropdown-item ");
    

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public Map<String, List<String>> findNavBarElements() {
        List<WebElement> navBarHeaders = driver.findElements(navbarSelector);
        List<String> navBarHeadersText = new ArrayList<>();
        Map<String, List<String>> navBarHeadersMap = new LinkedHashMap<>();
        for (WebElement navBarHeader : navBarHeaders) {
            navBarHeadersText.add(navBarHeader.getText());
            if(navBarHeader.getAttribute("class").contains("dropdown-toggle")){
                navBarHeader.click();
                pageWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownSelector));
                List<WebElement> dropdownHeaders = driver.findElements(dropdownSelector);
                List<String> dropdownHeadersTexts = new ArrayList<>();
                for (WebElement dropdownHeader : dropdownHeaders) {
                    dropdownHeadersTexts.add(dropdownHeader.getText());
                }
                navBarHeadersMap.put(navBarHeader.getText(), dropdownHeadersTexts);
            } else {
                navBarHeadersMap.put(navBarHeader.getText(), new ArrayList<>());
            }
        }


    return navBarHeadersMap;
    }
}
