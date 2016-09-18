object Service {
  def submitTask(task: Task) = {
  }

  def getTask(id: Int): Either[String, Task] = {
    Repository.findTask(id) match {
      case None => Left("No such task")
      case Some(t) => Right(t)
    }
  }

  def status: Map[String, Int] = {
    Repository.allTasks.groupBy(_.status).mapValues(_.length)
  }
}
