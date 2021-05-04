package email

import com.sun.mail.imap.IdleManager

import java.nio.file.Paths
import java.util.Properties
import javax.activation.{DataHandler, FileDataSource}
import javax.mail._
import javax.mail.event.{MessageCountAdapter, MessageCountEvent}
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.io.Source


class MailApiTest extends BaseApiTest {
  val messageSubject = "DEBUG MESSAGE"
  val folderName = "Inbox"
  val to: String = config.getString("recipient")
  val from: String = config.getString("sender")
  val props: Properties = getProperties

  "Client" should "be able to send email message" in {
    val session: Session = getSession(props)

    sendEmail(session, from, to, messageSubject, "files/test.txt",
      "files/test_attachment.txt")
  }

  "Client" should "be able to receive email message" in {
    val session: Session = getSession(props)

    val folder: Folder = openFolderInMailBox(session, folderName)

    val message = receiveEmail(session, folder, messageSubject)

    message.getSubject should be (messageSubject)
  }

  private def sendEmail(session: Session, from: String, to: String, subject: String, messagePath: String,
                        attachment: String): Unit = {
    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(from))
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
    message.setSubject(subject)

    val textPart = new MimeBodyPart
    textPart.setText(Source.fromResource(messagePath).mkString)

    val filePart = new MimeBodyPart

    val res = getClass.getClassLoader.getResource(attachment)
    val file = Paths.get(res.toURI).toFile

    val fds = new FileDataSource(file.getAbsolutePath)
    filePart.setDataHandler(new DataHandler(fds))
    filePart.setFileName(fds.getName)

    val multipart = new MimeMultipart
    multipart.addBodyPart(textPart)
    multipart.addBodyPart(filePart)

    message.setContent(multipart)

    Transport.send(message)
    log.info(s"Message <$subject> is sent to $to")
  }

  private def receiveEmail(session: Session, folder: Folder, subject: String): Message = {
    val manager = getIdleManager(session)
    val event = waitForFirst(awaitForNewMessages(folder, manager))(_
      .getMessages.toList.head.getSubject.contains(subject)).futureValue

    val message = event.getMessages.toList.head
    log.info(s"Message <${message.getSubject}> is received")
    message
  }

  private def getIdleManager(session: Session) = {
    new IdleManager(session, java.util.concurrent.Executors.newSingleThreadExecutor())
  }

  private def openFolderInMailBox(session: Session, folderName: String) = {
    val store = session.getStore("imaps")
    store.connect("imap.gmail.com", from,
      config.getString("password"))
    val folder = store.getFolder(folderName)
    folder.open(Folder.READ_WRITE)
    log.info(s"$folderName folder is opened for $from")
    folder
  }

  private def awaitForNewMessages(folderName: Folder, idleManager: IdleManager): Future[MessageCountEvent] = {
    val promise = Promise[MessageCountEvent]
    folderName.addMessageCountListener(new MessageCountAdapter {
      override def messagesAdded(e: MessageCountEvent): Unit = {
        promise.trySuccess(e)
      }
    })
    idleManager.watch(folderName)
    promise.future
  }

  private def waitForFirst[A](futureA: => Future[A])(predicate: A => Boolean): Future[A] = {
    futureA
      .filter(predicate)
      .recoverWith { case _: NoSuchElementException => waitForFirst(futureA)(predicate) }
  }

  private def getSession(properties: Properties) = {
    val session: Session = Session.getInstance(properties, new Authenticator() {
      override protected def getPasswordAuthentication: PasswordAuthentication = {
        new PasswordAuthentication(config.getString("username"), config.getString("password"))
      }
    })
    session.setDebug(false)
    session
  }

  private def getProperties = {
    val properties = System.getProperties
    properties.put("mail.smtp.host", "smtp.gmail.com")
    properties.put("mail.smtp.port", "465")
    properties.put("mail.smtp.auth", "true")
    properties.put("mail.smtp.ssl.enable", "true")
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    properties.put("mail.store.protocol", "imaps")
    properties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    properties.put("mail.imap.socketFactory.fallback", "false")
    properties.put("mail.imaps.usesocketchannels", "true")
    properties
  }
}
