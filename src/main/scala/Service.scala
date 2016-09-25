import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Service {
  def submitTask(n: Int): Future[Either[String, Task]] = Future {
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
