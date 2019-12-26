package CommonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.PropertyFileUtil;
import junit.framework.Assert;

public class FunctionLibrary {
public static WebDriver driver;
//methods for launching browser
public static WebDriver startBrowser() throws Throwable
{
if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
{
	System.setProperty("webdriver.chrome.driver","D:\\Selenium_Frameworks\\ERP_Accounting\\CommonDrivers\\chromedriver.exe" );
	driver=new ChromeDriver();
	
}
else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
	{
		driver=new FirefoxDriver();
		
	}
	else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("ie"))
	{
		driver=new InternetExplorerDriver();
	}
	else
	{
		System.out.println("Brower is not matching");
	}
return driver;
}
//method for open application
public static void openApplication(WebDriver driver) throws Throwable
{
	driver.get(PropertyFileUtil.getValueForKey("Url"));
	driver.manage().window().maximize();
}
//method foe wait for element
public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String waittime)
{
	//wait for Explicit wait
	WebDriverWait mywait=new WebDriverWait(driver,Integer.parseInt(waittime));
	if(locatortype.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
	}
	else
	{
		System.out.println("unable to execute wait foe element application");
	}
}
//method for type action 
public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata)
{
	if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).clear();
		driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		
	}
	else	if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.id(locatorvalue)).clear();
		driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(locatorvalue)).clear();
		driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
	}
	else
	{
		System.out.println("unable to execure typeaction method ");
	}
}
//method for click action
public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
{
	if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(locatorvalue)).click();
		
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();
		}
		else
		{
			System.out.println("unable to click action method");
		}
}
public static void closeBrowser(WebDriver driver)
{
	driver.close();
}
public static String generateDate()
{
	Date date=new Date();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_ss");
	return sdf.format(date);
}
//method for capturing supplier number
public static void captureData(WebDriver driver,String locatortype,String locatorvalue) throws Throwable
{
	String snumber="";
	if(locatortype.equalsIgnoreCase("id"))
	{
		snumber=driver.findElement(By.id(locatorvalue)).getAttribute("value");
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		snumber=driver.findElement(By.name(locatorvalue)).getAttribute("value");
	}
	else
		if(locatortype.equalsIgnoreCase("xpath"))
		{
			snumber=driver.findElement(By.xpath(locatorvalue)).getAttribute("value");
		}
	
//writing into notepad
FileWriter fw=new FileWriter("D:\\Selenium_Frameworks\\ERP_Accounting\\CaptureData\\supplier.txt");
BufferedWriter bw=new BufferedWriter(fw);
bw.write(snumber);
bw.flush();
bw.close();
}
//table validation for supplier
public static void tableValidation(WebDriver driver,String column) throws Throwable
{
	FileReader fr=new FileReader("D:\\Selenium_Frameworks\\ERP_Accounting\\CaptureData\\supplier.txt");
	BufferedReader br=new BufferedReader(fr);
	String exp_data=br.readLine();
	//convert columndata into integer
	int columndata=Integer.parseInt(column);
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-text"))).isDisplayed());
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
	Thread.sleep(5000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-text"))).clear();
	Thread.sleep(5000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-text"))).sendKeys(exp_data);
	Thread.sleep(5000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
	WebElement table=driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("supplier-table")));
	//count no of row
	List <WebElement> rows=table.findElements(By.tagName("tr"));
	for(int i=1;i<rows.size();i++)
	{
		String act_data=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+columndata+"]/div/span/span")).getText();
		Thread.sleep(5000);
		System.out.println(exp_data+"  "+act_data);
		Thread.sleep(5000);
		Assert.assertEquals(exp_data, act_data,"snumber is not matching");
		break;
	}
	
}

}