package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.model.ModelElementImpl;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "fill" })
@SuppressWarnings("all")
public class DiamondArrowHead extends ArrowHead {
  public DiamondArrowHead(final XConnection connection, final double width, final double height, final Paint stroke, final Paint fill, final boolean isSource) {
    super(connection, width, height, stroke, isSource);
    boolean _notEquals = (!Objects.equal(fill, null));
    if (_notEquals) {
      this.setFill(fill);
    }
  }
  
  public DiamondArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 10, 10, null, null, isSource);
  }
  
  public Node createNode() {
    Polygon _xblockexpression = null;
    {
      Paint _fill = this.getFill();
      boolean _equals = Objects.equal(_fill, null);
      if (_equals) {
        XConnection _connection = this.getConnection();
        ObjectProperty<Paint> _strokeProperty = _connection.strokeProperty();
        this.fillProperty.bind(_strokeProperty);
      }
      Polygon _polygon = new Polygon();
      final Procedure1<Polygon> _function = new Procedure1<Polygon>() {
        public void apply(final Polygon it) {
          ObservableList<Double> _points = it.getPoints();
          double _width = DiamondArrowHead.this.getWidth();
          double _multiply = (0.5 * _width);
          double _height = DiamondArrowHead.this.getHeight();
          double _multiply_1 = ((-0.5) * _height);
          double _width_1 = DiamondArrowHead.this.getWidth();
          double _width_2 = DiamondArrowHead.this.getWidth();
          double _multiply_2 = (0.5 * _width_2);
          double _height_1 = DiamondArrowHead.this.getHeight();
          double _multiply_3 = (0.5 * _height_1);
          _points.setAll(
            Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(_multiply_1), Double.valueOf(_width_1), Double.valueOf(0.0), Double.valueOf(_multiply_2), Double.valueOf(_multiply_3))));
          ObjectProperty<Paint> _fillProperty = it.fillProperty();
          _fillProperty.bind(DiamondArrowHead.this.fillProperty);
          ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
          ObjectProperty<Paint> _strokeProperty_1 = DiamondArrowHead.this.strokeProperty();
          _strokeProperty.bind(_strokeProperty_1);
          DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
          XConnection _connection = DiamondArrowHead.this.getConnection();
          DoubleProperty _strokeWidthProperty_1 = _connection.strokeWidthProperty();
          _strokeWidthProperty.bind(_strokeWidthProperty_1);
          it.setStrokeType(StrokeType.CENTERED);
        }
      };
      _xblockexpression = ObjectExtensions.<Polygon>operator_doubleArrow(_polygon, _function);
    }
    return _xblockexpression;
  }
  
  public double getLineCut() {
    double _width = this.getWidth();
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    return (_width + _strokeWidth);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public DiamondArrowHead() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(fillProperty, Paint.class);
  }
  
  private SimpleObjectProperty<Paint> fillProperty = new SimpleObjectProperty<Paint>(this, "fill");
  
  public Paint getFill() {
    return this.fillProperty.get();
  }
  
  public void setFill(final Paint fill) {
    this.fillProperty.set(fill);
  }
  
  public ObjectProperty<Paint> fillProperty() {
    return this.fillProperty;
  }
}
