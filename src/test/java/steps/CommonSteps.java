package steps;

import hooks.hooks;
import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;
import pages.HomePage;
import pages.SearchPage;

public class CommonSteps {
    HomePage home;
    SearchPage search;

    @Dado("que estou na home da Amazon")
    public void abrirHome() {
        home = new HomePage(hooks.driver);
        home.open();
    }

    @Quando("eu digito {string} na busca")
    public void digitarNaBusca(String termo) {
        home.typeSearch(termo);

    }

    @Quando("eu digito {string} na busca para buscar")
    public void digitarNaBuscaParaBsucar(String termo) {
        search = home.searchFor(termo);
    }

    @Então("devo ver sugestões de autocomplete")
    public void validarSugestoes() {
        Assertions.assertFalse(home.getSuggestions().isEmpty(), "Nenhuma sugestão exibida!");
    }

    @Quando("eu abro o menu")
    public void abrirMenu() {
        home.openMenuAndWait();
    }

    @Então("o menu deve estar visível")
    public void menuVisivel() {
        Assertions.assertTrue(home.getSideMenuElement().isDisplayed(), "Menu não visível!");
    }



    @Quando("eu seleciono o primeiro resultado")
    public void selecionarPrimeiroResultado() {
        search.clickFirstProduct();
    }

    @Então("a página de detalhes deve ser exibida")
    public void validarDetailPage() {
        Assertions.assertTrue(home.isDetailPage(), "Página de detalhe não exibida!");
    }
}
