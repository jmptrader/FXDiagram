package de.fxdiagram.lib.simple;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.BoundsExtensions;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Helper tool for nodes with nested diagrams.
 */
@SuppressWarnings("all")
public class DiagramScaler implements XActivatable {
  private ChangeListener<Bounds> boundsInLocalListener;
  
  private ChangeListener<Number> layoutListener;
  
  private XDiagram diagram;
  
  private ListChangeListener<XNode> listChangeListener;
  
  public DiagramScaler(final XDiagram diagram) {
    this.diagram = diagram;
    final ChangeListener<Bounds> _function = (ObservableValue<? extends Bounds> prop, Bounds oldVal, Bounds newVal) -> {
      this.scaleToFit();
    };
    this.boundsInLocalListener = _function;
    final ChangeListener<Number> _function_1 = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
      this.scaleToFit();
    };
    this.layoutListener = _function_1;
    final ListChangeListener<XNode> _function_2 = (ListChangeListener.Change<? extends XNode> change) -> {
      while (change.next()) {
        {
          boolean _wasAdded = change.wasAdded();
          if (_wasAdded) {
            List<? extends XNode> _addedSubList = change.getAddedSubList();
            final Consumer<XNode> _function_3 = (XNode it) -> {
              ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
              _boundsInLocalProperty.addListener(this.boundsInLocalListener);
              DoubleProperty _layoutXProperty = it.layoutXProperty();
              _layoutXProperty.addListener(this.layoutListener);
              DoubleProperty _layoutYProperty = it.layoutYProperty();
              _layoutYProperty.addListener(this.layoutListener);
            };
            _addedSubList.forEach(_function_3);
          }
          boolean _wasRemoved = change.wasRemoved();
          if (_wasRemoved) {
            List<? extends XNode> _removed = change.getRemoved();
            final Consumer<XNode> _function_4 = (XNode it) -> {
              ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
              _boundsInLocalProperty.removeListener(this.boundsInLocalListener);
              DoubleProperty _layoutXProperty = it.layoutXProperty();
              _layoutXProperty.removeListener(this.layoutListener);
              DoubleProperty _layoutYProperty = it.layoutYProperty();
              _layoutYProperty.removeListener(this.layoutListener);
            };
            _removed.forEach(_function_4);
          }
        }
      }
    };
    this.listChangeListener = _function_2;
  }
  
  public void scaleToFit() {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      this.diagram.setScaleX(1);
      this.diagram.setScaleY(1);
      this.diagram.setClip(null);
    } else {
      Group _nodeLayer = this.diagram.getNodeLayer();
      ObservableList<Node> _children = _nodeLayer.getChildren();
      final Function1<Node, BoundingBox> _function = (Node it) -> {
        Bounds _layoutBounds = it.getLayoutBounds();
        double _layoutX = it.getLayoutX();
        double _layoutY = it.getLayoutY();
        return BoundsExtensions.translate(_layoutBounds, _layoutX, _layoutY);
      };
      List<BoundingBox> _map = ListExtensions.<Node, BoundingBox>map(_children, _function);
      final Function2<BoundingBox, BoundingBox, BoundingBox> _function_1 = (BoundingBox b0, BoundingBox b1) -> {
        return BoundsExtensions.operator_plus(b0, b1);
      };
      final BoundingBox myBounds = IterableExtensions.<BoundingBox>reduce(_map, _function_1);
      double _xifexpression = (double) 0;
      double _width = myBounds.getWidth();
      boolean _notEquals = (_width != 0);
      if (_notEquals) {
        double _width_1 = this.getWidth();
        double _width_2 = myBounds.getWidth();
        double _divide = (_width_1 / _width_2);
        _xifexpression = Math.min(1, _divide);
      } else {
        _xifexpression = 1;
      }
      final double newScaleX = _xifexpression;
      double _xifexpression_1 = (double) 0;
      double _height = myBounds.getHeight();
      boolean _notEquals_1 = (_height != 0);
      if (_notEquals_1) {
        double _height_1 = this.getHeight();
        double _height_2 = myBounds.getHeight();
        double _divide_1 = (_height_1 / _height_2);
        _xifexpression_1 = Math.min(1, _divide_1);
      } else {
        _xifexpression_1 = 1;
      }
      final double newScaleY = _xifexpression_1;
      final double newScale = Math.min(newScaleX, newScaleY);
      this.diagram.setScaleX(newScale);
      this.diagram.setScaleY(newScale);
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_2 = (Rectangle it) -> {
        this.fit(it, newScale, newScaleX, newScaleY, myBounds);
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      this.diagram.setClip(_doubleArrow);
    }
  }
  
  protected void fit(final Rectangle it, final double newScale, final double newScaleX, final double newScaleY, final Bounds allNodesBounds) {
    it.setArcWidth((22 / newScale));
    it.setArcHeight((22 / newScale));
    double _minX = allNodesBounds.getMinX();
    it.setX(_minX);
    double _width = allNodesBounds.getWidth();
    it.setWidth(_width);
    if ((newScaleX > newScaleY)) {
      double _width_1 = this.getWidth();
      double _divide = (_width_1 / newScale);
      double _width_2 = allNodesBounds.getWidth();
      final double delta = (_divide - _width_2);
      double _x = it.getX();
      double _minus = (_x - (0.5 * delta));
      it.setX(_minus);
      double _width_3 = it.getWidth();
      double _plus = (_width_3 + delta);
      it.setWidth(_plus);
    }
    double _minY = allNodesBounds.getMinY();
    it.setY(_minY);
    double _height = allNodesBounds.getHeight();
    it.setHeight(_height);
    if ((newScaleY > newScaleX)) {
      double _height_1 = this.getHeight();
      double _divide_1 = (_height_1 / newScale);
      double _height_2 = allNodesBounds.getHeight();
      final double delta_1 = (_divide_1 - _height_2);
      double _y = it.getY();
      double _minus_1 = (_y - (0.5 * delta_1));
      it.setY(_minus_1);
      double _height_3 = it.getHeight();
      double _plus_1 = (_height_3 + delta_1);
      it.setHeight(_plus_1);
    }
  }
  
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.widthProperty.addListener(this.layoutListener);
      this.heightProperty.addListener(this.layoutListener);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      final Consumer<XNode> _function = (XNode it) -> {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
        _boundsInLocalProperty.addListener(this.boundsInLocalListener);
        DoubleProperty _layoutXProperty = it.layoutXProperty();
        _layoutXProperty.addListener(this.layoutListener);
        DoubleProperty _layoutYProperty = it.layoutYProperty();
        _layoutYProperty.addListener(this.layoutListener);
      };
      _nodes.forEach(_function);
      ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
      _nodes_1.addListener(this.listChangeListener);
      Group _buttonLayer = this.diagram.getButtonLayer();
      ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = _buttonLayer.layoutBoundsProperty();
      _layoutBoundsProperty.addListener(this.boundsInLocalListener);
      this.scaleToFit();
      this.isActiveProperty.set(true);
    }
  }
  
  public void deactivate() {
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      this.widthProperty.removeListener(this.layoutListener);
      this.heightProperty.removeListener(this.layoutListener);
      this.diagram.setClip(null);
      this.diagram.setScaleX(1);
      this.diagram.setScaleY(1);
      Group _buttonLayer = this.diagram.getButtonLayer();
      ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = _buttonLayer.layoutBoundsProperty();
      _layoutBoundsProperty.removeListener(this.boundsInLocalListener);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      _nodes.removeListener(this.listChangeListener);
      ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
      final Consumer<XNode> _function = (XNode it) -> {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
        _boundsInLocalProperty.removeListener(this.boundsInLocalListener);
        DoubleProperty _layoutXProperty = it.layoutXProperty();
        _layoutXProperty.removeListener(this.layoutListener);
        DoubleProperty _layoutYProperty = it.layoutYProperty();
        _layoutYProperty.removeListener(this.layoutListener);
      };
      _nodes_1.forEach(_function);
      this.isActiveProperty.set(false);
    }
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width",_initWidth());
  
  private static final double _initWidth() {
    return 80;
  }
  
  public double getWidth() {
    return this.widthProperty.get();
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height",_initHeight());
  
  private static final double _initHeight() {
    return 60;
  }
  
  public double getHeight() {
    return this.heightProperty.get();
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
