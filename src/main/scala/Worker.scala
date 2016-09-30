import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.Actor
import akka.actor.ActorLogging

class Worker extends Actor with ActorLogging {
  def receive = {
    case task: Task =>
      log.info(s"Received some stuff to do for task ${task.id} n=${task.n}")
      Future {
        Thread.sleep(5000)
      }.onComplete { t =>
        Repository.updateTask(task.id, "Completed")
        log.info(s"Yeah, okay, task ${task.id} done.")
      }
  }
}
