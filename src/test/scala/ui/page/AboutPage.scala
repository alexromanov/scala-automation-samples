package ui.page

import org.openqa.selenium.chrome.ChromeDriver
import org.scalatestplus.selenium.Page

class AboutPage(implicit val driver: ChromeDriver) extends Page {
  override val url: String = "https://alexromanov.github.io/about/"
}
