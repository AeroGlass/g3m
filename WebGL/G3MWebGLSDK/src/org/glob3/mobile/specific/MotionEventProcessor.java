

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2I;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;


public final class MotionEventProcessor {

   final static String            TAG        = "MotionEventProcessor";

   private final G3MWidget        _widget;
   private final JavaScriptObject _canvas;
   private boolean                _mouseDown = false;
   private boolean                _keyDown   = false;
   private Vector2I               _prevPos   = null;


   public MotionEventProcessor(final G3MWidget widget,
                               final JavaScriptObject canvas) {
      _widget = widget;
      _canvas = canvas;

      jsAddMouseWheelListener();
   }


   public void processEvent(final Event event) {

      final Vector2I pos = new Vector2I(event.getClientX(), event.getClientY());
      TouchEvent touchEvent = null;

      switch (DOM.eventGetType(event)) {
         case Event.ONMOUSEMOVE:
            touchEvent = mouseMoveHandler(pos);
            break;
         case Event.ONMOUSEDOWN:
            touchEvent = mouseDownHandler(pos);
            break;
         case Event.ONMOUSEUP:
            touchEvent = mouseUpHandler(pos);
            break;
         case Event.ONDBLCLICK:
            touchEvent = doubleClickHanler(pos);
            break;
         case Event.ONKEYDOWN:
            keyDownHandler(event);
            break;
         case Event.ONKEYUP:
            keyUpHandler(event);
            break;
         case Event.ONCONTEXTMENU:
            event.preventDefault();
            break;
         case Event.ONMOUSEWHEEL:
            event.preventDefault();
            break;

         default:
            return;
      }

      if (touchEvent != null) {
         final ArrayList<TouchEvent> tes = new ArrayList<TouchEvent>();

         tes.add(touchEvent);
         eventHandler(tes);
      }
   }


   public void eventHandler(final ArrayList<TouchEvent> touchEvents) {
      if (touchEvents.size() > 0) {

         for (final TouchEvent te : touchEvents) {
            Scheduler.get().scheduleDeferred(new Command() {

               @Override
               public void execute() {
                  _widget.onTouchEvent(te);
               }

            });

         }
      }
   }


   private TouchEvent mouseMoveHandler(final Vector2I pos) {
      //            log(LogLevel.InfoLevel, " onMouseMove");

      final ArrayList<Touch> touches = new ArrayList<Touch>();
      TouchEvent touchEvent = null;

      if (_mouseDown) {
         if (_keyDown) {
            touches.add(new Touch(new Vector2I(pos._x - 10, pos._y), _prevPos));
            touches.add(new Touch(pos, _prevPos));
            touches.add(new Touch(new Vector2I(pos._x + 10, pos._y), _prevPos));
         }
         else {
            touches.add(new Touch(pos, _prevPos));
         }
         touchEvent = TouchEvent.create(TouchEventType.Move, touches);
         _prevPos = pos;
      }

      return touchEvent;
   }


   private TouchEvent mouseDownHandler(final Vector2I pos) {
      //            log(LogLevel.InfoLevel, " onMouseDown");

      final ArrayList<Touch> touches = new ArrayList<Touch>();

      _mouseDown = true;
      _prevPos = pos;
      if (_keyDown) {
         touches.add(new Touch(new Vector2I(pos._x - 10, pos._y), _prevPos));
         touches.add(new Touch(pos, _prevPos));
         touches.add(new Touch(new Vector2I(pos._x + 10, pos._y), _prevPos));
      }
      else {
         touches.add(new Touch(pos, _prevPos));
      }

      return TouchEvent.create(TouchEventType.Down, touches);
   }


   private TouchEvent mouseUpHandler(final Vector2I pos) {
      //    log(LogLevel.InfoLevel, " onMouseUp");

      final ArrayList<Touch> touches = new ArrayList<Touch>();

      _mouseDown = false;
      if (_keyDown) {
         touches.add(new Touch(new Vector2I(pos._x - 10, pos._y), _prevPos));
         touches.add(new Touch(pos, _prevPos));
         touches.add(new Touch(new Vector2I(pos._x + 10, pos._y), _prevPos));
      }
      else {
         touches.add(new Touch(pos, _prevPos));
      }
      _prevPos = pos;

      return TouchEvent.create(TouchEventType.Up, touches);
   }


   private TouchEvent doubleClickHanler(final Vector2I pos) {
      //          log(LogLevel.InfoLevel, " onDoubleClick");

      final Touch touch = new Touch(pos, pos, (byte) 2);

      return TouchEvent.create(TouchEventType.Down, touch);
   }


   private void keyDownHandler(final Event event) {
      //               log(LogLevel.InfoLevel, " onKeyDown");

      // event.getShiftKey() does not work on Chrome
      if (event.getShiftKey() || (event.getKeyCode() == 16)) {
         _keyDown = true;
      }
   }


   private void keyUpHandler(final Event event) {
      //               log(LogLevel.InfoLevel, " onKeyUp");

      // event.getShiftKey() does not work on Chrome
      if (event.getShiftKey() || (event.getKeyCode() == 16)) {
         _keyDown = false;
      }
   }


   private void mouseWheelHandler(final int delta,
                                  final int x,
                                  final int y) {
      //      log(LogLevel.InfoLevel, " delta=" + delta + " x=" + x + " y=" + y);

      final ArrayList<Touch> beginTouches = new ArrayList<Touch>();
      final ArrayList<Touch> endTouches = new ArrayList<Touch>();

      Vector2I firstPointerPos = new Vector2I(x, y);
      Vector2I secondPointerPos = new Vector2I(x + 20, y + 20);

      beginTouches.add(new Touch(new Vector2I(firstPointerPos._x, firstPointerPos._y), new Vector2I(x, y)));
      beginTouches.add(new Touch(new Vector2I(secondPointerPos._x, secondPointerPos._y), new Vector2I(x, y)));

      _prevPos = firstPointerPos;
      final Vector2I prevSecondPos = secondPointerPos;

      firstPointerPos = new Vector2I(firstPointerPos._x - delta, firstPointerPos._y - delta);
      secondPointerPos = new Vector2I(secondPointerPos._x + delta, secondPointerPos._y + delta);

      endTouches.add(new Touch(firstPointerPos, _prevPos));
      endTouches.add(new Touch(secondPointerPos, prevSecondPos));

      final ArrayList<TouchEvent> tes = new ArrayList<TouchEvent>();

      tes.add(TouchEvent.create(TouchEventType.Down, beginTouches));
      tes.add(TouchEvent.create(TouchEventType.Move, endTouches));
      tes.add(TouchEvent.create(TouchEventType.Up, endTouches));

      eventHandler(tes);
   }


   public native void jsAddMouseWheelListener() /*-{
		//      debugger;
		var thisInstance = this;

		var canvas = this.@org.glob3.mobile.specific.MotionEventProcessor::_canvas;

		$wnd.g3mMouseWheelHandler = function(e) {
			// cross-browser wheel delta
			var e = $wnd.event || e; // old IE support
			var delta = (Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail))));
			thisInstance.@org.glob3.mobile.specific.MotionEventProcessor::mouseWheelHandler(III)(delta, e.clientX, e.clientY);
		};

		if (canvas) {

			if (canvas.addEventListener) {
				// IE9, Chrome, Safari, Opera
				canvas.addEventListener("mousewheel",
						$wnd.g3mMouseWheelHandler, false);
				// Firefox
				canvas.addEventListener("DOMMouseScroll",
						$wnd.g3mMouseWheelHandler, false);
			}
			// IE 6/7/8
			else {
				canvas.attachEvent("onmousewheel", $wnd.g3mMouseWheelHandler);
			}
		}

   }-*/;


   public void log(final LogLevel level,
                   final String msg) {
      if (ILogger.instance() != null) {
         switch (level) {
            case InfoLevel:
               ILogger.instance().logInfo(TAG + msg);
               break;
            case WarningLevel:
               ILogger.instance().logWarning(TAG + msg);
               break;
            case ErrorLevel:
               ILogger.instance().logError(TAG + msg);
               break;
            default:
               break;
         }
      }
      else {
         GWT.log(TAG + msg);
      }
   }

}
