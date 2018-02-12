package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContextExecutor, Future}

trait RouteExceptionHandler {
  val routeExceptionHandler = ExceptionHandler {
    case _: ArithmeticException => complete {
      BadRequest -> "Divide by zero!"
    }
  }
}


object WebServer extends App with RouteExceptionHandler {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)
  val ip = config.getString("http.ip")
  val port = config.getInt("http.port")

  val routes = handleExceptions(routeExceptionHandler) {
    path("") {
      getFromResource("web/index.html")
    } ~
      path("hallo") {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"Test!"))
      } ~
      path("divide") {
        parameters('a.as[Int], 'b.as[Int]) { (a, b) =>
          complete {
            val result = a / b
            s"Result is $result"
          }
        }
      }
  }

  println(s"Starting server at $ip:$port")
  val binder: Future[Http.ServerBinding] = Http(system)
    .bindAndHandle(routes, ip, port)
}