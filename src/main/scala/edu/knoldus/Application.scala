package edu.knoldus

import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Application extends App {

  //scalastyle:off
  val config = ConfigFactory.load("application.conf")

  val obj = new TwitterOperation

  val log = Logger.getLogger(this.getClass)

  val consumerKey = config.getString("TwitterCredentials.Key.consumerKey")
  val consumerSecret = config.getString("TwitterCredentials.Key.consumerSecret")
  val accessToken = config.getString("TwitterCredentials.Key.accessToken")
  val accessTokenSecret = config.getString("TwitterCredentials.Key.accessTokenSecret")

  val twitter = obj.getTwitterInstance(consumerKey, consumerSecret, accessToken, accessTokenSecret)

  val hashTag: String = "KheloIndia"

  val tweets = obj.getTweetsForHashTag(hashTag, twitter) onComplete {
    case Success(value) => log.info(s"\n\nFethed Tweets = $value \n")
    case Failure(exception) => log.info(s"\n\n$exception \n")
  }
  log.info("\n")
  log.info("\n")

  val tweetsCount = obj.getNumberOfTweetsForHashTag(hashTag, twitter) onComplete {
    case Success(value) => log.info(s"\n\nTweets Count = $value ")
    case Failure(exception) => log.info(s"\n\n$exception ")
  }
  log.info("\n")
  log.info("\n")

  val averageTweetsPerDay = obj.getAverageTweetsPerDay(hashTag, twitter, "2018-01-30", "2018-02-01") onComplete {
    case Success(value) => log.info(s"\n\nAverage tweets per day = $value \n")
    case Failure(exception) => log.info(s"\n\n$exception \n")
  }
  log.info("\n")
  log.info("\n")

  val averageReTweet = obj.getReTweetCount(hashTag, twitter, "2018-01-30", "2018-02-02") onComplete {
    case Success(value) => log.info(s"\n\nAverage ReTweets = $value \n")
    case Failure(exception) => log.info(s"\n\n$exception \n")
  }
  log.info("\n")
  log.info("\n")

  val averageLikes = obj.getLikesCount(hashTag, twitter, "2018-01-30", "2018-02-02") onComplete {
    case Success(value) => log.info(s"\n\nAverage Likes = $value \n")
    case Failure(exception) => log.info(s"\n\n$exception \n")
  }
  log.info("\n")


  Thread.sleep(20000)
  //scalastyle:on

}
