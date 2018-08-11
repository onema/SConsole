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

import com.typesafe.scalalogging.Logger
import org.rogach.scallop.{ScallopOption, Subcommand}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

abstract class Command(commandName: String) extends Subcommand(commandName) {

  //--- Fields ---
  protected val log: Logger = Logger("sconsole")
  protected val options = new mutable.HashMap[String, Any]
  protected var application: Application = _

  //--- Abstract Methods ---
  def configure(): Unit

  def execute(): Unit

  //--- Methods ---
  def name: String = commandNameAndAliases.head

  /**
    * @param app The main application running the commands
    */
  def setApplication(app: Application): Unit =  application = app

  /**
    * Add an option to the command. This can be any of the Scallop supported option types and definitions
    * @param opt a ScallopOption[A] returned by the opt function
    * @tparam A Option Type
    */
  def addOption[A](opt: ScallopOption[A]): Unit = {
    options(opt.name) = opt
  }

  /**
    * Retrieve any of the Scallop supported option types and definitions stored in the command
    * @param optionName The name given to the option definition
    * @tparam A Option type
    * @return
    */
  def getOption[A](optionName: String): ScallopOption[A] = {
    Try(options(optionName).asInstanceOf[A]) match {
      case Success(result) => result.asInstanceOf[ScallopOption[A]]
      case Failure(exception) => throw exception
    }
  }

  /**
    * Add a trail arg, this is a wrapper around addOption
    * @param arg a ScallopOption[A] returned by the trailArg function
    * @tparam A  Option type
    */
  def addTrailArg[A](arg: ScallopOption[A]): Unit = addOption(arg)

  /**
    * Retrieve a trailArg by name
    * @param argName The name given to the trailArg
    * @tparam A Option Type
    * @return
    */
  def getTrailArg[A](argName: String): ScallopOption[A] = getOption(argName)

  /**
    * Add Scallop properties "props"
    * @param map Dictionary of String, A returned by the scallop props function
    * @tparam A Type of the map values
    */
  def addProps[A](map: Map[String, A]): Unit = {
    val name = map("name").asInstanceOf[String]
    options(name) = map
  }

  /**
    * Retrieve a property by name
    * @param propName name of the property
    * @tparam A type of the property
    * @return
    */
  def getProps[A](propName: String): Map[String, A] = {
    getOption(propName).toOption match {
      case Some(x) => x
      case None => throw new Exception("Property \"" + propName + "\" not found")
    }
  }

  /**
    * @param description description of the command
    */
  def description(description: String): Unit = banner(
    s"""
       |$description
       |Options:
     """.stripMargin)
}
