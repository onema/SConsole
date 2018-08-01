/**
  * This file is part of the ONEMA Default (Template) Project Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2018, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <kinojman@gmail.com>
  */

package io.onema.command

import io.onema.command.Application.BasicConf
import org.rogach.scallop.ScallopConf

import scala.collection.mutable

object Application {
  private class BasicConf(args: Seq[String], commandNameAndAliases: Seq[String]) extends ScallopConf(args, commandNameAndAliases)
}

class Application(var name: String, var version: String = "NA", var description: String = "NA") {

  //--- Fields ---
  private val commands = new mutable.HashMap[String, Command]()

  //--- Methods ---
  def find(name: String): Option[Command] = commands.get(name)

  /**
    *
    * @param command
    */
  def add(command: Command): Unit = {
    commands(command.name) = command
  }

  /**
    *
    * @param cmds
    */
  def addCommands(commands: Seq[Command]): Unit = commands.foreach(add)

  /**
    *
    * @param args
    */
  def run(args: Seq[String]): Unit = {
    val config = new BasicConf(args, Seq(name))
    config.version(version)
    config.banner(description)
    commands.values.foreach(command => {
      command.configure()
      command.setApplication(this)
      config.addSubcommand(command)
    })
    config.verify()
    config.subcommand match {
      case Some(command) =>
        command.asInstanceOf[Command].execute()
      case _ =>
        new BasicConf(Seq("--help"), Seq(name))
    }
  }
}
