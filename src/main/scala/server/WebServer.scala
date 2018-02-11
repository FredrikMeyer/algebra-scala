package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object WebServer {

  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("system")
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

    val route =
      pathSingleSlash {
        get {
          complete {
            "Her kommer det matematikk :)"
          }
        }
      }

    val config = ConfigFactory.load()
    val port = config.getInt("http.port")

    Http().bindAndHandle(route, "localhost", port)

    println(s"Server started at localhost:$port")

  }


}