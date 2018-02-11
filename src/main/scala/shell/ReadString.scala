package shell

import interpreter.{Interpreter, Token}

object ReadString {
  def readString(): String = {
    val str = io.StdIn.readLine()
    val interpreter = new Interpreter(str)
    interpreter.expr().toString
  }

  def main(args: Array[String]): Unit = {
    Iterator.continually(readString())
      .takeWhile(_.nonEmpty)
      .foreach(line => println("> " + line))
  }
}