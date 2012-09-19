package net.mtgto.irc.bot.log

import net.mtgto.irc.config.BotConfig

case class LogMongoBotConfig(
  val hostname: String,
  val port: Int,
  val databaseName: String,
  val collectionName: String,
  val account: Option[(String, String)]
) extends BotConfig
