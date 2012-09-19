package net.mtgto.irc.bot.log

import net.mtgto.irc.{Bot, Client}
import net.mtgto.irc.event.{Event, Message, Notice, PrivateMessage}

import com.mongodb.casbah._

import java.util.Date

class LogMongoBot(
  val config: LogMongoBotConfig
) extends Bot {
  protected[this] val db = MongoConnection(config.hostname, config.port)(config.databaseName)

  protected[this] val collection = db(config.collectionName)
  
  config.account.map {
    account => if (!db.authenticate(account._1, account._2)) throw new RuntimeException
  }

  protected[this] val messageTypeStr = "message"

  protected[this] val privateMessageTypeStr = "private_message"

  protected[this] val noticeTypeStr = "notice"

  protected[this] implicit def convertEventToDBObject(event: Event): DBObject = {
    event match {
      case Message(channel, nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> messageTypeStr,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
      case PrivateMessage(nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> privateMessageTypeStr,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
      case Notice(channel, nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> noticeTypeStr,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
    }
  }

  protected[this] implicit def convertDBObjectToEvent(db: DBObject): Event = {
    db.getAs[String]("type").get match {
      case messageTypeStr =>
        Message(db.getAs[String]("channel").get, db.getAs[String]("nickname").get, db.getAs[String]("username").get,
                db.getAs[String]("hostname").get, db.getAs[String]("text").get, db.getAs[Date]("date").get)
    }
  }

  /**
   * find events from log filtering by channel name.
   */
  def findEventsByChannel(channel: String): Seq[Event] = {
    collection.find(DBObject("channel" -> channel)).sort(DBObject("date" -> -1)).map(convertDBObjectToEvent).toSeq
  }

  override def onMessage(message: Message) = {
    collection.insert(message)
  }
}
