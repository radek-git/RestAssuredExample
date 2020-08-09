import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.path.json.JsonPath.from;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExampleTest {

    @Test
    public void checkErrorAndResponse() {
        get("https://sv443.net/jokeapi/v2/joke/Any")
                .then()
                .body("error", is(false))
                .statusCode(200);
    }

    @Test
    public void checkStatusAndItems() {
        get("https://sv443.net/jokeapi/v2/joke/Any?type=single&amount=2")
                .then()
                .body("jokes.type", hasItems("single")) // contains
                .statusCode(200);
    }

    @Test
    public void checkNumberOfResults() {
        List<String> getResults = Arrays.asList(get("http://dummy.restapiexample.com/api/v1/employees").toString());
        System.out.println(getResults.size());
        assertThat(getResults.size(), is(1));

    }

    @Test
    public void checkSalary() {
        get("http://dummy.restapiexample.com/api/v1/employees")
                .then()
                .body("data[0].employee_salary", is("320800"))
                .statusCode(200);
    }

    @Test
    public void checkNumber() {
        get("https://www.metaweather.com/api/location/search/?query=warsaw")
                .then()
                .body("[0].woeid", is(523920));
    }

    @Test
    public void checkFloat() {
        get("https://www.metaweather.com/api/location/search/?query=warsaw")
                .then()
                .body("[0].woeid", is(not(523920f)));
    }

    @Test
    public void checkBigDecimal() {
        given().config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                when()
                .get("https://www.metaweather.com/api/location/search/?query=warsaw")
                .then()
                .body("[0].woeid", is(not(BigDecimal.valueOf(523920))));
    }

    @Test
    public void checkJson() {
        get("http://dummy.restapiexample.com/api/v1/employees")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("file.json"));
    }

//    @Test
//    public void checkItems() {
//        when().get("https://qrng.anu.edu.au/API/jsonI.php?length=5&type=uint16")
//                .then()
//                .body("data", hasItems(6493, 9990, 44054, 3254, 15620));
//    }
//
//    @Test
//    public void checkXML() {
//        when()
//                .get("http://www.geoplugin.net/xml.gp?ip=xx.xx.xx.xx")
//                .then()
//                .body("geoPlugin.geoplugin_request", equalTo("89.64.71.175"));
//    }
//
//    @Test
//    public void checkXmlWithExtractedResponse() {
//        Response response = when()
//                .get("http://www.geoplugin.net/xml.gp?ip=xx.xx.xx.xx")
//                .then()
//                .body("geoPlugin.geoplugin_request", equalTo("89.64.71.175"))
//                .extract().response();
//
//        Assert.assertEquals(response.statusCode(), 200);
//    }

    @Test
    public void checkParsedJson() {
        String json = "[1, 2, 3]";
        List<Object> list = from(json).getList("$");
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
    }

    @Test
    public void checkParsedJsonWithJsonPath() {
        String json = "[1, 2, 3]";

        JsonPath jsonPath = new JsonPath(json);
        assertTrue(jsonPath.getList("").contains(1));
    }

    @Test
    public void getSingleHeader() {
        Response response = get("http://dummy.restapiexample.com/api/v1/employees");
        Headers headers = response.getHeaders();

        assertEquals(headers.get("X-Sol").getValue(), "pub_site");
    }

    @Test
    public void getHeaderWithMultipleValues() {
        Response response = get("http://dummy.restapiexample.com/api/v1/employees");
        Headers headers = response.getHeaders();

        List<String> values = Arrays.asList(headers.getValues("X-Ezoic-Cdn").get(0).split(";"));
        assertEquals(values.get(0), "Hit ds");
    }

    @Test
    public void emptyTest() {

    }

}
