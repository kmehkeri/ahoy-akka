case class Task(id: Int, status: String)

object Repository {
  def createTask(task: Task): Unit = {
  }

  def updateTask(task: Task): Unit = {
  }

  def findTask(id: Int): Option[Task] = {
    Some(Task(id, "Completed"))
  }

  def allTasks: List[Task] = {
    List(Task(1, "Running"), Task(2, "Broken"), Task(3, "Completed"), Task(4, "Completed"))
  }
}
