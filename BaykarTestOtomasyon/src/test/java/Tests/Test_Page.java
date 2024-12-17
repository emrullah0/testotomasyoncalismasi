package Tests;

import Pages.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test_Page {

    public WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @AfterMethod
    public void testStatusControl(ITestResult result) {
        if(result.getStatus() == ITestResult.FAILURE) {
            String date = new SimpleDateFormat("_ddMMyy _ hh_mm_ss").format(new Date());
            String methodName = result.getMethod().getMethodName();
            String className = result.getTestClass().getRealClass().getSimpleName();
            String fileName = "src/test/java/ErrorScreenshots/" + className + "/" + methodName + "/" + date + ".png";
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        if(driver != null){
            driver.quit();
        }
    }
}
