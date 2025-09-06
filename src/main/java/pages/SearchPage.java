package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private final WebDriver driver;

    private final By resultsGrid  = By.cssSelector("div.s-main-slot");
    private final By productLinks = By.cssSelector("div.s-main-slot a[href*='/dp/']:not([role='button'])");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        // garante que estamos na página de resultados
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(resultsGrid));
    }


    // Clica no 1º produto "real" (link que leva para /dp/)
    public void clickFirstProduct() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // rola um pouco para forçar lazy-load inicial
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 600);");

        // espera haver pelo menos um link /dp/
        wait.until(d -> {
            List<WebElement> links = d.findElements(productLinks);
            return links != null && !links.isEmpty();
        });

        List<WebElement> links = driver.findElements(productLinks);

        // filtra visíveis e com algum texto/aria-label
        WebElement first = links.stream()
                .filter(WebElement::isDisplayed)
                .filter(e -> {
                    String t = e.getText();
                    if (t != null && !t.isBlank()) return true;
                    String aria = e.getAttribute("aria-label");
                    return aria != null && !aria.isBlank();
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nenhum resultado visível (/dp/)"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", first);
        wait.until(ExpectedConditions.elementToBeClickable(first));
        try {
            first.click();
        } catch (ElementClickInterceptedException ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", first);
        }

        // se abrir em nova aba/janela, focar
        for (String h : driver.getWindowHandles()) driver.switchTo().window(h);
    }
}
