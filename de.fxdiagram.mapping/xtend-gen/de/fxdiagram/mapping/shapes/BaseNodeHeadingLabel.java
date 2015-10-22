package de.fxdiagram.mapping.shapes;

import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.shapes.BaseNodeLabel;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class BaseNodeHeadingLabel<T extends Object> extends BaseNodeLabel<T> {
  public BaseNodeHeadingLabel() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseNodeHeadingLabel(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  protected Node createNode() {
    Node _createNode = super.createNode();
    final Procedure1<Node> _function = (Node it) -> {
      Insets _insets = new Insets(10, 20, 10, 20);
      StackPane.setMargin(this, _insets);
      Text _text = this.getText();
      Text _text_1 = this.getText();
      Font _font = _text_1.getFont();
      String _family = _font.getFamily();
      Text _text_2 = this.getText();
      Font _font_1 = _text_2.getFont();
      double _size = _font_1.getSize();
      double _multiply = (_size * 1.1);
      Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
      _text.setFont(_font_2);
    };
    return ObjectExtensions.<Node>operator_doubleArrow(_createNode, _function);
  }
}
