/**
  * This file is part of the ONEMA Default (Template) Project Package.
  * For the full copyright and license information,
  * please view the LICENSE file that was distributed
  * with this source code.
  *
  * copyright (c) 2018, Juan Manuel Torres (http://onema.io)
  *
  * @author Juan Manuel Torres <software@onema.io>
  */

package io.onema.command

import io.onema.command.Application.BasicConf
import org.rogach.scallop.ScallopConf

import scala.collection.mutable

object Application {
  private class BasicConf(args: Seq[String], commandNameAndAliases: Seq[String]) extends ScallopConf(args, commandNameAndAliases)
}

class Application(val name: String, val version: String = "", val description: String = "", val footer: String = "") {

  //--- Fields ---
  private val commands = new mutable.HashMap[String, Command]()

  //--- Methods ---
  def find(name: String): Option[Command] = commands.get(name)

  /**
    * Add a single command to the application
    * @param command the command to be added to the application
    */
  def add(command: Command): Unit = {
    commands(command.name) = command
  }

  /**
    * Add a collection of commands to the application
    * @param commands collection of Command
    */
  def addCommands(commands: Seq[Command]): Unit = commands.foreach(add)

  /**
    * Run the application
    * @param args CLI Arguments
    */
  def run(args: Seq[String]): Unit = {
    val config =
      if(args.nonEmpty) new BasicConf(args, Seq(name))
      else new BasicConf(Seq("--help"), Seq(name))

    // Set application help information
    if(version.nonEmpty) config.version(version)
    if(description.nonEmpty) {
      config.banner(
        s"""
           |$description
           |Options:
      """.stripMargin)
    }
    if(footer.nonEmpty) config.footer(footer)

    // Set each of the commands in the configuration
    commands.values.foreach(command => {
      command.configure()
      command.setApplication(this)
      config.addSubcommand(command)
    })

    // Verify and execute sub-command
    config.verify()
    config.subcommand match {
      case Some(command) =>
        command.asInstanceOf[Command].execute()
      case _ =>
    }
  }
}
