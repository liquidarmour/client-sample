package liquidarmour.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

trait WireMockFixture {

  import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
  val wireMockServer: WireMockServer = new WireMockServer(wireMockConfig().port(8080))

  def start() = wireMockServer.start()

  def reset() = WireMock.reset()

  def stop() = wireMockServer.stop
}
