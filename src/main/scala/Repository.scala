import scala.collection.mutable.ArrayBuffer

case class Task(id: Int, n: Int, status: String)

object Repository {
  private val tasks: ArrayBuffer[Task] = ArrayBuffer()

  def createTask(n: Int): Task = {
    val task = Task(tasks.size + 1, n, "Processing")
    tasks.append(task)
    task
  }

  def updateTask(id: Int, status: String): Unit = {
    val i = tasks.indexWhere(_.id == id)
    tasks(i) = Task(id, tasks(i).n, status)
  }

  def findTask(id: Int): Option[Task] = {
    tasks.find(_.id == id)
  }

  def allTasks: List[Task] = {
    tasks.toList
  }
}
