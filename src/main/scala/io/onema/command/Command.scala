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

import org.rogach.scallop.{ScallopOption, Subcommand}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

abstract class Command(commandName: String) extends Subcommand(commandName) {

  //--- Fields ---
  protected val options = new mutable.HashMap[String, Any]
  protected var application: Application = _

  //--- Abstract Methods ---
  def configure(): Unit = {}

  def execute(): Unit

  //--- Methods ---
  def name: String = commandNameAndAliases.head

  /**
    *
    * @param app
    */
  def setApplication(app: Application): Unit =  application = app

  /**
    *
    * @param opt
    * @tparam A
    */
  def addOption[A](opt: ScallopOption[A]): Unit = {
    options(opt.name) = opt
  }

  /**
    *
    * @param optionName
    * @tparam A
    * @return
    */
  def getOption[A](optionName: String): ScallopOption[A] = {
    Try(options(optionName).asInstanceOf[A]) match {
      case Success(result) => result.asInstanceOf[ScallopOption[A]]
      case Failure(exception) => throw exception
    }
  }

  /**
    *
    * @param arg
    * @tparam A
    */
  def addTrailArg[A](arg: ScallopOption[A]): Unit = addOption(arg)

  /**
    *
    * @param argName
    * @tparam A
    * @return
    */
  def getTrailArg[A](argName: String): ScallopOption[A] = getOption(argName)

  /**
    *
    * @param map
    * @tparam A
    */
  def addProps[A](map: Map[String, A]): Unit = {
    val name = map("name").asInstanceOf[String]
    options(name) = map
  }

  /**
    *
    * @param propName
    * @tparam A
    * @return
    */
  def getProps[A](propName: String): Map[String, A] = {
    getOption(propName).toOption match {
      case Some(x) => x
      case None => throw new Exception("Property \"" + propName + "\" not found")
    }
  }

  /**
    *
    * @param description
    */
  def description(description: String): Unit = banner(
    s"""
       |$description
       |
     """.stripMargin)

}
