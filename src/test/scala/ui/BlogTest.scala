package ui

import org.openqa.selenium.chrome.ChromeDriver
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.concurrent.Eventually
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.selenium.WebBrowser
import ui.page.{AboutPage, ContactPage, HomePage}

import scala.language.postfixOps

class BlogTest extends AnyFlatSpec with BeforeAndAfterAll with Matchers with Eventually with WebBrowser {

  implicit val webDriver: ChromeDriver = {
    System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver_win32\\chromedriver.exe")
    new ChromeDriver()
  }

  "User" should "be able to view latest blog post" in {
    val homePage = new HomePage
    go to homePage
    val posts = findAll(CssSelectorQuery("div.post-preview")).toList
    posts should have size 5
    val latestPost = posts.head
    latestPost.text should not be empty
  }

  "User" should "be able to open Contact Page" in {
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

  "User" should "be able to send message to author" in {
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

  "User" should "be able to open About Page" in {
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

  override def afterAll(): Unit = {
    quit()
  }
}
