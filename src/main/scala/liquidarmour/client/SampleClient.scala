package liquidarmour.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse, StatusCodes }
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{ Failure, Success }

class SampleClient(val host: String, port: Int = 443) {

  def call(): Future[String] = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$host:$port"))

    responseFuture.flatMap {
      case HttpResponse(statusCode, headers, entity, _) if statusCode.isSuccess() =>
        entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
          "Got response, body: " + body.utf8String
        }
      case resp @ HttpResponse(code, _, _, _) =>
        resp.discardEntityBytes()
        Future("Request failed, response code: " + code)
    }
  }
}
