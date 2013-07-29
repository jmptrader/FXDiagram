package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.services.ImageCache
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.binding.ObjectBinding
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent

import static javafx.geometry.HPos.*
import static javafx.geometry.VPos.*

import static extension de.fxdiagram.core.Extensions.*
import static extension javafx.util.Duration.*

class XRapidButton extends Parent implements XActivatable {
	
	@FxProperty@ReadOnly boolean isActive
	
	XNode host
	
	Placer placer
	
	(XRapidButton)=>void action

	Timeline timeline
	
	new(XNode host, double xPos, double yPos, 
		String file, (XRapidButton)=>void action) {
		this.host = host
		this.action = action
		children += new ImageView => [
			image = ImageCache.get.getImage(file)
		]
		placer = new Placer(this, xPos, yPos)
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
		host.node.addEventHandler(MouseEvent.MOUSE_ENTERED, [
			show
		]) 
		host.node.addEventHandler(MouseEvent.MOUSE_EXITED, [
			fade
		])
	}
	
	def doActivate() {
		visible = false
		onMouseEntered = [ show ]
		onMouseExited = [ fade ]
		onMousePressed = [ 
			action.apply(this) consume
		]
		placer.activate
		placer.addListener [
			element, oldVal, newVal | 
			layoutX = newVal.x
			layoutY = newVal.y
		] 
	}

	def protected setPosition(Point2D position) {
		translateX = position.x
		translateY = position.y			
	}
	
	def getHost() {	host }

	def getPlacer() { placer }
	
	def show() {
		getTimeline.stop
		visible = true
		opacity = 1.0
	}
	
	def fade() {
		getTimeline.playFromStart	
	}
	
	def protected getTimeline() {
		if(timeline == null) 
			timeline = new Timeline => [
				autoReverse = true
				keyFrames += new KeyFrame(500.millis, 
					new KeyValue(this.opacityProperty, new Double(1.0)))
				keyFrames += new KeyFrame(1000.millis, 
					new KeyValue(this.opacityProperty, 0.0))
				keyFrames += new KeyFrame(1000.millis, 
					new KeyValue(this.visibleProperty, false)
				)
			]
		timeline
	}
	
	def getChooserPosition() {
		val hpos = if(placer.XPos < 0.25)
				LEFT
			else if (placer.XPos < 0.75)
				CENTER
			else 
				RIGHT		
		val vpos = if(placer.YPos < 0.25)
				TOP
			else if (placer.YPos < 0.75)
				CENTER
			else 
				BOTTOM		
		Pos.valueOf(vpos + '_' + hpos)
	}
}

class Placer extends ObjectBinding<Point2D> {
	
	XRapidButton button
	double xPos
	double yPos
	
	new(XRapidButton button, double xPos, double yPos) {
		this.button = button
		this.xPos = xPos
		this.yPos = yPos
		activate
	}
	
	def activate() {
		val node = button.host
		bind(node.layoutXProperty, node.layoutYProperty, node.layoutBoundsProperty)
	}

	override protected computeValue() {
		val node = button.host
		val boundsInDiagram = node.localToDiagram(node.layoutBounds)
		if(boundsInDiagram != null) {
			val totalWidth = boundsInDiagram.width + 2 * button.layoutBounds.width
			val totalHeight = boundsInDiagram.height + 2 * button.layoutBounds.height
			val position = new Point2D(boundsInDiagram.minX - 1.5 * button.layoutBounds.width + xPos * totalWidth,
					boundsInDiagram.minY - 1.5 * button.layoutBounds.height + yPos * totalHeight)
			position
		} else {
			null
		}
	}
	
	def getXPos() { xPos }
	
	def getYPos() { yPos }
	
}