package ui.page

import org.openqa.selenium.chrome.ChromeDriver
import org.scalatestplus.selenium.Page

class ContactPage(implicit val driver: ChromeDriver) extends Page  {
  override val url: String = "https://alexromanov.github.io/contact/"

  val nameField = "name"
  val emailField = "email"
  val phoneField = "phone"
  val messageField = "message"
  val sendButton = "button.btn"
}
