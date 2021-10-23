package com.example.afip.gringa.jobs;

import com.example.afip.gringa.dto.User;
import com.example.afip.gringa.service.AfipService;
import com.example.afip.gringa.service.EmailService;
import com.example.afip.gringa.service.PrinterService;
import com.poiji.bind.Poiji;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static io.github.bonigarcia.wdm.config.DriverManagerType.FIREFOX;

@Service
public class AfipJob {

    private WebDriver driver;

    private List<User> userList;

    @Autowired
    private AfipService afipService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PrinterService printerService;

    @Value("${excel.url}")
    private String excelUrl;

    @Value("${firefox.download}")
    private String firefoxDownload;

    public void execute() throws InterruptedException {

        userList = Poiji.fromExcel(new File(excelUrl), User.class);

        driver = openConnection();

        afipService.loginWebAfip(driver);

        afipService.goOnlineVouchers(driver);

        for (User user : userList) {
            createInvoice(user);
        }

        driver.close();

    }

    private void createInvoice(User user) throws InterruptedException {

        afipService.goGenerateVouchers(driver);

        afipService.selectPointOfSale(driver);

        afipService.selectVoucherType(driver, user);

        afipService.continueButton(driver);

        afipService.continueButton(driver);

        if (user.getInvoice().equals("Recibo A")) {
            afipService.conditionAgainstIVAA(driver);
        }

        if (user.getInvoice().equals("Recibo B")) {
            afipService.conditionAgainstIVAB(driver);
        }

        afipService.sendNumberDoc(driver, user);

        afipService.termsOfSale(driver);

        afipService.continueButton(driver);

        afipService.descriptionOfService(driver, user);

        afipService.typeOfIVA(driver);

        afipService.sendPrice(driver, user);

        afipService.continueButton(driver);

        afipService.confirm(driver);

        afipService.download(driver);

        afipService.menuButton(driver);

        afipService.getLastModifiedFile(user);

        //printerService.print(user.getFilePath());

        emailService.send(user.getDescription(),"FEE Mensual", user.getEmail(),user.getFilePath(),user.getDescription());


    }


    private WebDriver openConnection() {
        FirefoxProfile profile = new FirefoxProfile();

        String fullPath = System.getProperty("user.dir") + firefoxDownload;

        profile.setPreference("browser.download.folderList",2); //Use for the default download directory the last folder specified for a download
        profile.setPreference("browser.download.dir", fullPath); //Set the last directory used for saving a file from the "What should (browser) do with this file?" dialog.
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf"); //list of MIME types to save to disk without asking what to use to open the file
        profile.setPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer

        FirefoxOptions firefoxOptions = new FirefoxOptions();

        firefoxOptions.setProfile(profile);

        ChromeDriverManager.getInstance(FIREFOX).setup();
        driver = new FirefoxDriver(firefoxOptions);
        return driver;
    }

    private WebElement waitElement(By by, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return element;
    }
}
