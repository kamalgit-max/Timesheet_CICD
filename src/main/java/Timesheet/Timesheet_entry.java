package Timesheet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Timesheet_entry {

    public static void main(String[] args) throws InterruptedException, IOException {

        FileInputStream fis = new FileInputStream(new File("D:\\Users\\gurumurthyk\\B2B_BDD\\Timesheet_entry\\target\\config.properties"));
        Properties p = new Properties();
        p.load(fis);
        String alertpop = "";
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait ww = new WebDriverWait(driver,Duration.ofSeconds(20));
        WebDriverWait www= new WebDriverWait(driver,Duration.ofSeconds(5));
        LocalDate d = LocalDate.now();
        driver.get("https://www.ultimatix.net/");
        driver.findElement(By.cssSelector("#form1")).sendKeys("2788230", Keys.ENTER);
        ww.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#easyAuth-btn")));
        driver.findElement(By.cssSelector("#easyAuth-btn")).click();
        ww.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#menu-icon"))).click();
        ww.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='trending-table']/tbody/tr/td/div[contains(.,'Timesheet Entry')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(By.xpath("//table[@id='trending-table']/tbody/tr/td/div[contains(.,'Timesheet Entry')]")));
        driver.findElement(By.xpath("//table[@id='trending-table']/tbody/tr/td/div[contains(.,'Timesheet Entry')]")).click();
        List<String> a = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(a.get(1));
        ww.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td.selectedCell")));
        String date1 = driver.findElement(By.cssSelector("td.selectedCell")).getAttribute("id");
        List<WebElement> dd1 = driver.findElements(By.cssSelector("table[ng-if*='unassigned'] tbody"));
        int ss =dd1.size();
        driver.findElement(By.cssSelector("td.selectedCell")).click();
        try {
            alertpop = www.until(ExpectedConditions.alertIsPresent()).getText();
            www.until(ExpectedConditions.alertIsPresent()).accept();
            System.out.println(alertpop);
        } catch (Exception e) {
            System.out.println("Alert is not present");
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dd = LocalDate.parse(date1,df);
        System.out.println(String.valueOf(dd.getDayOfWeek()));
        String holidays = p.getProperty("Branch_holidays");
        List<String> bh = Arrays.asList(holidays);
        if(String.valueOf(dd.getDayOfWeek()).equalsIgnoreCase("saturday")||String.valueOf(dd.getDayOfWeek()).equalsIgnoreCase("sunday")||bh.contains(date1))
        {
            System.out.println("Today is may be weekend or branch holiday");
            driver.quit();
        }
        else if (alertpop.contains("applied leave")||ss>1)
        {
            System.out.println("You have applied leave today");
            driver.quit();

        } else if (ss==1)
        {
            ww.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id$='effortUnassign01']")));
            driver.findElement(By.cssSelector("input[id$='effortUnassign01']")).clear();
            driver.findElement(By.cssSelector("input[id$='effortUnassign01']")).sendKeys("9");
            driver.findElement(By.cssSelector("input[value='Submit']")).click();
            Thread.sleep(3000);
            driver.quit();
        }

    }
}
