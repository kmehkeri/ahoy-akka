case class Task(id: Int, n: Int, status: String)

object Repository {
  def createTask(task: Task): Task = {
    Task(666, task.n, task.status)
  }

  def updateTask(task: Task): Unit = {
  }

  def findTask(id: Int): Option[Task] = {
    Some(Task(id, 100, "Completed"))
  }

  def allTasks: List[Task] = {
    List(Task(1, 123, "Running"), Task(2, 234, "Broken"), Task(3, 999, "Completed"), Task(4, 6285, "Completed"))
  }
}
