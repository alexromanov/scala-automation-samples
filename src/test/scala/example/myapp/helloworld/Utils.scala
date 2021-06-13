package example.myapp.helloworld

import scala.util.Random

object Utils {
  def getRandomValue: String = Random.alphanumeric.filter(_.isLetter).take(5).mkString

  def getRandomList(max: Int): List[String] = (1 to max).map(_ => getRandomValue).toList
}
