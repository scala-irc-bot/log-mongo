package net.mtgto.irc.bot.log

import org.specs2.mutable._
import org.specs2.mock._

import net.mtgto.irc.Client
import net.mtgto.irc.event._

import java.util.Date

class LogMongoBotSpec extends Specification with Mockito {
  "LogMongoBot" should {
    "deserialize serialized events" in {
      val bot = new LogMongoBot(LogMongoBotConfig("127.0.0.1", 27017, "irc", "events", None))
      val channel = "#test"
      val nickname = "alice"
      val username = "~alice"
      val hostname = "example.com"
      val targetNickname = "bob"
      val date = new Date

      "message" in {
        val message = Message(channel, nickname, username, hostname, "message", date)
        val db = bot.convertEventToDBObject(message)
        message === bot.convertDBObjectToEvent(db)
      }

      "private message" in {
        val privateMessage = PrivateMessage(nickname, username, hostname, "private message", date)
        val db = bot.convertEventToDBObject(privateMessage)
        privateMessage === bot.convertDBObjectToEvent(db)
      }

      "notice" in {
        val notice = Notice(targetNickname, nickname, username, hostname, "notice", date)
        val db = bot.convertEventToDBObject(notice)
        notice === bot.convertDBObjectToEvent(db)
      }

      "invite" in {
        val invite = Invite(channel, targetNickname, nickname, username, hostname, date)
        val db = bot.convertEventToDBObject(invite)
        invite === bot.convertDBObjectToEvent(db)
      }

      "join" in {
        val join = Join(channel, nickname, username, hostname, date)
        val db = bot.convertEventToDBObject(join)
        join === bot.convertDBObjectToEvent(db)
      }

      "kick" in {
        val kick = Kick(channel, targetNickname, nickname, username, hostname, "kick reason", date)
        val db = bot.convertEventToDBObject(kick)
        kick === bot.convertDBObjectToEvent(db)
      }

      "mode" in {
        val mode = Mode(channel, nickname, username, hostname, "mode", date)
        val db = bot.convertEventToDBObject(mode)
        mode === bot.convertDBObjectToEvent(db)
      }

      "topic" in {
        val topic = Topic(channel, nickname, "topic", date)
        val db = bot.convertEventToDBObject(topic)
        topic === bot.convertDBObjectToEvent(db)
      }

      "nick change" in {
        val newNickname = "ecila"
        val nickChange = NickChange(nickname, newNickname, username, hostname, date)
        val db = bot.convertEventToDBObject(nickChange)
        nickChange === bot.convertDBObjectToEvent(db)
      }

      "op" in {
        val op = Op(channel, targetNickname, nickname, username, hostname, date)
        val db = bot.convertEventToDBObject(op)
        op === bot.convertDBObjectToEvent(db)
      }

      "part" in {
        val part = Part(channel, nickname, username, hostname, date)
        val db = bot.convertEventToDBObject(part)
        part === bot.convertDBObjectToEvent(db)
      }

      "quit" in {
        val quit = Quit(nickname, username, hostname, "quit reason", date)
        val db = bot.convertEventToDBObject(quit)
        quit === bot.convertDBObjectToEvent(db)
      }
    }
  }
}
