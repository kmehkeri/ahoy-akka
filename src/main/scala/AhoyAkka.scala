import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object AhoyAkka {
  def main(args: Array[String]) {
    val host = "localhost"
    val port = 8888

    implicit val actorSystem = ActorSystem("ahoy-system")
    implicit val actorMaterializer = ActorMaterializer()
    implicit val executionContext = actorSystem.dispatcher
    implicit val taskJsonFormat = jsonFormat2(Task)

    val route =
      get {
        pathSingleSlash {
          complete { Service.status }
        } ~
        path("task" / IntNumber) { id =>
          complete {
            Service.getTask(id) match {
              case Left(msg) => Map("error" -> msg)
              case Right(task) => task
            }
          }
        }
      } ~
      post {
        path("submit") {
          uploadedFile("file") {
            case (metadata, file) =>
              file.delete()
              complete {
                Task(1, "Processing")
              }
          }
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

