package edu.knoldus

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Query, Twitter, TwitterFactory}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterOperation {

  /**
    * @param consumerKey - Twitter account's consumerKey
    * @param consumerSecret - Twitter account's consumerSecret
    * @param accessToken - Twitter account's accessToken
    * @param accessTokenSecret - Twitter account's accessTokenSecret
    * @return twitter account instance
    */
  def getTwitterInstance(consumerKey: String, consumerSecret: String, accessToken: String, accessTokenSecret: String): Twitter = {
    val configurationBuilder = new ConfigurationBuilder()
    configurationBuilder.setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    new TwitterFactory(configurationBuilder.build).getInstance
  }

  /**
    * @param hashTag - HashTag for which tweets are to be retrieved
    * @param twitter - Twitter account instance
    * @throws Exception when Twitter service or network is unavailable
    * @return list of tweets for provided HashTag
    */
  def getTweetsForHashTag(hashTag: String, twitter: Twitter): Future[List[twitter4j.Status]] = Future {
    twitter.search(new Query(hashTag)).getTweets.asScala.toList
  }

  /**
    * @param hashTag - HashTag for which tweets are to be retrieved
    * @param twitter - Twitter account instance
    * @throws Exception when Twitter service or network is unavailable
    * @return number of tweets for provided HashTag
    */
  def getNumberOfTweetsForHashTag(hashTag: String, twitter: Twitter): Future[Int] = Future {
    try {
      twitter.search(new Query(hashTag)).getTweets.size()
    }
    catch {
      case exception: Exception => throw exception
    }
  }

  /**
    * @param hashTag - HashTag for which tweets are to be retrieved
    * @param twitter - Twitter account instance
    * @param startDate - Start date of duration for which average tweets are to be calculated
    * @param endDate - End date of duration for which average tweets are to be calculated
    * @throws Exception when Twitter service or network is unavailable
    * @return Average tweets per day for provided HashTag and Duration
    */
  def getAverageTweetsPerDay(hashTag: String, twitter: Twitter, startDate: String, endDate: String): Future[Long] = Future {
    try {
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      val oldDate = LocalDate.parse(startDate, formatter)
      val newDate = LocalDate.parse(endDate, formatter)
      val numberOfDays = newDate.toEpochDay() - oldDate.toEpochDay()
      val query = new Query(hashTag)
      query.setSince(startDate)
      query.setUntil(endDate)
      twitter.search(query).getTweets.size() / numberOfDays
    }
    catch {
      case exception: Exception => throw exception
    }
  }

  /**
    * @param hashTag - HashTag for which tweets are to be retrieved
    * @param twitter - Twitter account instance
    * @throws Exception when Twitter service or network is unavailable
    * @return Average number of ReTweets for provided HashTag
    */
  def getReTweetCount(hashTag: String, twitter: Twitter): Future[Int] = Future {
    try {
      val query = new Query(hashTag)
      val tweets = twitter.search(query).getTweets.asScala.toList
      tweets.map(_.getRetweetCount).size / tweets.size
    }
    catch {
      case exception: Exception => throw exception
    }
  }

  /**
    * @param hashTag - HashTag for which tweets are to be retrieved
    * @param twitter - Twitter account instance
    * @throws Exception when Twitter service or network is unavailable
    * @return Average number of Likes for provided HashTag
    */
  def getLikesCount(hashTag: String, twitter: Twitter): Future[Int] = Future {
    try {
      val query = new Query(hashTag)
      val tweets = twitter.search(query).getTweets.asScala.toList
      tweets.map(_.getFavoriteCount).size / tweets.size
    }
    catch {
      case exception: Exception => throw exception
    }
  }

}
