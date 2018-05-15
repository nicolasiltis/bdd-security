package net.continuumsecurity.examples.my_application;

import net.continuumsecurity.Config;
import net.continuumsecurity.Credentials;
import net.continuumsecurity.UserPassCredentials;
import net.continuumsecurity.behaviour.ILogin;
import net.continuumsecurity.behaviour.ILogout;
import net.continuumsecurity.behaviour.INavigable;
import net.continuumsecurity.web.WebApplication;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DigitalApiGatewayParty extends WebApplication implements ILogin,
        ILogout,INavigable {

    private final static Logger log = Logger.getLogger(DigitalApiGatewayParty.class.getName());

    public DigitalApiGatewayParty() {
        super();

    }

    @Override
    public void openLoginPage() {
        //No login page
    }

    @Override
    public void login(Credentials creds) {
        driver.get(Config.getInstance().getBaseUrl() + "calendar");
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
	
	//convenience method
	public void login(String username, String password) {
		login(new UserPassCredentials(username, password));
	}

    @Override
    public boolean isLoggedIn() {
        if (driver.getPageSource().contains("2018")) {
            return true;
        } else {
            return false;
}
    }

    @Override
    public void logout() {
        driver.get(Config.getInstance().getBaseUrl() + "logout");
    }
	
	public void bookingExternal(){
	    log.info("Beginning of external booking");
		driver.get(Config.getInstance().getBaseUrl() + "booking?date=2018-07-13T10:00:00.000Z&storeId=780073");
		log.info(driver.getPageSource());
		findAndWaitForElement(By.id("mat-input-10"));
		log.info(" mat-input-10 found");
		driver.findElement(By.xpath("//mat-card/span")).click();
        driver.findElement(By.xpath("//div/button")).click();
		findAndWaitForElement(By.id("mat-input-2"));
		log.info(" mat-input-2 found");
		driver.findElement(By.id("mat-input-2")).click();
		driver.findElement(By.id("mat-input-2")).clear();
		driver.findElement(By.id("mat-input-2")).sendKeys("test");
		driver.findElement(By.id("mat-input-3")).click();
		driver.findElement(By.id("mat-input-3")).clear();
		driver.findElement(By.id("mat-input-3")).sendKeys("10");
		driver.findElement(By.xpath("//label[2]")).click();
		driver.findElement(By.xpath("//div[2]/label")).click();
		driver.findElement(By.id("mat-input-4")).click();
		driver.findElement(By.id("mat-input-4")).clear();
		driver.findElement(By.id("mat-input-4")).sendKeys("10");
		driver.findElement(By.xpath("//div[5]/div[2]/label")).click();
		driver.findElement(By.xpath("//div[6]/div[2]/label[2]")).click();
		driver.findElement(By.xpath("//div[7]/div[2]/label")).click();
		driver.findElement(By.xpath("//button")).click();
		findAndWaitForElement(By.id("mat-input-5"));
		log.info(" mat-input-5 found");
		driver.findElement(By.id("mat-input-5")).click();
		driver.findElement(By.id("mat-input-5")).clear();
		driver.findElement(By.id("mat-input-5")).sendKeys("test");
		driver.findElement(By.id("mat-input-6")).click();
		driver.findElement(By.id("mat-input-6")).clear();
		driver.findElement(By.id("mat-input-6")).sendKeys("test");
		driver.findElement(By.xpath("//mat-form-field[3]/div/div/div")).click();
		driver.findElement(By.id("mat-input-7")).clear();
		driver.findElement(By.id("mat-input-7")).sendKeys("078 041 33 33");
		driver.findElement(By.id("mat-input-8")).click();
		driver.findElement(By.id("mat-input-8")).clear();
		driver.findElement(By.id("mat-input-8")).sendKeys("test@test.test");
		driver.findElement(By.id("mat-input-9")).click();
		driver.findElement(By.id("mat-input-9")).clear();
		driver.findElement(By.id("mat-input-9")).sendKeys("test@test.test");
		driver.findElement(By.xpath("//button")).click();
		findAndWaitForElement(By.className("legal-notice"));
		log.info("legal-notice found");
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		driver.findElement(By.xpath("//button")).click();
	}
	
	public void viewRoleTeleperformanceAdmin() {
		driver.findElement(By.xpath("//a[@id='admin-menu']/span/span/span")).click();
		driver.findElement(By.xpath("//div[@id='navbarResponsive']/ul/li[2]/ul/li/a/span/span")).click();
	}
	
	public void viewRoleTeleperformanceOp() {
		
	}
	
    public void navigate() {
		bookingExternal();
		
    }

}

