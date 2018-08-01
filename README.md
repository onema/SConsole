SConsole
========
Simple model around scallop to define a CLI application and sub-commands for the app.

Example
-------
### Create a command: 

```scala
import io.onema.command.Command

class HideCommand extends Command("hide") {

  //--- Methods ---
  override def configure(): Unit = {
    description(
      s"""
         |Usage: ninja hide [OPTION]
         |Instruct the ninja to hide in a specific location.
         |
       |Options:
    """.stripMargin)
  }
  addTrailArg(trailArg[String](
    name = "where",
    required = false,
    descr = "Hiding location",
    default = Some("under a turtule")
  ))
  addTrailArg(trailArg[Int](name = "foo", required = false))
  override def execute(): Unit = {
    val location = getOption("where").getOrElse("???")
    println(s"Ninja is hidden $location")
  }
}
```

attack command

```scala
class AttackCommand extends Command("attack") {

  //--- Methods ---
  override def configure(): Unit = {
    description(
      s"""
         |Usage: ninja attack [OPTION]
         |Instruct the ninja to go and attack!
         |
       |Options:
    """.stripMargin)
  }
  addOption(opt[List[String]](
    name = "exclude",
    descr = "Things to exclude while attacking.",
    required = false,
    default = Option(List("civilians"))
  
  ))
  addOption(opt[String](
    name = "scream",
    short = 's',
    required = false,
    descr = "Scream while attacking"
  ))
  
  
  override def execute(): Unit = {
    val exclusions = getOption[List[String]]("exclude").getOrElse(List[String]())
    val attacking = Seq("dragons", "badguys", "civilians", "animals")
      .filter(x => !exclusions.contains(x))
    println(s"Ninja is attacking ${attacking.mkString(", ")}")
    getOption[String]("scream").toOption match {
      case Some(x) => println(x)
      case None =>
    }
  }
}
```

### Create an applicaiton

```scala
import io.onema.command.Application

object Main {
  def main(args: Array[String]): Unit = {
    val version =
      s"""
         | __   _ _____ __   _ _____ _______
         |  | \\  |   |   | \\  |   |   |_____|
         |  |  \\_| __|__ |  \\_| __|   |     |
         |  0.1.0
  """.stripMargin
    val description =
      """
        |Usage: ninja [hide]
        |Ninja command
        |
        |Options:
      """.stripMargin
    val app = new Application("ninja", version, description)
    app.add(new HideCommand)
    app.add(new AttackCommand)

    // Add additional commands here...
    app.run(args)
  }
}
```

### Run the command
```bash
ninja --hide "under a rock"
ninja --hide
ninja --help

```