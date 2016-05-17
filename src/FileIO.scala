import java.io.{BufferedWriter, File, FileWriter}

import scala.collection.mutable
import scala.io.Source

/**
  * This program's purpose is to read a CSV file of purchases, and then allow the user to filter through it by category
  * The program ultimately displays all purchases that fit the desired category
  * CSV format: customer_id,date,credit_card,cvv,category
  * Desired format to output: "Customer: 23, Date: 2016-07-11" -> Slice off the time
  */

object FileIO {
  val mutableList = new mutable.MutableList[( String, String, String, String, String )] ()

  def prompt(s: String) = {
    println(s)
    io.StdIn.readLine()
  }

  def findCategory(entry: String): Unit = {
    var category: String = entry

    // Need to check if the category is valid
    def checkNull(category: String): Boolean = {
      mutableList.exists(p => p._5.equals(category.toLowerCase().capitalize))
    }

    while(! checkNull(category)) { category = prompt("Please enter a valid category:") }

    val fw = new FileWriter(new File("filtered_purchases.prn"))
    val bw = new BufferedWriter(fw)
    val list = mutableList.filter(p => p._5.equals(category.toLowerCase().capitalize))
    list.foreach(line => {
      // Sliced to show only the date, not the time
      println(s"Customer: ${line._1}, Date: ${line._2.slice(0, 10)}")
      bw.write(s"Customer: ${line._1} | Date: ${line._2.slice(0, 10)}\n")
    })
    bw.close()
  }

  def main(args: Array[String]) {
    val purchases = Source.fromFile("/Users/tristangreeno/workspace/ScalaPurchases/be594d3c-purchases.csv")
      .getLines().toList
    purchases.drop(1)
      .foreach( line => {
        val Array(id,date,cc,cvv,category) = line.split(",").map(_.trim)
        mutableList += Tuple5(id,date,cc,cvv,category)
      } )

    findCategory(prompt("Please enter a category (Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry): "))

  }
}
