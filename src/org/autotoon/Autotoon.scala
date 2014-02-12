package org.autotoon

import org.opencv.core._
import org.opencv.highgui._
import java.awt.image.BufferedImage
import javax.swing._
import org.opencv.core.Mat
import org.opencv.highgui.VideoCapture


class Autotoon extends JPanel{
  var image:BufferedImage = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB)
  def getImage = image
  def setImage(nu:BufferedImage) = {
    image = nu
  }
}

object Autotoon{
  def main(args: Array[String]): Int = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    val cap = new VideoCapture(0)
    var image = new Mat()
    if(!cap.isOpened){
      println("Device not opened")
      return -1
    }
    cap.grab()
    for(i <- 0 to 8){
      try{
        cap.grab()
        Thread.sleep(1000)
      }catch{
        case e:Exception =>
      }
    }
    cap.grab()
    cap.retrieve(image)
    Highgui.imwrite("/home/parallels/test.png",image)
    0
  }
}