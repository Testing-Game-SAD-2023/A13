package com.example;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {
    private static ChromeDriver driver;
    private static int timeout = 10;

    @BeforeClass
    public static void setDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\pasqu\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    }

    @Before
    public void openBrowser(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    } 

    @After
    public void closeBrowser(){
        driver.close();
    } 

    @Test
    public void validCredentials(){
        driver.get("http://localhost/login");	
        driver.findElement(By.id("email")).sendKeys("pippobaudo@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Pippino0");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        String urlPaginaDiRedirezione = "http://localhost/main";
        try {
            wait.until(ExpectedConditions.urlToBe(urlPaginaDiRedirezione));
        } catch(TimeoutException e) {
            Assert.fail();
        }

        Assert.assertEquals("Test fallito! Il login non è avvenuto correttamente.", driver.getCurrentUrl(), urlPaginaDiRedirezione);
    }

    @Test
    public void invalidCredentials(){
        driver.get("http://localhost/login");	
        driver.findElement(By.id("email")).sendKeys("pippobaudo@gmail.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.textToBe(By.tagName("body"), "Incorrect password"));
        } catch(TimeoutException e) {
            Assert.fail();
        }

        Assert.assertEquals("Test fallito! Il login è avvenuto correttamente.", driver.findElement(By.tagName("body")).getText(), "Incorrect password");
    }
}
