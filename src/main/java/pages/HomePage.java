package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    //Mapeando os elementos
    private final By searchBox      = By.id("twotabsearchtextbox");
    private final By suggestionsSel = By.cssSelector("div.s-suggestion-container, div.s-suggestion");
    private final By acceptCookies  = By.id("sp-cc-accept");
    private final By hamburgerBtn       = By.id("nav-hamburger-menu");
    private final By sideMenuContainer  = By.id("hmenu-container");
    private final By sideMenuContent    = By.id("hmenu-content");
    private final By btnContinuarComprando = By.xpath(
            "//input[@type='submit' and (contains(@value,'Continuar') or contains(@value,'Continue'))]" +
                    "|//button[contains(.,'Continuar comprando') or contains(.,'Continue shopping')]"
    );
    private final By resultItems = By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']");



    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://www.amazon.com.br/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // clicar no botao ir para home
        driver.findElements(btnContinuarComprando)
                .stream().findFirst()
                .ifPresent(WebElement::click);

        // tenta aceitar cookies se aparecer
        try {
            wait.withTimeout(Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(acceptCookies))
                    .click();
        } catch (TimeoutException | NoSuchElementException ignored) {
        } finally {
            // volta para 15s pro restante
            wait.withTimeout(Duration.ofSeconds(15));
        }

        // garante que a caixa de busca está visível e clicável
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
    }

    public void typeSearch(String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        input.clear();
        input.sendKeys(text);
    }

    public List<WebElement> getSuggestions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // espera até haver ao menos 1 sugestão
        wait.until((ExpectedCondition<Boolean>) d ->
                d != null && !d.findElements(suggestionsSel).isEmpty()
        );
        return driver.findElements(suggestionsSel);
    }

    public void openMenuAndWait() {
        driver.findElement(hamburgerBtn).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeContains(hamburgerBtn, "aria-expanded", "true"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sideMenuContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sideMenuContent));
    }

    public WebElement getSideMenuElement() {
        return driver.findElement(sideMenuContainer);
    }

    // submete a busca (ENTER) e espera a página de resultados
    public void submitSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        input.sendKeys(Keys.ENTER);

        // URL muda para /s?k=...
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/s?k="),
                ExpectedConditions.presenceOfElementLocated(resultItems)
        ));
    }

    // valida PDP
    public boolean isDetailPage() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle"))).isDisplayed();
        } catch (TimeoutException e) { return false; }
    }

    public SearchPage searchFor(String term) {
        typeSearch(term);
        submitSearch();
        return new SearchPage(driver);  // <-- devolve a page de resultados
    }
}
