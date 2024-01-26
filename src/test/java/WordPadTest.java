import io.appium.java_client.windows.WindowsDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordPadTest {

    private static final int TIMEOUT_IN_SECONDS = 3;
    private WindowsDriver<WebElement> wordpadSession = null;

    public static String getDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("M/dd/yyyy"));
    }

    @BeforeEach
    public void setUp() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", properties.getProperty("wordpad"));
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");

            wordpadSession = new WindowsDriver<>(new URI("http://localhost:4723").toURL(), capabilities);
            wordpadSession.manage().timeouts().implicitlyWait(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        wordpadSession.quit();
    }

    @Test
    public void checkAboutWindow() {
        wordpadSession.findElement(By.name("File tab")).click();
        wordpadSession.findElement(By.name("About WordPad")).click();
        wordpadSession.findElement(By.name("OK")).click();
    }

    @Test
    public void checkDateForming() {
        wordpadSession.findElement(By.name("Date and time")).click();
        wordpadSession.findElement(By.name("OK")).click();
        WebElement textField = wordpadSession.findElement(By.name("Rich Text Window"));
        assertEquals(
                getDate(),
                textField.getText().trim().replace("\u200E", "")
        );
        textField.sendKeys(Keys.LEFT_CONTROL, "a");
        textField.sendKeys(Keys.DELETE);
        // PROBLEM!
    }
}
