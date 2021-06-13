package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.{Sink, Source}
import example.myapp.example.myapp.helloworld.grpc.{GreeterService, GreeterServiceClient, HelloRequest}
import example.myapp.helloworld.Utils.{getRandomList, getRandomValue}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}

class GreeterServiceApiTest extends AnyFlatSpec with Matchers with ScalaFutures {
  val host = "127.0.0.1"
  val port = 8080

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(3, Minutes), interval = Span(10, Millis))

  implicit protected val system: ActorSystem = ActorSystem("api-test")

  val clientSettings: GrpcClientSettings = GrpcClientSettings.connectToServiceAt(host, port)
    .withTls(false)

  val client: GreeterService = GreeterServiceClient(clientSettings)

  "Greeter Service" should "handle unary requests and response" in {
    val name = getRandomValue
    val response = client.sayHello(HelloRequest(name)).futureValue

    response.getTimestamp should not be null
    response.message should be(s"Hello, $name")
  }

  "Greeter Service" should "handle client streaming" in {
    val data = getRandomList(3)
    val response = client.itKeepsTalking(Source(data.map(HelloRequest(_)))).futureValue

    response.timestamp should not be null
    response.message should be(s"Hello, ${data.mkString(", ")}")
  }

  "Greeter Service" should "handle server streaming" in {
    val name = getRandomValue
    val responses = client.itKeepsReplying(HelloRequest(name)).runWith(Sink.seq).futureValue

    responses should not be empty
    val messages = responses.map(_.message).toList
    all(messages) should not be ""
  }

  "Greeter Service" should "handle bi-directional streaming" in {
    val data = getRandomList(3)
    val responses = client.streamHellos(Source(data.map(HelloRequest(_)))).runWith(Sink.seq).futureValue

    responses should not be empty
    val messages = responses.map(_.message).toList
    all(messages) should not be ""
  }
}