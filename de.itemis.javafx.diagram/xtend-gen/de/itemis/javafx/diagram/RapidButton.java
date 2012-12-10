package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RapidButton extends Button {
  private Timeline timeline;
  
  public RapidButton(final String text) {
    super(text);
    this.setVisible(false);
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          RapidButton.this.show();
        }
      };
    this.setOnMouseEntered(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          RapidButton.this.fade();
        }
      };
    this.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
  }
  
  public void show() {
    Timeline _timeline = this.getTimeline();
    _timeline.stop();
    this.setVisible(true);
    this.setOpacity(1.0);
  }
  
  public void fade() {
    Timeline _timeline = this.getTimeline();
    _timeline.playFromStart();
  }
  
  protected Timeline getTimeline() {
    Timeline _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.timeline, null);
      if (_equals) {
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
            public void apply(final Timeline it) {
              it.setAutoReverse(true);
              ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
              Duration _millis = Duration.millis(500);
              DoubleProperty _opacityProperty = RapidButton.this.opacityProperty();
              KeyValue _keyValue = new KeyValue(_opacityProperty, Double.valueOf(1.0));
              KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
              _keyFrames.add(_keyFrame);
              ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
              Duration _millis_1 = Duration.millis(1500);
              DoubleProperty _opacityProperty_1 = RapidButton.this.opacityProperty();
              KeyValue _keyValue_1 = new KeyValue(_opacityProperty_1, Double.valueOf(0.0));
              KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
              _keyFrames_1.add(_keyFrame_1);
              ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
              Duration _millis_2 = Duration.millis(1500);
              BooleanProperty _visibleProperty = RapidButton.this.visibleProperty();
              KeyValue _keyValue_2 = new KeyValue(_visibleProperty, Boolean.valueOf(false));
              KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
              _keyFrames_2.add(_keyFrame_2);
            }
          };
        Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
        this.timeline = _doubleArrow;
      }
      _xblockexpression = (this.timeline);
    }
    return _xblockexpression;
  }
}
