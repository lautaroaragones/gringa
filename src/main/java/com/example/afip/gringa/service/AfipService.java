package com.example.afip.gringa.service;

import com.example.afip.gringa.dto.User;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;



@Service
public class AfipService {

    @Value("${afip.user}")
    private String afipUser;

    @Value("${afip.password}")
    private String afipPassword;

    @Value("${local.download}")
    private String localDownload;

    public void loginWebAfip(WebDriver driver){
        driver.manage().window().maximize();
        driver.get("https://auth.afip.gob.ar/contribuyente_/login.xhtml");
        waitElement(By.xpath("//*[@id=\"F1:username\"]"), driver).sendKeys(afipUser);
        waitElement(By.xpath("//*[@id=\"F1:btnSiguiente\"]"), driver).click();
        waitElement(By.xpath("//*[@id=\"F1:password\"]"), driver).sendKeys(afipPassword);
        waitElement(By.xpath("//*[@id=\"F1:btnIngresar\"]"), driver).click();
    }

    public void goOnlineVouchers(WebDriver driver) throws InterruptedException {
        waitElement(By.xpath("/html/body/main/section[2]/div/div[8]/div/div/div"), driver).click();
        Thread.sleep(4000);
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(0));
        driver.close();
        driver.switchTo().window(tabs2.get(1));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@value='MACCAM SRL']")).click();
        Thread.sleep(2000);
    }

    public void goGenerateVouchers(WebDriver driver) throws InterruptedException {
        driver.findElement(By.xpath("//*[@id='btn_gen_cmp']")).click();
        Thread.sleep(2000);
    }

    public void selectPointOfSale(WebDriver driver) throws InterruptedException {
        WebElement selectElement = driver.findElement(By.id("puntodeventa"));
        Select selectObject = new Select(selectElement);
        selectObject.selectByValue("6");
        Thread.sleep(2000);
    }

    public void selectVoucherType(WebDriver driver, User user) throws InterruptedException{
        WebElement selectElement2 = driver.findElement(By.id("universocomprobante"));
        Select selectObject2 = new Select(selectElement2);
        selectObject2.selectByVisibleText(user.getInvoice());
        Thread.sleep(2000);
    }

    public void sendNumberDoc(WebDriver driver, User user) throws InterruptedException {
        waitElement(By.xpath("//*[@id=\"nrodocreceptor\"]"), driver).sendKeys(user.getCuit());
        waitElement(By.xpath("//*[@id=\"nrodocreceptor\"]"), driver).click();
        Thread.sleep(2000);
    }

    public void conditionAgainstIVA(WebDriver driver) throws InterruptedException {
        WebElement selectElement3 = driver.findElement(By.id("idivareceptor"));
        Select selectObject3 = new Select(selectElement3);
        selectObject3.selectByVisibleText(" Consumidor Final");
        Thread.sleep(2000);
    }


    public void termsOfSale(WebDriver driver) throws InterruptedException {
        waitElement(By.xpath("//*[@id=\"formadepago1\"]"), driver).click();
        Thread.sleep(2000);
    }

    public void descriptionOfService(WebDriver driver, User user) throws InterruptedException {
        waitElement(By.xpath("//*[@id=\"detalle_descripcion\"]"), driver).sendKeys(user.getDescription());
        Thread.sleep(2000);
    }

    public void typeOfIVA(WebDriver driver) throws InterruptedException {
        WebElement selectElement4 = driver.findElement(By.id("detalle_tipo_iva"));
        Select selectObject4 = new Select(selectElement4);
        selectObject4.selectByVisibleText(" 21%");
        Thread.sleep(2000);
    }

    public void sendPrice(WebDriver driver, User user) throws InterruptedException {
        waitElement(By.xpath("//*[@id=\"detalle_precio\"]"), driver).sendKeys(user.getAmount());
        Thread.sleep(2000);
    }

    public void continueButton(WebDriver driver) throws InterruptedException {
        driver.findElement(By.xpath("//input[@value='Continuar >']")).click();
        Thread.sleep(2000);
    }

    public void confirm(WebDriver driver) throws InterruptedException {
        waitElement(By.xpath("//*[@id=\"btngenerar\"]"), driver).click();
        Thread.sleep(2000);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(2000);
    }

    public void download(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(5000);
        driver.findElement(By.xpath("//input[@value='Imprimir...']")).click();
        Thread.sleep(2000);
    }

    public void menuButton(WebDriver driver) throws InterruptedException {
        driver.findElement(By.xpath("//input[@value='Menú Principal']")).click();
        Thread.sleep(2000);
    }

    private WebElement waitElement(By by, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return element;
    }

    public void getLastModifiedFile(User user)
    {
        File directory = new File(System.getProperty("user.dir") + localDownload);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (File file : files)
            {
                if (file.lastModified() > lastModifiedTime)
                {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        user.setFilePath(chosenFile.getPath());

    }

}