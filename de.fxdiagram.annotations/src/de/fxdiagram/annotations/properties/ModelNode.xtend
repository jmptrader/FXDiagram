package de.fxdiagram.annotations.properties

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration
import org.eclipse.xtend.lib.macro.declaration.MemberDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeReference
import org.eclipse.xtend.lib.macro.declaration.Visibility

/**
 * An active annotation that makes the annotated type as serializable by implementing
 * {@link XModelProvider} and adding all properties listed in the {@link value} field.
 * The properties' types must be primitive or implement {@link XModelProvider}. 
 * 
 * @see de.fxdiagram.core.model
 */
@Active(ModelNodeProcessor)
@Target(ElementType.TYPE)
annotation ModelNode {
	String[] value = #[]
	val inherit = true
}

class ModelNodeProcessor extends AbstractClassProcessor {
	
	extension TransformationContext context 
	
	override doTransform(MutableClassDeclaration annotatedClass, TransformationContext context) {
		this.context = context
		val modelNodeAnnotationType = findTypeGlobally(ModelNode)
		val modelAnnotation = annotatedClass.findAnnotation(modelNodeAnnotationType)
		val validPropertyNames = newArrayList
		modelAnnotation
			.getStringArrayValue('value')
			.forEach[
				val accessor = getPropertyAccessor(annotatedClass, it, true)
				if(accessor == null) {
					// issue a warning only, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=457681
					modelAnnotation.addWarning("Cannot find JavaFX property '" + it + "'")
				}
				validPropertyNames += it
			]
		val existingNoArgConstructor = annotatedClass.findDeclaredConstructor()
		if(existingNoArgConstructor == null) {
			annotatedClass.addConstructor[
				docComment = 'Automatically generated by @ModelNode. Needed for deserialization.'
			]
		}
		val modelProviderType = newTypeReference('de.fxdiagram.core.model.XModelProvider')
		if(!modelProviderType.type.isAssignableFrom(annotatedClass))
			annotatedClass.implementedInterfaces = annotatedClass.implementedInterfaces + #[modelProviderType]
		annotatedClass.addMethod('populate', [
			addParameter('modelElement', newTypeReference('de.fxdiagram.core.model.ModelElementImpl'))
			val isInheritAttribtueSet = modelAnnotation.getValue('inherit') != Boolean.FALSE
			val superClass = annotatedClass.extendedClass
			val isInherit = isInheritAttribtueSet &&
				if(superClass != null) {
					val isSuperImplementsModelProvider = modelProviderType.isAssignableFrom(superClass)
					val isSuperHasModelNodeAnnotation = (superClass.type as TypeDeclaration).findAnnotation(modelNodeAnnotationType) != null
					isSuperImplementsModelProvider || isSuperHasModelNodeAnnotation
				} else {
					false
				}
			body = '''
				«IF isInherit»super.populate(modelElement);«ENDIF»
				«FOR accessor: validPropertyNames.map[getPropertyAccessor(annotatedClass, it, true)]»
					modelElement.addProperty(«accessor.call», «newTypeReference(accessor.componentType.type)».class);
				«ENDFOR»
			'''
		])
	}
	
	protected def CharSequence getHierarchy(ClassDeclaration clazz) '''
		Class: «clazz.simpleName»
			«FOR m: clazz.declaredMethods»
				«m.simpleName»
			«ENDFOR»
		«(clazz.extendedClass?.type as ClassDeclaration)?.hierarchy ?: ''»
	'''
	
	protected def MemberDeclaration getPropertyAccessor(ClassDeclaration clazz, String propertyName, boolean allowPrivate) {
		val field = clazz.findDeclaredField(propertyName) 
			?: clazz.findDeclaredField((propertyName + 'Property'))
		if(field != null && (allowPrivate || field.visibility != Visibility.PRIVATE)) 
			return field
		val method = clazz.findDeclaredMethod(propertyName)
			?: clazz.findDeclaredMethod((propertyName + 'Property'))
			?: clazz.findDeclaredMethod(('get' + propertyName.toFirstUpper)) 
		if(method != null && (allowPrivate || method.visibility != Visibility.PRIVATE)) 
			return method
		if(clazz.extendedClass != null) 
			return getPropertyAccessor(clazz.extendedClass.type as ClassDeclaration, propertyName, false)
		return null
	}
	
	
	protected def getComponentType(MemberDeclaration member) {
		getComponentType(
			switch member {
				MethodDeclaration: member.returnType
				FieldDeclaration: member.type
				default: object
			})
	}
	
	protected def getCall(MemberDeclaration member) {
		member.simpleName + (if (member instanceof MethodDeclaration) '()' else '')
	}
	
	protected def TypeReference getComponentType(TypeReference it) {
		if(newTypeReference(Property, newWildcardTypeReference).isAssignableFrom(it))
			return isListType(Double)
				?: isListType(String)
				?: isListType(Integer)
				?: isListType(Boolean)
				?: isListType(Long)
				?: isListType(Float)
				?: isListType(Enum)
				?: isListType(Object)
			    ?: isType(DoubleProperty, Double) 
				?: isType(StringProperty, String)
				?: isType(IntegerProperty, Integer)
				?: isType(BooleanProperty, Boolean)
				?: isType(LongProperty, Long)
				?: isType(FloatProperty, Float)
				?: actualTypeArguments.head
		else 
			return null
	}
	
	protected def TypeReference isType(TypeReference typeRef, Class<? extends Property<?>> propertyType, Class<?> clazz) {
		if(newTypeReference(propertyType).isAssignableFrom(typeRef)) 
			return newTypeReference(clazz)
		else
			return null
	}

	protected def TypeReference isListType(TypeReference typeRef, Class<?> clazz) {
		val componentType = newTypeReference(clazz)
		if(newTypeReference(ListProperty, componentType).isAssignableFrom(typeRef)) 
			return componentType
		else
			return null
	}
}