package steps.api;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class ApiSteps {
    private RequestSpecification spec;
    private Response response;

    @Dado("que a API base é {string}")
    public void baseUrl(String base) {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // tenta pegar a key de -Dreqres.key ou env REQRES_API_KEY; default do ReqRes é "reqres-free-v1"
        String apiKey = System.getProperty("reqres.key",
                System.getenv().getOrDefault("REQRES_API_KEY", "reqres-free-v1"));

        RequestSpecBuilder rsb = new RequestSpecBuilder()
                .setBaseUri(base)
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI);

        // se for reqres, adiciona o header exigido
        if (base.contains("reqres.in")) {
            rsb.addHeader("x-api-key", apiKey);
        }

        spec = rsb.build();
    }

    @Quando("eu faço GET em {string}")
    public void getEm(String path) {
        response = given().spec(spec)
                .when().get(path)
                .then().extract().response();
    }

    @Quando("eu faço POST em {string} com o corpo:")
    public void postEmComCorpo(String path, String body) {
        response = given().spec(spec).body(body)
                .when().post(path)
                .then().extract().response();
    }

    @Então("o status deve ser {int}")
    public void statusDeveSer(int status) {
        Assertions.assertEquals(status, response.statusCode(),
                "Status inesperado: " + response.asPrettyString());
    }

    @Então("o campo {string} deve ser {string}")
    public void campoDeveSer(String jsonPath, String esperado) {
        String valor = response.jsonPath().getString(jsonPath);
        Assertions.assertEquals(esperado, valor,
                "Campo diferente. Corpo: " + response.asPrettyString());
    }

    @Então("o campo {string} deve existir")
    public void campoDeveExistir(String jsonPath) {
        Object valor = response.jsonPath().get(jsonPath);
        Assertions.assertNotNull(valor, "Campo não encontrado: " + jsonPath + "\nCorpo: " + response.asPrettyString());
    }
}
