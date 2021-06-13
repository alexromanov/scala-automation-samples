# scala-automation-samples

Collection of tests, written in Scala. Built by sbt. 

The list includes:
1. [Sending / receiving email to Google Gmail](/src/test/scala/email/MailApiTest.scala)
2. [UI end-to-end tests with Selenium WebDriver and scalatest-selenium](/src/test/scala/ui/BlogTest.scala)
3. [gRPC API tests with akka-grpc](/src/test/scala/example/myapp/helloworld/GreeterServiceApiTest.scala) (sample gRPC server is [here](https://github.com/alexromanov/server-grpc-sample))
    

Generate gRPC clients:
```console
$ sbt compile
```

Execute tests:
```console
$ sbt test
```
