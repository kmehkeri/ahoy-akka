import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import scala.concurrent.Future

object AhoyAkka extends App with Config {
  implicit val actorSystem = ActorSystem("ahoy-system")
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  implicit val taskJsonFormat = jsonFormat3(Task.apply)

  val route =
    get {
      pathSingleSlash {
        val statusF = Service.status
        onSuccess(statusF) {
          case status => complete(status)
        }
      } ~
      path("task" / IntNumber) { id =>
        val taskF = Service.getTask(id)
        onSuccess(taskF) {
          case Left(msg) => complete(Map("error" -> msg))
          case Right(task) => complete(task)
        }
      }
    } ~
    post {
      path("submit") {
        fileUpload("file") {
          case (metadata, byteStream) =>
            val taskF = byteStream.runFold(0)((acc, str) => acc + str.length).flatMap(n => Service.submitTask(n))
            onSuccess(taskF) { task =>
              complete {
                StatusCodes.Accepted -> task
              }
            }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, httpHost, httpPort)

  bindingFuture.map { b =>
    println(s"Server started at ${httpHost}:${httpPort}\nPress ENTER to stop...")
    readLine()
    b
  }.flatMap(_.unbind()).onComplete { _ =>
    println("Terminating...")
    actorSystem.terminate()
  }
}

