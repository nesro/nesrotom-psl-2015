/*
 * Copyright (c) 2015, Tomas Nesrovnal (nesrotom@fit.cvut.cz)
 * This is a semestral work for MI-PSL 2015 (programming in scala) lecture
 * on FIT CTU. See: https://edux.fit.cvut.cz/courses/MI-PSL/
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
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text

import scalafx.scene.shape.Rectangle
import scalafx.Includes._ // when

import scalafx.scene.image.{Image, ImageView}

import scalafx.scene.control.Button

import scalafx.event.ActionEvent


object ScalaFXHelloWorld extends JFXApp {
	stage = new PrimaryStage {
	title = "nesrotom MI-PSL 2015 semestral work"
	scene = new Scene {

	val rect = new Rectangle {
		x = 25
		y = 40
		width = 100
		height = 100
		fill <== when (hover) choose Green otherwise Red
	}

	val rect2 = new Rectangle {
		x = 50
		y = 100
		width = 10
		height = 10
		fill = Black
	}

	val btn = new Button("click me") {
	}

	btn.onAction = {ae: ActionEvent => rect2.fill = White }

	fill = Grey
	content = new HBox {
	padding = Insets(20)
	children = Seq(
		new Text {
			text = "MI-PSL "
			style = "-fx-font-size: 10pt"
			fill = new LinearGradient(
			endX = 0,
			stops = Stops(PaleGreen, SeaGreen))
		},
		new Text {
			text = "is the best :D"
			style = "-fx-font-size: 10pt"
			fill = new LinearGradient(
				endX = 0,
				stops = Stops(Cyan, DodgerBlue)
			)
			effect = new DropShadow {
				color = DodgerBlue
				radius = 25
				spread = 0.25
			}
		},
		rect,
		rect2,
		btn,
		new ImageView {
			//image = new Image(this.getClass.getResourceAsStream("/scalafx/ensemble/images/icon-48x48.png"))
			image = new Image("http://www.scala-lang.org/resources/img/scala-logo.png")
			fitHeight = 50
			fitWidth = 150
			preserveRatio = true
			smooth = true
		}
	)
	}
	}
	}
}
