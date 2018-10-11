SConsole
========
Simple model around [scallop](https://github.com/scallop/scallop/wiki) that streamlines and removes the boilerplate to define CLI applications and create sub-commands.

In this model you create `Commands` that configure each `Subcommand`. These commands are also in charge of executing the command logic. 
Once those commands have been defined you can add them to an application. 

Example
-------
> **NOTE:**
>
> This example was inspired by the article "**[Creating Neat .NET Core Command Line Apps](https://samyn.co/post/creating-neat-net-core-console-apps/)**"

### Create a command: 

```scala
// HideCommand.scala
import io.onema.command.Command

class HideCommand extends Command("hide") {

  //--- Methods ---
  override def configure(): Unit = {
    description("Instruct the ninja to hide in a specific location.")
    addTrailArg(trailArg[String](
      name = "where",
      required = false,
      descr = "Hiding location",
      default = Some("under a turtle")
    ))
  }
  
  override def execute(): Unit = {
    val location = getOption("where").getOrElse("???")
    println(s"Ninja is hidden $location")
  }
}
```

Now create another command

```scala
// AttackCommand.scala
import io.onema.command.Command

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

### Create and run the application

```scala
// Main.scala
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
        |Usage: ninja [hide|attack]
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
> ninja hide "under a rock"
ninja is hiding under a rock

> ninja hide
ninja is hiding under a turtle

> ninja attack  --exclude civilians
Ninja is attacking dragons, badguys, animals

> ninja attack --help
Usage: ninja attack [OPTION]
Instruct the ninja to go and attack!

Options:
     
  -e, --exclude  <arg>...   Things to exclude while attacking.
  -s, --scream  <arg>       Scream while attacking
  -h, --help                Show help message

```
