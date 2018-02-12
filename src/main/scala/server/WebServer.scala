package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import java.nio.file.{Files, Path, Paths}

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContextExecutor, Future}

object routes {

  val workingDirectory: String = System.getProperty("user.dir")


  private def getDefaultPage: Route = {
    getFromResource("web/index.html")
  }

  def generate: Route = {
    path("") {
      getDefaultPage
    }
  }
}


object WebServer extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  val binder: Future[Http.ServerBinding] = Http(system)
    .bindAndHandle(routes.generate, config.getString("http.ip"), config.getInt("http.port"))
}