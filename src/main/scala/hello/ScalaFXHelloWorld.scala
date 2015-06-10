/*
 * Copyright (c) 2015, Tomas Nesrovnal (nesrotom@fit.cvut.cz)
 * This is a semestral work for MI-PSL 2015 (programming in scala) lecture
 * on FIT CTU. See: https://edux.fit.cvut.cz/courses/MI-PSL/
 * docs: http://docs.scalafx.googlecode.com/hg/scalafx-8.0/scaladoc/index.html
 *
 * Copyright (c) 2011-2015, ScalaFX Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ScalaFX Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCALAFX PROJECT OR ITS CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package hello

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
//import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text

import scalafx.scene.shape.Rectangle
import scalafx.Includes._ // when

import scalafx.scene.image.{Image, ImageView, PixelWriter, WritableImage}

import scalafx.scene.control.{Button, ProgressBar}

import scalafx.event.ActionEvent
import scalafx.scene.input.MouseEvent

import scala.collection._
import scala.collection.parallel.mutable.ParArray

/*
import scala.math.BigDecimal
*/

object ScalaFXHelloWorld extends JFXApp {
	stage = new PrimaryStage {
	title = "nesrotom MI-PSL 2015 semestral work"
	scene = new Scene {

	var xsize = 500.0
	var ysize = 500.0
	var ymin = -5.0 
	var ymax = 5.0 
	var xmin = -5.0 
	var xmax = 5.0 
	var power = 15.0
	var threshold = 15
	var xclick = 0.0 
	var yclick = 0.0 
	var zoomed = 0
	var xzoom = 0.0 
	var yzoom = 0.0 
	var zoom = 0.0 
	var xstep = 0.0 
	var ystep = 0.0 

	//val pixels = new Array[Int]((xsize * ysize).toInt)
	val pixels = new ParArray[Int]((xsize * ysize).toInt)

	def reset() {
		ymin = -5.0
		ymax = 5.0
		xmin = -5.0
		xmax = 5.0
		power = 15.0
		threshold = 15
		xclick = 0.0
		yclick = 0.0
		zoomed = 0
		xzoom = 0.0
		yzoom = 0.0
		zoom = 0.0
		xstep = 0.0
		ystep = 0.0
		draw()
	}

	def scale(x: Int, min: Int, max: Int, a: Int, b: Int): Int = {
		if (max == min) {
			0
		} else {
			(((b-a)*(x-min))/(max - min)) + a
		}
	}

	def mb(ix: Double, iy: Double, threshold: Int): Int = {
		var i = threshold
		var x = 0.0 
		var y = 0.0 
		while (x * x + y * y < 2 && 0 < i) {
			val yy = 2 * x * y + iy 
			val xx = x * x - y * y + ix 
			y = yy
			x = xx
			i -= 1
		}
		threshold - i
	}


	val iv = new ImageView {
		//image = new Image(this.getClass.getResourceAsStream("/scalafx/ensemble/images/icon-48x48.png"))
		image = new Image("http://www.scala-lang.org/resources/img/scala-logo.png")
		fitHeight = ysize.toInt 
		fitWidth = xsize.toInt
		//preserveRatio = true
		//smooth = true
	}
	val wi = new WritableImage(xsize.toInt, ysize.toInt)
	val pw = wi.pixelWrit

	var b = new Button("reset")
	b.onAction = (_:ActionEvent)=> {
		reset()
	}

	def draw() {
		var imin = 99999
		var imax = -99999
		var cx = (xmax - xmin) / xsize
		var cy = (ymax - ymin) / ysize
		var progress = 0
		//for (ix <- 0 to xsize.toInt - 1; iy <- 0 to ysize.toInt - 1) {
		for (ix <- (0 until xsize.toInt).par) {
			for (iy <- 0 until ysize.toInt) {
				val c = mb(xmin + cx*ix, ymin + cy*iy, power.toInt)
				pixels(ix + iy * ysize.toInt) = c
				if (c < imin) {
					imin = c
				}
				if (c > imax) {
					imax = c
				}
			}
			if ((ix % (xsize/10)) == 0) {
				progress = progress + 10
				println(s"computed: $progress %")
			}
		}
		println(s"imax=$imax")
		println(s"imin=$imin")

		for (ix <- 0 to xsize.toInt - 1; iy <- 0 to ysize.toInt - 1) {
			val cr = scale(pixels(ix + xsize.toInt * iy), imin, imax, 0, (255*255*255))
			pw.setColor(ix, iy, Color.web(f"$cr%06x"))
		}
		iv.image = wi
	}

	iv.onMouseClicked = (e: MouseEvent) => {
		yclick = ymin + (ymax - ymin) * (( e.y.toDouble) / ysize)
		xclick = xmin + (xmax - xmin) * (( e.x.toDouble) / xsize)
		yzoom = (ymax - ymin) / 4.0
		xzoom = (xmax - xmin) / 4.0
		ymin = yclick - yzoom
		ymax = yclick + yzoom
		xmin = xclick - xzoom
		xmax = xclick + xzoom
		ystep = (ymax - ymin) / ysize
		xstep = (xmax - xmin) / xsize
		power = power * 1.2
		threshold = (threshold.toDouble * 1.2).toInt
		zoomed = zoomed + 1
		
		var x = xmin
		var y = ymin
		val range = 0 to ((xsize.toInt * ysize.toInt))
		println(s"xzoom: $xzoom")
		println(s"yzoom: $yzoom")
		println(s"xclick: $xclick")
		println(s"yclick: $yclick")
		println(s"xmin: $xmin")
		println(s"xmax: $xmax")
		println(s"ymin: $ymin")
		println(s"ymax: $ymax")
		println(s"xstep: $xstep")
		println(s"ystep: $ystep")

		draw()
	}

	draw()

	fill = Grey 
	content = new VBox {
	margin = Insets(25)
	padding = Insets(25)
	children = Seq(
		iv,
		b
	)
	}
	}
	}
}
/*
object ScalaFXHelloWorld extends JFXApp {
	stage = new PrimaryStage {
	title = "nesrotom MI-PSL 2015 semestral work"
	scene = new Scene {

	var xsize = 500.0
	var ysize = 500.0
	var ymin = -5.0 : BigDecimal
	var ymax = 5.0 : BigDecimal
	var xmin = -5.0 : BigDecimal
	var xmax = 5.0 : BigDecimal
	var power = 15.0
	var threshold = 15
	var xclick = 0.0 : BigDecimal
	var yclick = 0.0 : BigDecimal
	var zoomed = 0
	var xzoom = 0.0 : BigDecimal
	var yzoom = 0.0 : BigDecimal
	var zoom = 0.0 : BigDecimal
	var xstep = 0.0 : BigDecimal
	var ystep = 0.0 : BigDecimal

	//val pixels = new Array[Int]((xsize * ysize).toInt)
	val pixels = new ParArray[Int]((xsize * ysize).toInt)

	def reset() {
		ymin = -5.0
		ymax = 5.0
		xmin = -5.0
		xmax = 5.0
		power = 15.0
		threshold = 15
		xclick = 0.0
		yclick = 0.0
		zoomed = 0
		xzoom = 0.0
		yzoom = 0.0
		zoom = 0.0
		xstep = 0.0
		ystep = 0.0
		draw()
	}

	def scale(x: Int, min: Int, max: Int, a: Int, b: Int): Int = {
		if (max == min) {
			0
		} else {
			(((b-a)*(x-min))/(max - min)) + a
		}
	}

	def mb(ix: BigDecimal, iy: BigDecimal, threshold: Int): Int = {
		var i = threshold
		var x = 0.0 : BigDecimal
		var y = 0.0 : BigDecimal
		while (x * x + y * y < 2 && 0 < i) {
			val yy = 2 * x * y + iy : BigDecimal
			val xx = x * x - y * y + ix : BigDecimal
			y = yy
			x = xx
			i -= 1
		}
		threshold - i
	}


	val iv = new ImageView {
		//image = new Image(this.getClass.getResourceAsStream("/scalafx/ensemble/images/icon-48x48.png"))
		image = new Image("http://www.scala-lang.org/resources/img/scala-logo.png")
		fitHeight = ysize.toInt 
		fitWidth = xsize.toInt
		//preserveRatio = true
		//smooth = true
	}
	val wi = new WritableImage(xsize.toInt, ysize.toInt)
	val pw = wi.pixelWrit

	var b = new Button("reset")
	b.onAction = (_:ActionEvent)=> {
		reset()
	}

	def draw() {
		var imin = 99999
		var imax = -99999
		var cx = (xmax - xmin) / xsize
		var cy = (ymax - ymin) / ysize
		var progress = 0
		//for (ix <- 0 to xsize.toInt - 1; iy <- 0 to ysize.toInt - 1) {
		for (ix <- (0 until xsize.toInt).par) {
			for (iy <- 0 until ysize.toInt) {
				val c = mb(xmin + cx*ix, ymin + cy*iy, power.toInt)
				pixels(ix + iy * ysize.toInt) = c
				if (c < imin) {
					imin = c
				}
				if (c > imax) {
					imax = c
				}
			}
			if ((ix % (xsize/10)) == 0) {
				progress = progress + 10
				println(s"computed: $progress %")
			}
		}
		println(s"imax=$imax")
		println(s"imin=$imin")

		for (ix <- 0 to xsize.toInt - 1; iy <- 0 to ysize.toInt - 1) {
			val cr = scale(pixels(ix + xsize.toInt * iy), imin, imax, 0, (255*255*255))
			pw.setColor(ix, iy, Color.web(f"$cr%06x"))
		}
		iv.image = wi
	}

	iv.onMouseClicked = (e: MouseEvent) => {
		yclick = ymin + (ymax - ymin) * (( e.y.toDouble) / ysize)
		xclick = xmin + (xmax - xmin) * (( e.x.toDouble) / xsize)
		yzoom = (ymax - ymin) / 4.0
		xzoom = (xmax - xmin) / 4.0
		ymin = yclick - yzoom
		ymax = yclick + yzoom
		xmin = xclick - xzoom
		xmax = xclick + xzoom
		ystep = (ymax - ymin) / ysize
		xstep = (xmax - xmin) / xsize
		power = power * 1.2
		threshold = (threshold.toDouble * 1.2).toInt
		zoomed = zoomed + 1
		
		var x = xmin
		var y = ymin
		val range = 0 to ((xsize.toInt * ysize.toInt))
		println(s"xzoom: $xzoom")
		println(s"yzoom: $yzoom")
		println(s"xclick: $xclick")
		println(s"yclick: $yclick")
		println(s"xmin: $xmin")
		println(s"xmax: $xmax")
		println(s"ymin: $ymin")
		println(s"ymax: $ymax")
		println(s"xstep: $xstep")
		println(s"ystep: $ystep")

		draw()
	}

	draw()

	fill = Grey 
	content = new VBox {
	margin = Insets(25)
	padding = Insets(25)
	children = Seq(
		iv,
		b
	)
	}
	}
	}
}*/
