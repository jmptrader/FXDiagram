package de.fxdiagram.examples.neonsign;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NeonSignNode extends XNode {
  private TextField textField;
  
  private Text neonText;
  
  public NeonSignNode() {
    FlipNode _flipNode = new FlipNode();
    final Procedure1<FlipNode> _function = new Procedure1<FlipNode>() {
      public void apply(final FlipNode it) {
        RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
        final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
          public void apply(final RectangleBorderPane it) {
            ObservableList<Node> _children = it.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                it.setTextOrigin(VPos.TOP);
                it.setText("Neon Sign");
                Insets _insets = new Insets(10, 20, 10, 20);
                StackPane.setMargin(it, _insets);
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
            _children.add(_doubleArrow);
          }
        };
        RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
        it.setFront(_doubleArrow);
        VBox _neonSign = NeonSignNode.this.getNeonSign();
        it.setBack(_neonSign);
      }
    };
    FlipNode _doubleArrow = ObjectExtensions.<FlipNode>operator_doubleArrow(_flipNode, _function);
    this.setNode(_doubleArrow);
  }
  
  protected VBox getNeonSign() {
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = new Procedure1<VBox>() {
      public void apply(final VBox it) {
        it.setStyle("-fx-background-image: url(\"de/fxdiagram/examples/neonsign/brick.jpg\");");
        it.setAlignment(Pos.TOP_CENTER);
        ObservableList<Node> _children = it.getChildren();
        TextField _textField = new TextField();
        final Procedure1<TextField> _function = new Procedure1<TextField>() {
          public void apply(final TextField it) {
            it.setText("How many LOC are necessary?");
            Insets _insets = new Insets(10, 40, 10, 40);
            VBox.setMargin(it, _insets);
          }
        };
        TextField _doubleArrow = ObjectExtensions.<TextField>operator_doubleArrow(_textField, _function);
        TextField _textField_1 = NeonSignNode.this.textField = _doubleArrow;
        _children.add(_textField_1);
        ObservableList<Node> _children_1 = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_1 = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringProperty _textProperty = it.textProperty();
            StringProperty _textProperty_1 = NeonSignNode.this.textField.textProperty();
            _textProperty.bind(_textProperty_1);
            Font _font = Font.font("Nanum Pen Script", 100);
            it.setFont(_font);
            Color _web = Color.web("#feeb42");
            it.setFill(_web);
            it.setWrappingWidth(580);
            it.setTextAlignment(TextAlignment.CENTER);
            int _minus = (-7);
            it.setRotate(_minus);
            Blend _blend = new Blend();
            final Procedure1<Blend> _function = new Procedure1<Blend>() {
              public void apply(final Blend it) {
                it.setMode(BlendMode.MULTIPLY);
                Bloom _bloom = new Bloom();
                it.setTopInput(_bloom);
                InnerShadow _innerShadow = new InnerShadow();
                final Procedure1<InnerShadow> _function = new Procedure1<InnerShadow>() {
                  public void apply(final InnerShadow it) {
                    Color _web = Color.web("#f13a00");
                    it.setColor(_web);
                    it.setRadius(5);
                    it.setChoke(0.4);
                  }
                };
                InnerShadow _doubleArrow = ObjectExtensions.<InnerShadow>operator_doubleArrow(_innerShadow, _function);
                it.setBottomInput(_doubleArrow);
              }
            };
            Blend _doubleArrow = ObjectExtensions.<Blend>operator_doubleArrow(_blend, _function);
            it.setEffect(_doubleArrow);
          }
        };
        Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
        Text _neonText = NeonSignNode.this.neonText = _doubleArrow_1;
        _children_1.add(_neonText);
      }
    };
    VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
    return _doubleArrow;
  }
  
  public void doActivate() {
    super.doActivate();
    Timeline _timeline = new Timeline();
    final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
      public void apply(final Timeline it) {
        int _minus = (-1);
        it.setCycleCount(_minus);
        ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
        Duration _millis = Duration.millis(10);
        DoubleProperty _opacityProperty = NeonSignNode.this.neonText.opacityProperty();
        KeyValue _keyValue = new <Number>KeyValue(_opacityProperty, Double.valueOf(0.75));
        KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
        _keyFrames.add(_keyFrame);
        ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
        Duration _millis_1 = Duration.millis(20);
        DoubleProperty _opacityProperty_1 = NeonSignNode.this.neonText.opacityProperty();
        KeyValue _keyValue_1 = new <Number>KeyValue(_opacityProperty_1, Double.valueOf(0.95));
        KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
        _keyFrames_1.add(_keyFrame_1);
        ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
        Duration _millis_2 = Duration.millis(40);
        DoubleProperty _opacityProperty_2 = NeonSignNode.this.neonText.opacityProperty();
        KeyValue _keyValue_2 = new <Number>KeyValue(_opacityProperty_2, Double.valueOf(0.85));
        KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
        _keyFrames_2.add(_keyFrame_2);
        ObservableList<KeyFrame> _keyFrames_3 = it.getKeyFrames();
        Duration _millis_3 = Duration.millis(50);
        DoubleProperty _opacityProperty_3 = NeonSignNode.this.neonText.opacityProperty();
        KeyValue _keyValue_3 = new <Number>KeyValue(_opacityProperty_3, Integer.valueOf(1));
        KeyFrame _keyFrame_3 = new KeyFrame(_millis_3, _keyValue_3);
        _keyFrames_3.add(_keyFrame_3);
        it.play();
      }
    };
    ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
  }
}