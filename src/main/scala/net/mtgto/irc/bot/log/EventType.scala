package net.mtgto.irc.bot.log

object EventType extends Enumeration {
  val MessageType = Value("message")
  val PrivateMessageType = Value("private_message")
  val NoticeType = Value("notice")
  val InviteType = Value("invite")
  val JoinType = Value("join")
  val KickType = Value("kick")
  val ModeType = Value("mode")
  val TopicType = Value("topic")
  val NickChangeType = Value("nick_change")
  val OpType = Value("op")
  val PartType = Value("part")
  val QuitType = Value("quit")
}
