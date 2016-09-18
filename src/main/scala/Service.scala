import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Service {
  def submitTask(task: Task) = {
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
