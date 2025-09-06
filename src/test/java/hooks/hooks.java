package hooks;

import io.cucumber.java.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class hooks {
    public static WebDriver driver;

    //comentario de teste para commit pelo intelliJ
    @Before(order = 0)
    public void startDriver(Scenario scenario) {
        if (!scenario.getSourceTagNames().contains("@ui")) return;
        WebDriverManager.chromedriver().setup(); // setando o driver
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless","false"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox","--disable-dev-shm-usage","--window-size=1366,900");
        driver = new ChromeDriver(options);
    }

    // Screenshot após cada passo (aparece no Extent)
    @AfterStep
    public void afterStep(Scenario scenario) {
        // só tira screenshot se existe driver (cenários @ui)
        if (driver == null) return;
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(bytes, "image/png", "step");
        } catch (Exception ignored) {}
    }

    @After(order = 1)
    public void attachOnFail(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(bytes, "image/png", "screenshot");
                // salva também em disco (opcional)
                Path dir = Paths.get("reports","screenshots");
                Files.createDirectories(dir);
                String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                Path file = dir.resolve(scenario.getName().replaceAll("\\s+","_") + "_" + ts + ".png");
                Files.write(file, bytes);
            } catch (Exception ignored) {}
        }
    }

    @After(order = 0)
    public void quit(Scenario scenario) {
        if (driver != null) driver.quit();
    }
}

