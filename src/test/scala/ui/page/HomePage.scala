package ui.page

import org.openqa.selenium.chrome.ChromeDriver
import org.scalatestplus.selenium.Page

class HomePage(implicit val driver: ChromeDriver) extends Page {
  val url = "https://alexromanov.github.io/"

  val homePageLink: String = "//a[@href=\"/\"]"
  val aboutPageLink: String = "//a[@href=\"/about/\"]"
  val contactPageLink: String = "//a[@href=\"/contact/\"]"
}
