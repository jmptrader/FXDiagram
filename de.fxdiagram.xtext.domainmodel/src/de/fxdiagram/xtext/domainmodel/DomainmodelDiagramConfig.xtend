package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.ESetting
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.shapes.BaseDiagramNode
import javafx.scene.paint.Color
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.example.domainmodel.domainmodel.Property

import static org.eclipse.xtext.example.domainmodel.domainmodel.DomainmodelPackage.Literals.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class DomainmodelDiagramConfig extends AbstractDiagramConfig {
	
	@Inject extension DomainModelUtil domainModelUtil
	 
	val packageDiagram = new DiagramMapping<PackageDeclaration>(this, 'packageDiagram') {
		override calls() {
			entityNode.nodeForEach[elements.filter(Entity)]
			packageNode.nodeForEach[elements.filter(PackageDeclaration)]
		}
	}
	
	val packageNode = new NodeMapping<PackageDeclaration>(this, 'packageNode') {
		override createNode(AbstractXtextDescriptor<PackageDeclaration> descriptor) {
			 new BaseDiagramNode(descriptor) 	
		}
		
		override calls() {
			packageDiagram.nestedDiagramFor[ it ]			
		}
	}

	val entityNode = new NodeMapping<Entity>(this, 'entityNode') {
		override createNode(AbstractXtextDescriptor<Entity> descriptor) {
			new EntityNode(descriptor) 
		}
		
		override calls() {
			propertyConnection.outConnectionForEach [
				features
					.filter(Property)
					.filter[domainModelUtil.getReferencedEntity(type) != null]
			].makeLazy[getArrowButton("Add property")]
			superTypeConnection.outConnectionForEach [ entity |
				val superEntity = domainModelUtil.getReferencedEntity(entity.superType)
				if(superEntity == null) 
					emptyList 
				else 
					#[new ESetting(entity, ENTITY__SUPER_TYPE, 0)] 
			].makeLazy[getTriangleButton("Add superclass")]
		}
	} 

	val propertyConnection = new ConnectionMapping<Property>(this, 'propertyConnection') {
		override createConnection(AbstractXtextDescriptor<Property> descriptor) {
			new XConnection(descriptor) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [ label |
					label.text.text = descriptor.withDomainObject[name]
				]		
			]
		}
		
		override calls() {
			entityNode.target [domainModelUtil.getReferencedEntity(type)]
		}
	}
	
	val superTypeConnection = new ConnectionMapping<ESetting<Entity>>(this, 'superTypeConnection') {
		override createConnection(AbstractXtextDescriptor<ESetting<Entity>> descriptor) {
			new XConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					null, Color.WHITE, false)
			]
		}
		
		override calls() {
			entityNode.target [getReferencedEntity(target as JvmTypeReference)]
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			Entity:
				add(entityNode)
			PackageDeclaration: {
				add(packageNode)
				add(packageDiagram)
			}
			Property: 
				add(propertyConnection)			
		}
	}
}