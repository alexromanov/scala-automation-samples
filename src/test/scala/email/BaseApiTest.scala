package email

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}

class BaseApiTest extends AnyFlatSpec with Matchers with ScalaFutures with Configure with Logging {
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(3, Minutes), interval = Span(10, Millis))
}

trait Configure {
  implicit lazy val config: Config = ConfigFactory.load()

  def config(name: String): Config = config.getConfig(name)
}

trait Logging {
  lazy val log: Logger = Logger(this.getClass)
}

object LogFactory extends Logging {

  def getLogger(category: String): Logger = {
    Logger(category)
  }
}
