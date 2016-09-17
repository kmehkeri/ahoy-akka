import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

object AhoyAkka {
  def main(args: Array[String]) {
    val host = "localhost"
    val port = 8888

    implicit val actorSystem = ActorSystem("ahoy-system")
    implicit val actorMaterializer = ActorMaterializer()
    implicit val executionContext = actorSystem.dispatcher

    val route =
      get {
        pathSingleSlash {
          complete { "Ahoy Akka!" }
        } ~
        path("task" / IntNumber) { taskId =>
          complete { s"Ahoy! Task ${taskId} reporting." }
        }
      } ~
      post {
        path("submit") {
          complete { "Ahoy! Submitting task..." }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, host, port)

    bindingFuture.map { b =>
      println(s"Server started at ${host}:${port}\nPress ENTER to stop...")
      readLine()
      b
    }.flatMap(_.unbind()).onComplete { _ =>
      println("Terminating...")
      actorSystem.terminate()
    }
  }
}

