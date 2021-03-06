import scala.concurrent.Future
import scala.util.{Success,Failure}

import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object AhoyAkka extends App with Config {
  implicit val actorSystem = ActorSystem("ahoy-system")
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  implicit val taskJsonFormat = jsonFormat3(Task.apply)

  val log = Logging.getLogger(actorSystem, this)
  val worker = actorSystem.actorOf(Props[Worker], "worker")
  val service = new Service(worker)

  val route =
    get {
      pathSingleSlash {
        val statusF = service.status
        onSuccess(statusF) {
          case status => complete(status)
        }
      } ~
      path("task" / IntNumber) { id =>
        val taskF = service.getTask(id)
        onSuccess(taskF) {
          case Left(msg) => complete(StatusCodes.NotFound -> Map("error" -> msg))
          case Right(task) => complete(task)
        }
      }
    } ~
    post {
      path("submit") {
        fileUpload("file") {
          case (metadata, byteStream) =>
            val taskF = byteStream.runFold(0)((acc, str) => acc + str.length).flatMap(n => service.submitTask(n))
            onSuccess(taskF) { task =>
              complete {
                StatusCodes.Accepted -> task
              }
            }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, httpHost, httpPort)

  bindingFuture.onComplete {
    case Success(b) => {
      log.info(s"Server started at ${httpHost}:${httpPort}")

      // If readLine returns end-of-stream it means we are (most probably) in non-interactive mode,
      // so don't do anything and wait to be killed
      if (readLine() != null) {
        // Otherwise terminate, we're inside sbt maybe and user pressed ENTER
        log.info("Terminating")
        b.unbind().onComplete { _ =>
          actorSystem.terminate()
        }
      }
    }
    case Failure(e) => {
      println("Aborting")
      actorSystem.terminate()
    }
  }
}

