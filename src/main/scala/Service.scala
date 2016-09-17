object Service {
  def submitTask(task: Task) = {
  }

  def getTask(id: Int) = {
    Repository.findTask(id)
  }

  def status: Map[String, Int] = {
    Repository.allTasks.groupBy(_.status).mapValues(_.length)
  }
}
