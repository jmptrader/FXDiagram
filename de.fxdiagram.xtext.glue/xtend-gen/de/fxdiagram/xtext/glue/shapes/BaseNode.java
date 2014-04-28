package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class BaseNode<T extends Object> extends SimpleNode {
  public BaseNode(final XtextDomainObjectDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  protected XtextDomainObjectDescriptor<T> getDescriptor() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((XtextDomainObjectDescriptor<T>) _domainObject);
  }
  
  protected XtextDomainObjectProvider getXtextDomainObjectProvider() {
    XRoot _root = CoreExtensions.getRoot(this);
    return _root.<XtextDomainObjectProvider>getDomainObjectProvider(XtextDomainObjectProvider.class);
  }
  
  public void doActivate() {
    super.doActivate();
    XtextDomainObjectDescriptor<T> _descriptor = this.getDescriptor();
    AbstractMapping<T> _mapping = _descriptor.getMapping();
    if ((_mapping instanceof NodeMapping<?>)) {
      XtextDomainObjectDescriptor<T> _descriptor_1 = this.getDescriptor();
      AbstractMapping<T> _mapping_1 = _descriptor_1.getMapping();
      final NodeMapping<T> nodeMapping = ((NodeMapping<T>) _mapping_1);
      List<AbstractConnectionMappingCall<?,T>> _outgoing = nodeMapping.getOutgoing();
      final Function1<AbstractConnectionMappingCall<?,T>,Boolean> _function = new Function1<AbstractConnectionMappingCall<?,T>,Boolean>() {
        public Boolean apply(final AbstractConnectionMappingCall<?,T> it) {
          return Boolean.valueOf(it.isLazy());
        }
      };
      Iterable<AbstractConnectionMappingCall<?,T>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?,T>>filter(_outgoing, _function);
      final Procedure1<AbstractConnectionMappingCall<?,T>> _function_1 = new Procedure1<AbstractConnectionMappingCall<?,T>>() {
        public void apply(final AbstractConnectionMappingCall<?,T> it) {
          XtextDomainObjectProvider _xtextDomainObjectProvider = BaseNode.this.getXtextDomainObjectProvider();
          XDiagramConfigInterpreter _xDiagramConfigInterpreter = new XDiagramConfigInterpreter(_xtextDomainObjectProvider);
          LazyConnectionMappingBehavior<?,T> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(BaseNode.this, it, _xDiagramConfigInterpreter, true);
          BaseNode.this.addBehavior(_lazyConnectionMappingBehavior);
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?,T>>forEach(_filter, _function_1);
      List<AbstractConnectionMappingCall<?,T>> _incoming = nodeMapping.getIncoming();
      final Function1<AbstractConnectionMappingCall<?,T>,Boolean> _function_2 = new Function1<AbstractConnectionMappingCall<?,T>,Boolean>() {
        public Boolean apply(final AbstractConnectionMappingCall<?,T> it) {
          return Boolean.valueOf(it.isLazy());
        }
      };
      Iterable<AbstractConnectionMappingCall<?,T>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?,T>>filter(_incoming, _function_2);
      final Procedure1<AbstractConnectionMappingCall<?,T>> _function_3 = new Procedure1<AbstractConnectionMappingCall<?,T>>() {
        public void apply(final AbstractConnectionMappingCall<?,T> it) {
          XtextDomainObjectProvider _xtextDomainObjectProvider = BaseNode.this.getXtextDomainObjectProvider();
          XDiagramConfigInterpreter _xDiagramConfigInterpreter = new XDiagramConfigInterpreter(_xtextDomainObjectProvider);
          LazyConnectionMappingBehavior<?,T> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(BaseNode.this, it, _xDiagramConfigInterpreter, false);
          BaseNode.this.addBehavior(_lazyConnectionMappingBehavior);
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?,T>>forEach(_filter_1, _function_3);
    }
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BaseNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty(), DomainObjectDescriptor.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
