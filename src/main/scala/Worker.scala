import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.Actor

class Worker extends Actor {
  def receive = {
    case task: Task =>
      println(s"Received some stuff to do for task ${task.id} n=${task.n}")
      Future {
        Thread.sleep(5000)
      }.onComplete { t =>
        Repository.updateTask(task.id, "Completed")
        println(s"Yeah, okay, task ${task.id} done.")
      }
  }
}
