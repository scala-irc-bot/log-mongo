log-mongo
========
Log-mongo is a bot plugin for [scala-irc-bot](http://github.com/scala-irc-bot/scala-irc-bot/).
It stores all received IRC events to MongoDB.
# Installation
1. git clone
2. sbt assembly
3. copy `log-mongo/target/log-mongo-assembly-0.0.1-SNAPSHOT.jar` to `scala-irc-bot/bots/`.
4. modify `scala-irc-bot/config/Config.scala` like:

```scala
val bots = Array[(String, Option[BotConfig])](
  ("net.mtgto.irc.bot.log.LogMongoBot",
  Some(net.mtgto.irc.bot.log.LogMongoBotConfig(
    "127.0.0.1", 27017, "irc", "events", Some("username", "password"))))
)
```
# About
Log-mongo uses [casbah](https://github.com/mongodb/casbah) to access MongoDB.