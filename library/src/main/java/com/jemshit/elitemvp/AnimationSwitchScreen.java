package com.jemshit.elitemvp;

/**
 * Created by hoanghiep on 8/3/17.
 */

public enum AnimationSwitchScreen {
    FULL_OPEN(R.anim.enter_to_left, R.anim.exit_to_left, R.anim.enter_to_right, R.anim.exit_to_right),
    FULL_BACK(R.anim.enter_to_right, R.anim.exit_to_right, R.anim.enter_to_left, R.anim.exit_to_left),
    ONLY_OPEN(R.anim.enter_to_left, R.anim.exit_to_left, 0, 0),
    ONLY_EXIT(0, 0, R.anim.enter_to_right, R.anim.exit_to_right),
    EMPTY(0, 0, 0, 0),
    OPEN_TO_TOP_BACK_NOMAL(R.anim.enter_to_top, 0, R.anim.enter_to_right, R.anim.exit_to_right),
    HIDE_TO_TOP(R.anim.enter_to_top, R.anim.hide_to_top, R.anim.enter_to_right, R.anim.exit_to_right),
    OPEN_TO_TOP_BACK_TO_BOTTOM(R.anim.enter_to_top, R.anim.hide_to_top, R.anim.enter_show_to_bottom, R.anim.exit_hide_to_bottom);

    private int openEnter;
    private int openExit;
    private int closeEnter;
    private int closeExit;

    private AnimationSwitchScreen(int openEnter, int openExit, int closeEnter, int closeExit) {
        this.openEnter = openEnter;
        this.openExit = openExit;
        this.closeEnter = closeEnter;
        this.closeExit = closeExit;
    }

    public int getOpenEnter() {
        return openEnter;
    }

    public int getOpenExit() {
        return openExit;
    }

    public int getCloseEnter() {
        return closeEnter;
    }

    public int getCloseExit() {
        return closeExit;
    }
}
