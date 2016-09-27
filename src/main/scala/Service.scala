import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.ActorRef

class Service(worker: ActorRef) {
  def submitTask(n: Int): Future[Either[String, Task]] = Future {
    val task = Repository.createTask(n)
    worker ! task
    Right(task)
  }

  def getTask(id: Int): Future[Either[String, Task]] = Future {
    Repository.findTask(id) match {
      case None => Left("No such task")
      case Some(t) => Right(t)
    }
  }

  def status: Future[List[Task]] = Future {
    Repository.allTasks
  }
}
