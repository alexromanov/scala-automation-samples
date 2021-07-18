package ui

import io.github.bonigarcia.wdm.WebDriverManager
import io.qameta.allure.scalatest.AllureScalatestContext
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.concurrent.Eventually
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.selenium.WebBrowser
import tags.Smoke
import ui.page.{AboutPage, ContactPage, HomePage}

import scala.language.postfixOps

class BlogTest extends AnyFlatSpec with BeforeAndAfterAll with Matchers with Eventually with WebBrowser {

  implicit val webDriver: ChromeDriver = {
    WebDriverManager.chromedriver().setup()
    val options = new ChromeOptions
    options.addArguments("--no-sandbox")
    options.addArguments("--disable-dev-shm-usage")
    options.addArguments("--headless")
    new ChromeDriver(options)
  }

  override def afterAll(): Unit = {
    webDriver.quit()
  }

  implicitlyWait(Span(3, Seconds))

  "User" should "be able to view latest blog post" taggedAs Smoke in new AllureScalatestContext {
    val homePage = new HomePage
    go to homePage
    val posts = findAll(CssSelectorQuery("div.post-preview")).toList
    posts should have size 5
    val latestPost = posts.head
    latestPost.text should not be empty
  }

  "User" should "be able to open Contact Page" taggedAs Smoke in new AllureScalatestContext {
    val homePage = new HomePage
    go to homePage
    homePage.url should equal (currentUrl)
    click on xpath(homePage.contactPageLink)
    eventually {
      currentUrl != homePage.url
    }
    val contactPage = new ContactPage
    contactPage.url should equal (currentUrl)
  }

  "User" should "be able to send message to author" taggedAs Smoke in new AllureScalatestContext {
    val contactPage = new ContactPage
    go to contactPage
    contactPage.url should be (currentUrl)
    textField(contactPage.nameField).value = "Reader"
    click on id(contactPage.emailField)
    enter("reader@mail.com")
    click on id(contactPage.phoneField)
    enter( "123456789")
    textArea(contactPage.messageField).value = "Hello Author"
    click on cssSelector(contactPage.sendButton)
  }

  "User" should "be able to open About Page" taggedAs Smoke in new AllureScalatestContext {
    val homePage = new HomePage
    go to homePage
    homePage.url should equal (currentUrl)
    click on xpath(homePage.aboutPageLink)
    eventually {
      currentUrl != homePage.url
    }
    val aboutPage = new AboutPage
    aboutPage.url should be (currentUrl)
  }
}
