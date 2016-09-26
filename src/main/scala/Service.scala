import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorRef

class Service(worker: ActorRef) {
  def submitTask(n: Int): Future[Either[String, Task]] = Future {
    val task = Task(0, n, "Processing")
    worker ! task
    Right(Repository.createTask(n))
  }

  def getTask(id: Int): Future[Either[String, Task]] = Future {
    Repository.findTask(id) match {
      case None => Left("No such task")
      case Some(t) => Right(t)
    }
  }

  def status: Future[Map[String, Int]] = Future {
    Repository.allTasks.groupBy(_.status).mapValues(_.length)
  }
}
