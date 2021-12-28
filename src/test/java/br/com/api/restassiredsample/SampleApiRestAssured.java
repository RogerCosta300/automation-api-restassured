package br.com.api.restassiredsample;

import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

public class SampleApiRestAssured {

    String idvotacao;

    @BeforeClass
    public static void url(){
        baseURI = "https://api.thecatapi.com/v1";
    }

//    @Test
//    public void fazerCadastro(){
//        given()
//                .contentType("application/json")
//                .body("{\"email\": \"rcosta.ba260@gmail.com\", \"appDescription\":\"Automation API RestAssured\"}")
//        .when()
//                .post("/user/passwordlesssignup")
//        .then()
//                .statusCode(200)
//                .log().all();
//    }

    @Test
    public void fazerCadastroCampoObrigatorio(){
        given()
                .contentType("application/json")
                .body("{\"appDescription\":\"Automation API RestAssured\"}")
        .when()
                .post("/user/passwordlesssignup")
        .then()
                .log().all()
                .body("message", containsString("\"email\" is required"))
                .statusCode(400);
    }

    @Test
    public void efetuarVotacao(){

        given()
            .contentType("application/json")
            .body("{\"image_id\": \"asf2\", \"sub_id\": \"my-user-1234\", \"value\": 1}")
            .headers("x-api-key","8d701aad-caab-4ab1-ba61-39a44f3ffe8c")
        .when()
            .post("/votes")
        .then()
            .statusCode(200)
            .body("message", is("SUCCESS"))
            .log().all();
    }

    @Test
    public void pegarVotacao(){
        Response response =
                (Response) given()
                    .headers("x-api-key","8d701aad-caab-4ab1-ba61-39a44f3ffe8c")
                .when()
                    .get("/votes");
                response.then()
                    .body("image_id", hasItems("asf2", "asf2"))
                    .statusCode(200)
                    .log().all();
         idvotacao = response.jsonPath().getString("id");

         System.out.println("VALOR DO ID -> " + idvotacao);
    }

    @Test
    public void deletarVotacao(){

        efetuarVotacao();
        pegarVotacao();

        System.out.println("VALOR DO ID ANTES DO DELETAR -> " + idvotacao.replace("[","").replace("]",""));

        given()
            .header("x-api-key","8d701aad-caab-4ab1-ba61-39a44f3ffe8c")
            .pathParam("vote_id", idvotacao.replace("[","").replace("]",""))
        .when()
            .delete("/votes/{vote_id}")
        .then()
            //.statusCode(200)
            .log().all();
    }
}
