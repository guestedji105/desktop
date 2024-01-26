import io.appium.java_client.windows.WindowsDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private static final int TIMEOUT_IN_SECONDS = 5;
    private WindowsDriver<WebElement> calcSession = null;

    private WebElement calculatorResult;

    @BeforeEach
    public void setUp() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", properties.getProperty("calc"));
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");

            calcSession =
                    new WindowsDriver<>(new URI("http://localhost:4723").toURL(), capabilities);
            calcSession.manage().timeouts().implicitlyWait(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        calculatorResult = calcSession.findElementByAccessibilityId("CalculatorResults");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        calcSession.quit();
        Thread.sleep(1000);
    }

    @Test
    public void additionTest() {
        calcSession.findElementByName("One").click();
        calcSession.findElementByName("Plus").click();
        calcSession.findElementByName("Seven").click();
        calcSession.findElementByName("Equals").click();
        assertEquals("8", getCalculatorResultText());
    }

    @Test
    public void divisionTest() {
        calcSession.findElementByAccessibilityId("num8Button").click();
        calcSession.findElementByAccessibilityId("num8Button").click();
        calcSession.findElementByAccessibilityId("divideButton").click();
        calcSession.findElementByAccessibilityId("num1Button").click();
        calcSession.findElementByAccessibilityId("num1Button").click();
        calcSession.findElementByAccessibilityId("equalButton").click();
        assertEquals("8", getCalculatorResultText());
    }

    @Test
    public void multiplicationTest() {
        calcSession.findElementByXPath("//Button[@Name='Nine']").click();
        calcSession.findElementByXPath("//Button[@Name='Multiply by']").click();
        calcSession.findElementByXPath("//Button[@Name='Nine']").click();
        calcSession.findElementByXPath("//Button[@Name='Equals']").click();
        assertEquals("81", getCalculatorResultText());
    }

    @Test
    public void subtractionTest() {
        calcSession.findElementByXPath("//Button[@AutomationId=\"num9Button\"]").click();
        calcSession.findElementByXPath("//Button[@AutomationId=\"minusButton\"]").click();
        calcSession.findElementByXPath("//Button[@AutomationId=\"num1Button\"]").click();
        calcSession.findElementByXPath("//Button[@AutomationId=\"equalButton\"]").click();
        assertEquals("8", getCalculatorResultText());
    }

    @ParameterizedTest
    @CsvSource({"One,Plus,Seven,8", "Nine,Minus,One,8", "Eight,Divide by,Eight,1"})
    public void templatedTest(String input1, String operation, String input2, String expectedResult) {
        calcSession.findElementByName(input1).click();
        calcSession.findElementByName(operation).click();
        calcSession.findElementByName(input2).click();
        calcSession.findElementByName("Equals").click();
        assertEquals(expectedResult, getCalculatorResultText());
    }

    @Test
    public void showProperties() {
        WebElement element = calcSession.findElement(By.name("Equals"));
        System.out.println(element.getTagName());
        System.out.println(element.getAttribute("Name"));
        System.out.println(element.getAttribute("Id"));
        System.out.println(element.getAttribute("RuntimeId"));
        System.out.println(element.getAttribute("AutomationId"));
        System.out.println(element.getAttribute("Previous"));
        System.out.println(element.getAttribute("Ancestors"));
        System.out.println(element.getAttribute("FrameworkId"));
        System.out.println(element.getAttribute("ProviderDescription"));
        System.out.println(element.getAttribute("ControlType"));
        System.out.println(element.getAttribute("Table.ItemColumnHeaderItems"));
    }

    private String getCalculatorResultText()
    {
        return calculatorResult.getText().replace("Display is", "").trim();
    }
}
