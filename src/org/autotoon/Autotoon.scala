package org.autotoon

import org.opencv.core._
import org.opencv.highgui._
import org.opencv.imgproc.Imgproc._

import java.awt.image.BufferedImage
import java.awt.Graphics
import javax.swing._
import java.util


class Autotoon extends JPanel{
  var image = new Mat
  def getImage = image
  def setImage(nu:Mat) = {
    image = nu
  }


  def getOutlineImage = {
    val ddepth = CvType.CV_16S
    val scale = 1.5
    val delta = 0
    val gray = new Mat()
    val blurred = new Mat()
    val lines = new Mat()
    val liteLines = new Mat()
    val gradX = new Mat()
    val gradY = new Mat()
    val absGradX = new Mat()
    val absGradY = new Mat()
    val blur = 5
    GaussianBlur(image,blurred,new Size(blur,blur),1)
    cvtColor(image,gray,COLOR_RGB2GRAY)
    Sobel(gray,gradX,ddepth,1,0,1,scale,delta,BORDER_DEFAULT)
    Core.convertScaleAbs(gradX,absGradX)
    Sobel(gray,gradY,ddepth,1,0,1,scale,delta,BORDER_DEFAULT)
    Core.convertScaleAbs(gradY,absGradY)
    Core.addWeighted(absGradX,0.5,absGradY,0.5,0,lines)
    Core.bitwise_not(lines,liteLines)
    Autotoon matToBufferedImage liteLines
  }

  override def paintComponent(g:Graphics):Unit = {
    val temp = getOutlineImage
    g.drawImage(temp,10,10,temp.getWidth,temp.getHeight, this)
  }
}

object Autotoon{
  def mainOld(args: Array[String]): Int = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    val cap = new VideoCapture(0)
    var image = new Mat()
    var gray = new Mat()
    var blurred = new Mat()
    var lines = new Mat()
    var gradX = new Mat()
    var gradY = new Mat()
    var absGradX = new Mat()
    var absGradY = new Mat()
    val ddepth = CvType.CV_16S
    val blur = 5
    val scale = 1
    val delta = 0
    if(!cap.isOpened){
      println("Device not opened")
      return -1
    }
    cap.grab()
    for(i <- 0 to 8){
      try{
        cap.grab()
        Thread.sleep(100)
      }catch{
        case e:Exception =>
      }
    }
    cap.grab()
    cap.retrieve(image)
    GaussianBlur(image,blurred,new Size(blur,blur),0)
    cvtColor(image,gray,COLOR_RGB2GRAY)
    Sobel(gray,gradX,ddepth,1,0,7,scale,delta,BORDER_DEFAULT)
    Core.convertScaleAbs(gradX,absGradX)
    Sobel(gray,gradY,ddepth,1,0,7,scale,delta,BORDER_DEFAULT)
    Core.convertScaleAbs(gradY,absGradY)
    Core.addWeighted(absGradX,0.5,absGradY,0.5,0,lines)
    Highgui.imwrite("/home/parallels/test.png",lines)
    0
  }

  def main(args: Array[String]): Int = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    val frame = new JFrame("Capture")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(400,400)
    val panel = new Autotoon()
    frame.setContentPane(panel)
    frame.setVisible(true)
    val pic=new Mat()
    val capture = new VideoCapture(0)
    capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,1024)
    capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,768)
    if( capture.isOpened)
    {
      while( true )
      {
        capture.read(pic)
        if( !pic.empty )
        {
          frame.setSize(pic.width+40,pic.height+60)
          panel.setImage(pic)
          panel.repaint()
        }
        else
        {
          System.out.println(" --(!) No captured frame -- Break!")
          return 0
        }
      }
    }
    0
  }


  def matToBufferedImage(matrix:Mat):BufferedImage = {
    val cols = matrix.cols
    val rows = matrix.rows
    val elemSize = matrix.elemSize.asInstanceOf[Int]
    val data = new Array[Byte](cols * rows * elemSize)
    matrix.get(0, 0, data)
    val typ:Int = matrix.channels match {
      case 1 => BufferedImage.TYPE_BYTE_GRAY
      case 3 =>
        var b:Byte = 0
        for(i <- Iterator.iterate(0)(_ * 3) takeWhile (_ < data.length)){
          b = data(i)
          data(i) = data(i+2)
          data(i+2) = b
        }
        BufferedImage.TYPE_3BYTE_BGR
      case _ => 0
    }
    if(typ == 0) return null

    val image = new BufferedImage(cols, rows, typ)
    image.getRaster.setDataElements(0, 0, cols, rows, data)
    image
  }
}
