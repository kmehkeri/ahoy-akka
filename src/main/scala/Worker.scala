import akka.actor.Actor

class Worker extends Actor {
  def receive = {
    case task: Task =>
      println(s"Received some stuff to do for task ${task.id} n=${task.n}")
      Thread.sleep(5000)
      println("Yeah, okay")
  }
}
