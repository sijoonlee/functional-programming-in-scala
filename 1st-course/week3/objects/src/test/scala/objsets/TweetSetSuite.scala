package objsets

import org.junit._
import org.junit.Assert.assertEquals
import objsets.TweetReader

class TweetSetSuite {
  trait TestSets {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 20))
    val c = new Tweet("c", "c body", 7)
    val d = new Tweet("d", "d body", 9)
    val set4c = set3.incl(c) // empty, a, b, c
    val set4d = set3.incl(d) // empty, a, b, d
    val set5 = set4c.incl(d) // empty, a, b, c, d
  }

  def asSet(tweets: TweetSet): Set[Tweet] = {
    var res = Set[Tweet]()
    tweets.foreach(res += _)
    res
  }

  def size(set: TweetSet): Int = asSet(set).size

  @Test def `filter: on empty set`: Unit =
    new TestSets {
      assertEquals(0, size(set1.filter(tw => tw.user == "a")))
    }

  @Test def `filter: a on set5`: Unit =
    new TestSets {
      assertEquals(1, size(set5.filter(tw => tw.user == "a")))
    }

  @Test def `filter: twenty on set5`: Unit =
    new TestSets {
      assertEquals(2, size(set5.filter(tw => tw.retweets == 20)))
    }

  @Test def `union: set4c and set4d`: Unit =
    new TestSets {
      assertEquals(4, size(set4c.union(set4d)))
    }

  @Test def `union: with empty set1`: Unit =
    new TestSets {
      assertEquals(4, size(set5.union(set1)))
    }

  @Test def `union: with empty set2`: Unit =
    new TestSets {
      assertEquals(4, size(set1.union(set5)))
    }

  @Test def `descending: set5`: Unit =
    new TestSets {
      val trends = set5.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a" || trends.head.user == "b")
    }

  def printTweet(l: List[Tweet]): List[Nothing] = l match {
    case head::tail => { println(head.retweets); printTweet(tail) }
    case _ => scala.Nil
  }

/*
  @Test def `test the most tweet data from union`: Unit =
    new TestSets {
      val techCrunch = """[
{ "user": "TechCrunch", "text": "Resignation Media Hires CEO John Ellis To Run Tapiture, Its Fast-Growing Pinterest For Men  http://t.co/ctn7oWJc by @anthonyha", "retweets": 18.0 },
{ "user": "TechCrunch", "text": "FreedomPop Opens Its Freemium Internet Service To The Masses With New Public Beta http://t.co/35mA9Adp by @chrisvelazco", "retweets": 27.0 },
{ "user": "TechCrunch", "text": "Dish And The Dream Of Internet TV http://t.co/y8KcSl8G by @ryanlawler", "retweets": 25.0 },
{ "user": "TechCrunch", "text": "Adobe's Acrobat XI Boasts New PDF Editor And Touch-Friendly Interface ? Upgrades Start At $139 http://t.co/1YDWvlVI by @anthonyha", "retweets": 26.0 },
{ "user": "TechCrunch", "text": "Testing Out Bodymetrics, The Startup That Wants To Be A Denim Shopper's Best Friend [TCTV] http://t.co/sPe6wA02 by @loyalelectron", "retweets": 22.0 },
{ "user": "TechCrunch", "text": "Up Close With The Next Big Home Commodity: LED Lighting http://t.co/nGPSMnMH", "retweets": 77.0 }
]"""

      val engadget = """[
{ "user": "engadget", "text": "Sony reveals Japan prices for Windows 8 VAIO machines -  http://t.co/FRCu2XVb", "retweets": 18.0 },
{ "user": "engadget", "text": "FreedomPop's pay-as-you-go data service launches in beta, offering 500MB of free WiMAX per month -  http://t.co/Ny48yXUl", "retweets": 10.0 },
{ "user": "engadget", "text": "Lenovo intros ThinkCentre M78 with AMD A-Series APU and a starting price of $449 -  http://t.co/OEDe1EwW", "retweets": 22.0 },
{ "user": "engadget", "text": "HP announces the ElitePad 900, a business-friendly Windows 8 tablet arriving in January -  http://t.co/RjSj2cms", "retweets": 133.0 }
]"""
      val techChurchTweetList = TweetReader.ParseTweets.getTweetData("TechCrunch", techCrunch)
      //printTweet(techChurchTweetList)
      val techChurchTweetSet = TweetReader.toTweetSet(techChurchTweetList)

      val engadgetTweetList = TweetReader.ParseTweets.getTweetData("engadget", engadget)
      val engadgetTweetSet = TweetReader.toTweetSet(engadgetTweetList)

      val unioned = techChurchTweetSet union engadgetTweetSet
      assert(unioned.mostRetweeted.retweets == 133.0)
    }
*/
  @Rule def individualTestTimeout = new org.junit.rules.Timeout(10 * 1000)
}
