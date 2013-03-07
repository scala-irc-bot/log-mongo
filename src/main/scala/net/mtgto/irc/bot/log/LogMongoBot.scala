package net.mtgto.irc.bot.log

import EventType._

import net.mtgto.irc.{Bot, Client}
import net.mtgto.irc.event._

import com.mongodb.casbah.Imports._

import java.util.Date

class LogMongoBot(
  val config: LogMongoBotConfig
) extends Bot {
  protected[this] val db = MongoConnection(config.hostname, config.port)(config.databaseName)

  protected[this] val collection = db(config.collectionName)

  config.account.map {
    account => if (!db.authenticate(account._1, account._2)) throw new RuntimeException
  }

  def convertEventToDBObject(event: Event) = event.asDBObject

  def convertDBObjectToEvent(db: DBObject) = db.asEvent

  implicit class EventToDBObject(event: Event) {
    def asDBObject: DBObject = {
      event match {
        case Message(channel, nickname, username, hostname, text, date) =>
          MongoDBObject(
            "type" -> MessageType.toString,
            "channel" -> channel,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "text" -> text,
            "date" -> date
          )
        case PrivateMessage(nickname, username, hostname, text, date) =>
          MongoDBObject(
            "type" -> PrivateMessageType.toString,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "text" -> text,
            "date" -> date
          )
        case Notice(target, nickname, username, hostname, text, date) =>
          MongoDBObject(
            "type" -> NoticeType.toString,
            "target" -> target,
            "sourceNickname" -> nickname,
            "sourceUsername" -> username,
            "sourceHostname" -> hostname,
            "text" -> text,
            "date" -> date
          )
        case Invite(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, date) =>
          MongoDBObject(
            "type" -> InviteType.toString,
            "channel" -> channel,
            "targetNickname" -> targetNickname,
            "sourceNickname" -> sourceNickname,
            "sourceUsername" -> sourceUsername,
            "sourceHostname" -> sourceHostname,
            "date" ->date
          )
        case Join(channel, nickname, username, hostname, date) =>
          MongoDBObject(
            "type" -> JoinType.toString,
            "channel" -> channel,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "date" -> date
          )
        case Kick(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, reason, date) =>
          MongoDBObject(
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
          MongoDBObject(
            "type" -> ModeType.toString,
            "channel" -> channel,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "mode" -> mode,
            "date" -> date
          )
        case Topic(channel, nickname, topic, date) =>
          MongoDBObject(
            "type" -> TopicType.toString,
            "channel" -> channel,
            "nickname" -> nickname,
            "topic" -> topic,
            "date" -> date
          )
        case NickChange(oldNickname, newNickname, username, hostname, date) =>
          MongoDBObject(
            "type" -> NickChangeType.toString,
            "oldNickname" -> oldNickname,
            "newNickname" -> newNickname,
            "username" -> username,
            "hostname" -> hostname,
            "date" -> date
          )
        case Op(channel, targetNickname, sourceNickname, sourceUsername, sourceHostname, date) =>
          MongoDBObject(
            "type" -> OpType.toString,
            "channel" -> channel,
            "targetNickname" -> targetNickname,
            "sourceNickname" -> sourceNickname,
            "sourceUsername" -> sourceUsername,
            "sourceHostname" -> sourceHostname,
            "date" -> date
          )
        case Part(channel, nickname, username, hostname, date) =>
          MongoDBObject(
            "type" -> PartType.toString,
            "channel" -> channel,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "date" -> date
          )
        case Quit(nickname, username, hostname, reason, date) =>
          MongoDBObject(
            "type" -> QuitType.toString,
            "nickname" -> nickname,
            "username" -> username,
            "hostname" -> hostname,
            "reason" -> reason,
            "date" -> date
          )
      }
    }
  }

  implicit class DBObjectToEvent(db: DBObject) {
    def asEvent: Event = {
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
  }

  /**
   * find events from log filtering by channel name.
   */
  def findEventsByChannel(channel: String): Seq[Event] = {
    collection.find(MongoDBObject("channel" -> channel)).sort(MongoDBObject("date" -> -1)).map(_.asEvent).toSeq
  }

  override def onMessage(client: Client, message: Message) = {
    collection.insert(message.asDBObject)
  }

  override def onPrivateMessage(client: Client, message: PrivateMessage) = {
    collection.insert(message.asDBObject)
  }

  override def onNotice(client: Client, notice: Notice) = {
    collection.insert(notice.asDBObject)
  }

  override def onInvite(client: Client, invite: Invite) = {
    collection.insert(invite.asDBObject)
  }

  override def onJoin(client: Client, join: Join) = {
    collection.insert(join.asDBObject)
  }

  override def onKick(client: Client, kick: Kick) = {
    collection.insert(kick.asDBObject)
  }

  override def onMode(client: Client, mode: Mode) = {
    collection.insert(mode.asDBObject)
  }

  override def onTopic(client: Client, topic: Topic) = {
    collection.insert(topic.asDBObject)
  }

  override def onNickChange(client: Client, nickChange: NickChange) = {
    collection.insert(nickChange.asDBObject)
  }

  override def onOp(client: Client, op: Op) = {
    collection.insert(op.asDBObject)
  }

  override def onPart(client: Client, part: Part) = {
    collection.insert(part.asDBObject)
  }

  override def onQuit(client: Client, quit: Quit) = {
    collection.insert(quit.asDBObject)
  }
}
