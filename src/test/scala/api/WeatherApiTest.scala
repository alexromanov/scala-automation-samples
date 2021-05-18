package api

import akka.grpc.internal.GrpcResponseHelpers.status
import akka.http.Version.check
import akka.http.javadsl.server.Directives.route
import akka.http.scaladsl.client.RequestBuilding.{Get, WithTransformation}
import akka.http.scaladsl.model.StatusCodes.OK
import email.BaseApiTest
import io.restassured.RestAssured.when
import org.hamcrest.CoreMatchers.equalTo

class WeatherApiTest extends BaseApiTest {
  val appId = "9c6b51ba904581395808431da8d6988d"
  val host = "http://api.openweathermap.org/data/2.5/weather"
  val city = "Dnipro"
  val cityId = 709930
  case class WeatherData(name: String, id: Int, timezone: Int, cod: Int)

  "Client" should "be able to get weather data by city name" in {
    val response = when().get(s"$host?q=$city&appid=$appId")
      .`then`().statusCode(200)
      .body(
        "coord.lon", equalTo(34.9833f),
        "coord.lat", equalTo(48.45f),
        "base", equalTo("stations"),
        "name", equalTo(city),
        "id", equalTo(cityId)
      ).extract().response().as(WeatherData.getClass)
    println(response)
  }

  "Client" should "be able to get weather data by city id" in {
    val response = when get s"$host?id=$cityId&appid=$appId"
    response.statusCode should be(200)
    response.jsonPath().get[WeatherData]()

  }
}
