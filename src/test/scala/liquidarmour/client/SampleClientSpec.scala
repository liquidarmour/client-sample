package liquidarmour.client

import org.scalatest.{ AsyncWordSpec, Matchers }
import com.github.tomakehurst.wiremock.client.WireMock._

import scala.util.Success

class SampleClientSpec extends AsyncWordSpec with Matchers with WireMockFixture {

  "client" should {
    "call and pass 200" in {
      start()
      stubFor(get(urlEqualTo("/")).willReturn(aResponse.withHeader("Content-Type", "text/plain").withBody("Hello world!")))
      val client = new SampleClient("http://localhost", 8080)
      client.call().map { s =>
        s shouldBe "Got response, body: Hello world!"
      }.andThen {
        case Success(s) =>
          stop
      }
    }
    "call and pass 202" in {
      start()
      stubFor(get(urlEqualTo("/")).willReturn(aResponse.withHeader("Content-Type", "text/plain").withBody("Hello world!").withStatus(202)))
      val client = new SampleClient("http://localhost", 8080)
      client.call().map { s =>
        s shouldBe "Got response, body: Hello world!"
      }.andThen {
        case Success(s) =>
          stop
      }
    }
    "call and get 404" in {
      start()
      stubFor(get(urlEqualTo("/")).willReturn(aResponse.withHeader("Content-Type", "text/plain").withStatus(404)))
      val client = new SampleClient("http://localhost", 8080)
      client.call().map { s =>
        s shouldBe "Request failed, response code: 404 Not Found"
      }
    }
    "call and get 500" in {
      start()
      stubFor(get(urlEqualTo("/")).willReturn(aResponse.withHeader("Content-Type", "text/plain").withStatus(500)))
      val client = new SampleClient("http://localhost", 8080)
      client.call().map { s =>
        s shouldBe "Request failed, response code: 500 Internal Server Error"
      }
    }
  }
}
