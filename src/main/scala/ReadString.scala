object ReadString {
  def readString(): String = {
    io.StdIn.readLine()
  }

  def main(args: Array[String]): Unit = {
    Iterator.continually(readString())
      .takeWhile(_.nonEmpty)
      .foreach(line => println("> " + line))
  }
}