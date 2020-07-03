package com.kegy.mvps.presenter;

import static com.google.common.collect.ImmutableList.of;
import static com.kegy.mvps.presenter.Presenter.PresenterState.BIND;
import static com.kegy.mvps.presenter.Presenter.PresenterState.CREATE;
import static com.kegy.mvps.presenter.Presenter.PresenterState.DESTROY;
import static com.kegy.mvps.presenter.Presenter.PresenterState.INIT;
import static com.kegy.mvps.presenter.Presenter.PresenterState.NONE;
import static com.kegy.mvps.presenter.Presenter.PresenterState.UNBIND;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable;
import com.kegy.mvps.presenter.Presenter.PresenterState;

final class PresenterStateMachine {

  private final
  ImmutableTable<PresenterState, PresenterState, List<PresenterState>> STATE_TABLE =
      ImmutableTable.<Presenter.PresenterState, PresenterState, List<PresenterState>>builder()
          .put(INIT, CREATE, of(CREATE))
          .put(CREATE, BIND, of(BIND))
          .put(BIND, BIND, of(UNBIND, BIND))
          .put(BIND, UNBIND, of(UNBIND))
          .put(UNBIND, DESTROY, of(DESTROY))
          .put(BIND, DESTROY, of(UNBIND, DESTROY))
          .put(INIT, DESTROY, of(NONE))
          .build();

  void moveToState(PresenterState from, PresenterState to,
      PresenterState.PresenterStateCallBack callBack) {
    List<PresenterState> states = STATE_TABLE.get(from, to);
    Preconditions.checkState(states != null, "不能从 " + from + " 跳到 " + to);
    for (PresenterState state : states) {
      callBack.onState(state);
    }
  }

}
