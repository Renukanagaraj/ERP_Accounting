package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
public static WebDriver driver;
public static ExtentReports report;
public static ExtentTest test;
@Test
public void ERP_Test() throws Throwable
{
	//creating reference object foe excel util method
	ExcelFileUtil xl=new ExcelFileUtil();
	//iterating all row in mastertest case sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("y"))
{//store module name into Testcase(TCModule) module
		String TCModule=xl.getCellData("MasterTestCases",i, 1);
		//generate user define html report date formate
		report=new ExtentReports("./Reports//"+TCModule+FunctionLibrary.generateDate()+".html");
		//iterate all rows in TCModule sheet
		for(int j=1;j<=xl.rowCount(TCModule);j++)
		{
			test=report.startTest(TCModule);
		//read all columns from TC module(is a variable which is going to to store testcases)
		String Description=xl.getCellData(TCModule, j, 0);//bec selenium tool 
		String Function_Name=xl.getCellData(TCModule, j, 1);
		String Locator_Type=xl.getCellData(TCModule, j, 2);
		String Locator_Value=xl.getCellData(TCModule, j, 3);
		String Test_Data=xl.getCellData(TCModule, j, 4);
		try{
			if(Function_Name.equalsIgnoreCase("startBrowser"))
			{
				driver=FunctionLibrary.startBrowser();
				System.out.println("exceuting start browser");
				test.log(LogStatus.INFO,Description);
				
			}else if(Function_Name.equalsIgnoreCase("openApplication"))
			{
				FunctionLibrary.openApplication(driver);
				test.log(LogStatus.INFO,Description);
				
			}else if(Function_Name.equalsIgnoreCase("waitForElement"))
			{
				FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
				test.log(LogStatus.INFO,Description);
				
			}
			
			else if(Function_Name.equalsIgnoreCase("typeAction"))
				{
					FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
					test.log(LogStatus.INFO,Description);
					
				}
				else if(Function_Name.equalsIgnoreCase("clickAction"))
				{
					FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
					test.log(LogStatus.INFO,Description);
					
				}
				else if(Function_Name.equalsIgnoreCase("closeBrowser"))
				{
					FunctionLibrary.closeBrowser(driver);
					test.log(LogStatus.INFO,Description);
					
				}
				else
					if(Function_Name.equalsIgnoreCase("captureData"))
					{
						FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
						test.log(LogStatus. INFO,Description);
						
					}
					else
						if(Function_Name.equalsIgnoreCase("tableValidation"))
						{
							FunctionLibrary.tableValidation(driver, Test_Data);
							test.log(LogStatus. INFO,Description);
							
						}
			//write as pass into status column
			xl.setcelldata(TCModule, j, 5,"PASS");
			test.log(LogStatus.PASS,Description );
			xl.setcelldata("MasterTestCases", i, 3, "PASS");
		}catch(Exception e){
			System.out.println("exception handled ");
		xl.setcelldata(TCModule, j, 5, "FAIL");
		test.log(LogStatus.FAIL,Description );
		xl.setcelldata("MasterTestCases", j, 3, "FAIL");
		//take screen short and store
	File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	FileUtils.copyFile(screen, new File("./Screens//" +TCModule+FunctionLibrary.generateDate()+"Myscreen.png"));
		break;
		
		}
		report.endTest(test);
		report.flush();
		
	}
}
else
{
	xl.setcelldata("MasterTestCases", i, 3, "Not Exceute");
}
}
}
}
