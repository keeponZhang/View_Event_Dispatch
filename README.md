# view的分发
### 要点
* 1.dispatchTransformedTouchEvent 会递归调用，调用者必须是ViewGroup
  如果child不为null，则会调用该child的dispatchTouchEvent，一直递归下去。
  假如现在是分发到了触摸到的最里层view的父亲viewGroup中，分两种情况
  1.最后的child是view,这时会调到view的dispatchTouchEvent，然后是onTouchEvent方法（tips1)
  2.最后的child是viewGroup，这时会调到viewGroup的dispatchTouchEvent，然后child为null，
  接着dispatchTransformedTouchEvent，然后调用viewGroup父类view的dispatchTouchEvent，接着
  调用viewGroup的onTouchEvent方法（多态）(tips2) 
  * 总结：事件从顶层view的的dispatchTouchEvent一层一层传到触摸到的最底层的view(view或者viewGroup)的dispatchTouchEvent，如果是view会传到onTouchEvent,如果是viewgroup,首先会传到viewgroup的dispatchTouchEvent，在通过viewgroup的dispatchTransformedTouchEvent调用super.dispatchTouchEvent,从而传到viewgroup的onTouchEvent(前提是没有重写dispatchTouchEvent方法)
  ```java
  private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
            View child, int desiredPointerIdBits) {
        final boolean handled;
        if (child == null) {
            //tips3:能走到这里，必定是viewGroup,然后调用viewGroup父类view的dispatchTouchEvent，接着
           // 调用viewGroup的onTouchEvent方法（多态）
            handled = super.dispatchTouchEvent(transformedEvent);
        } else {
            final float offsetX = mScrollX - child.mLeft;
            final float offsetY = mScrollY - child.mTop;
            transformedEvent.offsetLocation(offsetX, offsetY);
            if (! child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }
            // 如果child不为null，则会调用该child的dispatchTouchEvent，一直递归下去 
            //如果这时走到最后一层了
            //tips1:child为view，会调到view的dispatchTouchEvent，然后是onTouchEvent方法
            //tips2：child为viewGroup，然后走到viewGroup的dispatchTouchEvent,继而走到viewgroup的dispatchTransformedTouchEvent（该方法，此时child为空），走到tips3，最后走到该child（实际为viewGroup）的onTouchEvent
            handled = child.dispatchTouchEvent(transformedEvent);
        }
        // Done.
        transformedEvent.recycle();
        return handled;
    }`
  ```     
* 2.加入的最后的child的dispatchTouchEvent返回false（直接调super一样(一般的view，继承自view，没有设置监听listener，
  并且也不是CLICKABLE，dispatchTouchEvent调用的onTouchEvent返回false，从而dispatchTouchEvent返回false)），这时倒数第二层的ViewGroup的
  dispatchTransformedTouchEvent会返回false，if里面的代码不会走，mFirstTouchTarget会等于null，这时代码走下去会继续调用
  dispatchTransformedTouchEvent,此时传入的child是null,所以这时会调用viewGroup父类view的dispatchTouchEvent，接着
  调用viewGroup的onTouchEvent方法（多态）（接着会回溯上去，如果都是调super实现的话，最终会到顶层viewDevorView的onTouchEvent方法，再到Activity的onTouchEvent方法。（此时down事件走完）
  * 结论1：dispatchTouchEvent 和 onTouchEvent return false的时候事件都回传给父控件的onTouchEvent处理。
     ```java
          public boolean dispatchTouchEvent(MotionEvent ev) {
             boolean handled = false;
             if (onFilterTouchEventForSecurity(ev)) {
             final boolean intercepted;
             if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
             }else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                 intercepted = true;
             }

            // Check for cancelation.
            final boolean canceled = resetCancelNextUpFlag(this)
                    || actionMasked == MotionEvent.ACTION_CANCEL;
            if (!canceled && !intercepted) {
                if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                    final int childrenCount = mChildrenCount;
                    if (newTouchTarget == null && childrenCount != 0) {
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);
                        // Find a child that can receive the event.
                        // Scan children from front to back.
                        final ArrayList<View> preorderedList = buildOrderedChildList();
                        final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                        final View[] children = mChildren;
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            final int childIndex = customOrder
                                    ? getChildDrawingOrder(childrenCount, i) : i;
                            final View child = (preorderedList == null)
                                    ? children[childIndex] : preorderedList.get(childIndex);
                          
                            if (!canViewReceivePointerEvents(child)
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                                ev.setTargetAccessibilityFocus(false);
                                continue;
                            }
                            resetCancelNextUpFlag(child);
                            //加入的最后的child的dispatchTouchEvent返回false（直接调super一样），这时倒数第二层的ViewGroup的
                            //dispatchTransformedTouchEvent会返回false,mFirstTouchTarget为null,此时走到tips4
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                if (preorderedList != null) {
                                    // childIndex points into presorted list, find original index
                                    for (int j = 0; j < childrenCount; j++) {
                                        if (children[childIndex] == mChildren[j]) {
                                            mLastTouchDownIndex = j;
                                            break;
                                        }
                                    }
                                } else {
                                    mLastTouchDownIndex = childIndex;
                                }
                                newTouchTarget = mFirstTouchTarget= addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }
                        }
                        if (preorderedList != null) preorderedList.clear();
                    }

                    if (newTouchTarget == null && mFirstTouchTarget != null) {
                        // Did not find a child to receive the event.
                        // Assign the pointer to the least recently added target.
                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }

            // Dispatch to touch targets.
            //mFirstTouchTarget == null的可能性
            //1.子控件不消费，dispatchOnTouchEvent返回false
            //2,子控件返回了true，down事件是mFirstTouchTarget不为null，但是到了move事件时，父容器拦截了，在tips4.1由重新置为了null
            if (mFirstTouchTarget == null) {
                // No touch targets so treat this as an ordinary view.
                //tips4:mFirstTouchTarget为null,此时又再一次调用viewGroup的dispatchTransformedTouchEvent，child为null，接着走的是要点1的tips3，调用viewGroup的onTouchEvent方法（多态）
                //dispatchTouchEvent 和 onTouchEvent return false的时候事件都回传给父控件的onTouchEvent处理。
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
            } else {
                // Dispatch to touch targets, excluding the new touch target if we already
                // dispatched to it.  Cancel touch targets if necessary.
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else {
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                || intercepted;
                        if (dispatchTransformedTouchEvent(ev, cancelChild,
                                target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        if (cancelChild) {
                            //tips4.1
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }

            // Update list of touch targets for pointer up or cancel, if needed.
            if (canceled
                    || actionMasked == MotionEvent.ACTION_UP
                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                resetTouchState();
            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
                final int actionIndex = ev.getActionIndex();
                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
                removePointersFromTouchTargets(idBitsToRemove);
            }
        }

        if (!handled && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
        }
        return handled;
    }
     ```
* 3.加入的最后的child的dispatchTouchEvent返回true，这时倒数第二层的ViewGroup的
    dispatchTransformedTouchEvent会返回true，if里面的代码会走，mFirstTouchTarget就不为null，这时代码走下去，handled为true，
    接着会回溯上去（倒数第二层的mFirstTouchTarget为倒数第一层的view，倒数第三层的mFirstTouchTarget为倒数第二层的view（倒数第三层handled也为true））
  * 结论1：dispatchTouchEvent 和 onTouchEvent 一旦return
    true,事件就停止传递了（到达终点）（没有谁能再收到这个事件）。
    看下图中只要return true事件就没再继续传下去了，对于return
    true我们经常说事件被消费了，消费了的意思就是事件走到这里就是终点，不会往下传，没有谁能再收到这个事件了（此时down事件不会传到父容器的onTouchEvent中）,接下来的事件会继续走到该viewGroup的dispatchTouchEvent方法,传到tips6，接着传给下一层view（mFirstTarget)处理
    （这里有一个注意点，同一个view,onTouchEvent返回true,如果dispatchTouchEvent方法返回调用的是super.dispatchTouchEvent,这dispatchTouchEvent也会返回true,其他情况则不一定）
     ```java
          public boolean dispatchTouchEvent(MotionEvent ev) {
             boolean handled = false;
             if (onFilterTouchEventForSecurity(ev)) {
             final boolean intercepted;
             if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
             }else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                 intercepted = true;
             }

            // Check for cancelation.
            final boolean canceled = resetCancelNextUpFlag(this)
                    || actionMasked == MotionEvent.ACTION_CANCEL;
            if (!canceled && !intercepted) {
                if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                    final int childrenCount = mChildrenCount;
                    if (newTouchTarget == null && childrenCount != 0) {
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);
                        // Find a child that can receive the event.
                        // Scan children from front to back.
                        final ArrayList<View> preorderedList = buildOrderedChildList();
                        final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                        final View[] children = mChildren;
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            final int childIndex = customOrder
                                    ? getChildDrawingOrder(childrenCount, i) : i;
                            final View child = (preorderedList == null)
                                    ? children[childIndex] : preorderedList.get(childIndex);
                          
                            if (!canViewReceivePointerEvents(child)
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                                ev.setTargetAccessibilityFocus(false);
                                continue;
                            }
                            resetCancelNextUpFlag(child);
                            //加入的最后的child的dispatchTouchEvent返回true，这时倒数第二层的ViewGroup的
                             //dispatchTransformedTouchEvent会返回true，mFirstTouchTarget不为null，走到tips5
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                if (preorderedList != null) {
                                    // childIndex points into presorted list, find original index
                                    for (int j = 0; j < childrenCount; j++) {
                                        if (children[childIndex] == mChildren[j]) {
                                            mLastTouchDownIndex = j;
                                            break;
                                        }
                                    }
                                } else {
                                    mLastTouchDownIndex = childIndex;
                                }
                                newTouchTarget = mFirstTouchTarget= addTouchTarget(child, idBitsToAssign);
                                //tips4.1
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }
                        }
                        if (preorderedList != null) preorderedList.clear();
                    }

                    if (newTouchTarget == null && mFirstTouchTarget != null) {
                        // Did not find a child to receive the event.
                        // Assign the pointer to the least recently added target.
                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }

            // mFirstTouchTarget其实是在Action_Down是确定的
           //tips5.1
            //mFirstTouchTarget == null的可能性
            //1.子控件不消费，dispatchOnTouchEvent返回false
            //2,子控件返回了true，down事件是mFirstTouchTarget不为null，但是到了move事件时，父容器拦截了，在tips4.1由重新置为了null
            if (mFirstTouchTarget == null) {
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
            } else {
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
                    //tips5:情况1此时是action_down，并且子控件返回true，alreadyDispatchedToNewTouchTarget为true，target == newTouchTarget，所以此时handled为true
                    //接着会回溯上去（倒数第二层的mFirstTouchTarget为倒数第一层的view，倒数第三层的mFirstTouchTarget为倒数第二层的view（倒数第三层handled也为true））
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else {
                        //情况2：此时如果是action_move或者action_up,TouchTarget newTouchTarget = null,alreadyDispatchedToNewTouchTarget = false;
                        //target=mFirstTouchTarget,可知如果父容器拦截了action_move或者action_up事件，intercepted为true，子控件会收到cancel事件,否则会继续通过dispatchTransformedTouchEvent方法，把事件传给子控件，一层一层传下去
                        //tips6
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                || intercepted;
                        //这里有一点注意，如果父容器没拦截down事件，拦截up事件，如果子控件对cancel事件消费了，handle仍然会被置为true
                        if (dispatchTransformedTouchEvent(ev, cancelChild,
                                target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        //父容器如果拦截，mFirstTouchTarget会置为null，下次进来走tips4，其实就是会走到viewGroup的onTouchEvent方法
                        if (cancelChild) {
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }

            // Update list of touch targets for pointer up or cancel, if needed.
            if (canceled
                    || actionMasked == MotionEvent.ACTION_UP
                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                resetTouchState();
            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
                final int actionIndex = ev.getActionIndex();
                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
                removePointersFromTouchTargets(idBitsToRemove);
            }
        }

        if (!handled && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
        }
        return handled;
    }
     ```     
### 总结
* 1.某个View 一旦决定拦截(onInterceptTouchEvent 返回true)，那么这
  一个事件序列都只能由它来处理〈如果事件序列能够传递给它的话），
  并且它的onInterceptTouchEvent不会再被调用（因为此时拦截了down事件，mFirstTarget为null）
* 2.加入的最后的child的dispatchTouchEvent返回true,一般来说，父容器就不走onTouchEvent?有没办法实现走父容器的onTouchEvent呢?
  有，在父容器的onInterceptTouchEvent方法返回true.此时newTouchTarget =
  null,alreadyDispatchedToNewTouchTarget =
  false;因为父容器拦截，会给子view（mFirstTarget）分发一个cancel事件，如果是只是个点击事件，其实该viewgroup并不会走onTouchEvent；如果中间还有move事件，因为第一个move事件，mFirsTarget
  就被置为null，接下来的事件会分发给父容器的onTouchEvent. 
  
* 3.父容器的onInterceptTouchEvent返回true，表示父容器拦截了事件，子view就再也收不到事件，有没办法在该情况下，子view也能受到事件呢？
  有的，但是有一个前提，父容器不能拦截actionDown事件，因为父容器拦截actionDown后，事件走不到子view中(因为你在action_down事件前设置的不允许父容器拦截标志位，会在actiondown中被重置）
  所以一般做法是在父容器onInterceptTouchEvent方法中，如果actionDown事件返回false，其他事件返回true。然后在子view的dispatchTouchEvent的actionDown调用
  getParent().requestDisallowInterceptTouchEvent(true)，true表示不允许父控件拦截。
  (注意，如果子view消费了down事件，并且子view没有调用getParent().requestDisallowInterceptTouchEvent(true)方法，子view不会收到ove
  和up事件，会收到cancel事件)
* 4.view事件分发给自己的onTouchEvent 处理呢，那只能return
  super.dispatchTouchEvent,View类的dispatchTouchEvent（）方法默认实现就是能帮你调用View自己的onTouchEvent方法的。
  然后ViewGroup怎样通过dispatchTouchEvent方法能把事件分发到自己的onTouchEvent处理呢，return
  true和false
  都不行，那么只能通过Interceptor把事件拦截下来给自己的onTouchEvent，
  所以ViewGroup
  dispatchTouchEvent方法的super默认实现就是去调用onInterceptTouchEvent
* 5.ViewGroup和View的onTouchEvent方法是做事件处理的，那么这个事件只能有两个处理:1.自己消费掉，事件终结，不再传给谁----->return
  true; 2.继续从下往上传，不消费事件，让父View也能收到到这个事件----->return false;View的默认实现是不消费的。所以super==false。
* 6.如果down事件没有控件消费，那么会回溯掉最顶层view
  Decorview，DecorView在
  onTouchEvent收到down事件后，默认也是不消费的，接着就会传递up事件，因为最顶层view的mFirstTouchTarget为null，up事件会传递Decorview的onTouchEvent,默认同样是不消费，到此事件传递结束。
* 7.除了down事件的其他事件，只有当前viewGroup的mFirstTouchTarget不为null，并且子view没有在down事件调用getParent().requestDisallowInterceptTouchEvent(true)，
     才会调用onInterceptTouchEvent方法(即是调用onInterceptTouchEvent方法走的3个条件，down事件或者mFirstTouchTarget != null，即actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null,
     还有一种就是  子view没有在down事件中调用 getParent().requestDisallowInterceptTouchEvent(true);
* 8 .clearTouchTargets先释放当前的target到复用池，如果当前target的nextTarget不为null，则把nextTarget也释放到复用池
* 9.onInterceptTouchEvent的down返回false,move和up拦截，如果子view消费了事件，
  mFistTouchTarget不为null，dispatchTransformedTouchEvent(ev, cancelChild, target.child, target.pointerIdBits))
  会给消费该事件的子view传递一个action_cancel事件，之后的事件就不会再传给该子view
* 10.如果View不消耗除ACTION_DOWN以外的其它事件，那么这个点击事件不会消失，此时父元素的onTouchEvent()并不会调用，
  并且当前View可以持续收到后续的事件，最终这些消失的点击事件会传递给Activity处理。(父容器不拦截的话,不会发出cancel事件，mFirstTouchTarget不会置为null，接下来的触摸事件还会传到子view)









