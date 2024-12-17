package Tests;

import Pages.MainPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;

public class Test_Language_Change_And_Control extends Test_Page {


    private MainPage mainPage;
    private final List<String> supportedLanguages = Arrays.asList("EN", "TR");
    private final Map<String,String[]> trTextsMap = new LinkedHashMap<>();
    private final Map<String,String[]> enTextsMap = new LinkedHashMap<>();
    private final Map<String, Map<String,String[]>> languageTextsMap = new LinkedHashMap<>();
    private Map<String, List<String>> navBarElemenetsMap;

    @BeforeTest
    public void setUpPage() {

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        mainPage = new MainPage(driver);

        trTextsMap.put("KARİYER", new String[]{"Yüksek İrtifa", "Yerleşkelerimiz", "Sosyal Alanlar"});
        trTextsMap.put("AÇIK POZİSYONLAR", new String[]{});
        trTextsMap.put("STAJ", new String[]{});
        trTextsMap.put("S.S.S", new String[]{});
        trTextsMap.put("BAYKAR", new String[]{});
        trTextsMap.put("GİRİŞ", new String[]{});
        trTextsMap.put("EN", new String[]{});
        trTextsMap.put("Köklerden\nGöklere", new String[]{});
        languageTextsMap.put("TR", trTextsMap);

        enTextsMap.put("CAREER", new String[]{"High Altitude","Our campuses", "Social Areas"});
        enTextsMap.put("OPEN POSITIONS", new String[]{});
        enTextsMap.put("INTERNSHIP", new String[]{});
        enTextsMap.put("FAQ", new String[]{});
        enTextsMap.put("BAYKAR", new String[]{});
        enTextsMap.put( "LOGIN", new String[]{});
        enTextsMap.put("TR", new String[]{});
        enTextsMap.put( "From the Roots\nto the Skies", new String[]{});
        languageTextsMap.put( "EN", enTextsMap);


    }

    @Test(priority = 1)
    public void openBaykarCareerPage() {
        mainPage.openPage("https://kariyer.baykartech.com");
        mainPage.waitUntilPageLoaded();
        String pageUrl = driver.getCurrentUrl();
        Assert.assertEquals(pageUrl, "https://kariyer.baykartech.com/tr/");;
    }

    @Test(priority = 2,dependsOnMethods = "openBaykarCareerPage")
    public void languageChange() {
        for (String language : supportedLanguages) {
            WebElement languageButton = driver.findElement(By.linkText(language));
            languageButton.click();
            mainPage.waitUntilPageLoaded();
            Assert.assertTrue(driver.getCurrentUrl().contains(language.toLowerCase()));
            navBarElemenetsMap = mainPage.findNavBarElements();
            WebElement slogan = driver.findElement(By.cssSelector("#mainScroll"));
            String sloganText = slogan.getText();
            navBarElemenetsMap.put(sloganText, new ArrayList<>());

            for (Map.Entry<String, String[]> entry :languageTextsMap.get(language).entrySet()) {
                String navBarHeaderText = entry.getKey();
                String[] subItemsTexts = entry.getValue();
                Assert.assertTrue(navBarElemenetsMap.containsKey(navBarHeaderText), "Web sayfasınde beklenen dilde navigasyon menüsü başlığı bulunanamıştır. : " + navBarHeaderText);
                List<String> actualSubItems = navBarElemenetsMap.get(navBarHeaderText);
                Assert.assertEquals(actualSubItems.size(), subItemsTexts.length, "Dropdown menüsü başlıklarının sayısı uyuşmamaktadır. İlgili dropdown menüsü başlığı : " + navBarHeaderText);
                for (int i = 0; i < subItemsTexts.length; i++) {
                    Assert.assertEquals(actualSubItems.get(i), subItemsTexts[i], "Dropdown menüsü öğesi beklenen dilde çevirisi hatalıdır. Beklenen Text : " + subItemsTexts[i]+ " Sayfada Tespit Edilen Text :" + actualSubItems.get(i));
                }
            }
        }
    }


}
