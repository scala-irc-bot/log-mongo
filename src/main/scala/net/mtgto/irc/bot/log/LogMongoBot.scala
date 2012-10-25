package net.mtgto.irc.bot.log

import EventType._

import net.mtgto.irc.{Bot, Client}
import net.mtgto.irc.event._

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

  implicit def convertEventToDBObject(event: Event): DBObject = {
    event match {
      case Message(channel, nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> MessageType.toString,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
      case PrivateMessage(nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> PrivateMessageType.toString,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
      case Notice(target, nickname, username, hostname, text, date) =>
        DBObject(
          "type" -> NoticeType.toString,
          "target" -> target,
          "sourceNickname" -> nickname,
          "sourceUsername" -> username,
          "sourceHostname" -> hostname,
          "text" -> text,
          "date" -> date
        )
      case Invite(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, date) =>
        DBObject(
          "type" -> InviteType.toString,
          "channel" -> channel,
          "targetNickname" -> targetNickname,
          "sourceNickname" -> sourceNickname,
          "sourceUsername" -> sourceUsername,
          "sourceHostname" -> sourceHostname,
          "date" ->date
        )
      case Join(channel, nickname, username, hostname, date) =>
        DBObject(
          "type" -> JoinType.toString,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "date" -> date
        )
      case Kick(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, reason, date) =>
        DBObject(
          "type" -> KickType.toString,
          "channel" -> channel,
          "targetNickname" -> targetNickname,
          "sourceNickname" -> sourceNickname,
          "sourceUsername" -> sourceUsername,
          "sourceHostname" -> sourceHostname,
          "reason" -> reason,
          "date" -> date
        )
      case Mode(channel, nickname, username, hostname, mode, date) =>
        DBObject(
          "type" -> ModeType.toString,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "mode" -> mode,
          "date" -> date
        )
      case Topic(channel, nickname, topic, date) =>
        DBObject(
          "type" -> TopicType.toString,
          "channel" -> channel,
          "nickname" -> nickname,
          "topic" -> topic,
          "date" -> date
        )
      case NickChange(oldNickname, newNickname, username, hostname, date) =>
        DBObject(
          "type" -> NickChangeType.toString,
          "oldNickname" -> oldNickname,
          "newNickname" -> newNickname,
          "username" -> username,
          "hostname" -> hostname,
          "date" -> date
        )
      case Op(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, date) =>
        DBObject(
          "type" -> OpType.toString,
          "channel" -> channel,
          "targetNickname" -> targetNickname,
          "sourceNickname" -> sourceNickname,
          "sourceUsername" -> sourceUsername,
          "sourceHostname" -> sourceHostname,
          "date" -> date
        )
      case Part(channel, nickname, username, hostname, date) =>
        DBObject(
          "type" -> PartType.toString,
          "channel" -> channel,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "date" -> date
        )
      case Quit(nickname, username, hostname, reason, date) =>
        DBObject(
          "type" -> QuitType.toString,
          "nickname" -> nickname,
          "username" -> username,
          "hostname" -> hostname,
          "reason" -> reason,
          "date" -> date
        )
    }
  }

  implicit def convertDBObjectToEvent(db: DBObject): Event = {
    EventType.withName(db.getAs[String]("type").get) match {
      case MessageType =>
        Message(db.getAs[String]("channel").get, db.getAs[String]("nickname").get, db.getAs[String]("username").get,
                db.getAs[String]("hostname").get, db.getAs[String]("text").get, db.getAs[Date]("date").get)
      case PrivateMessageType =>
        PrivateMessage(db.getAs[String]("nickname").get,
                       db.getAs[String]("username").get,
                       db.getAs[String]("hostname").get,
                       db.getAs[String]("text").get,
                       db.getAs[Date]("date").get)
      case NoticeType =>
        Notice(db.getAs[String]("target").get,
               db.getAs[String]("sourceNickname").get,
               db.getAs[String]("sourceUsername").get,
               db.getAs[String]("sourceHostname").get,
               db.getAs[String]("text").get,
               db.getAs[Date]("date").get)
      case InviteType =>
        Invite(db.getAs[String]("channel").get,
               db.getAs[String]("targetNickname").get,
               db.getAs[String]("sourceNickname").get,
               db.getAs[String]("sourceUsername").get,
               db.getAs[String]("sourceHostname").get,
               db.getAs[Date]("date").get)
      case JoinType =>
        Join(db.getAs[String]("channel").get,
             db.getAs[String]("nickname").get,
             db.getAs[String]("username").get,
             db.getAs[String]("hostname").get,
             db.getAs[Date]("date").get)
      case KickType =>
        Kick(db.getAs[String]("channel").get,
             db.getAs[String]("targetNickname").get,
             db.getAs[String]("sourceNickname").get,
             db.getAs[String]("sourceUsername").get,
             db.getAs[String]("sourceHostname").get,
             db.getAs[String]("reason").get,
             db.getAs[Date]("date").get)
      case ModeType =>
        Mode(db.getAs[String]("channel").get,
             db.getAs[String]("nickname").get,
             db.getAs[String]("username").get,
             db.getAs[String]("hostname").get,
             db.getAs[String]("mode").get,
             db.getAs[Date]("date").get)
      case TopicType =>
        Topic(db.getAs[String]("channel").get,
              db.getAs[String]("nickname").get,
              db.getAs[String]("topic").get,
              db.getAs[Date]("date").get)
      case NickChangeType =>
        NickChange(db.getAs[String]("oldNickname").get,
                   db.getAs[String]("newNickname").get,
                   db.getAs[String]("username").get,
                   db.getAs[String]("hostname").get,
                   db.getAs[Date]("date").get)
      case OpType =>
        Op(db.getAs[String]("channel").get,
           db.getAs[String]("targetNickname").get,
           db.getAs[String]("sourceNickname").get,
           db.getAs[String]("sourceUsername").get,
           db.getAs[String]("sourceHostname").get,
           db.getAs[Date]("date").get)
      case PartType =>
        Part(db.getAs[String]("channel").get,
             db.getAs[String]("nickname").get,
             db.getAs[String]("username").get,
             db.getAs[String]("hostname").get,
             db.getAs[Date]("date").get)
      case QuitType =>
        Quit(db.getAs[String]("nickname").get,
             db.getAs[String]("username").get,
             db.getAs[String]("hostname").get,
             db.getAs[String]("reason").get,
             db.getAs[Date]("date").get)
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

  override def onPrivateMessage(message: PrivateMessage) = {
    collection.insert(message)
  }

  override def onNotice(notice: Notice) = {
    collection.insert(notice)
  }

  override def onInvite(invite: Invite) = {
    collection.insert(invite)
  }

  override def onJoin(join: Join) = {
    collection.insert(join)
  }

  override def onKick(kick: Kick) = {
    collection.insert(kick)
  }

  override def onMode(mode: Mode) = {
    collection.insert(mode)
  }

  override def onTopic(topic: Topic) = {
    collection.insert(topic)
  }

  override def onNickChange(nickChange: NickChange) = {
    collection.insert(nickChange)
  }

  override def onOp(op: Op) = {
    collection.insert(op)
  }

  override def onPart(part: Part) = {
    collection.insert(part)
  }

  override def onQuit(quit: Quit) = {
    collection.insert(quit)
  }
}
