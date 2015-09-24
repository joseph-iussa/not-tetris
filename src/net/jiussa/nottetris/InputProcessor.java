package net.jiussa.nottetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
    private Game game;

    public InputProcessor(Game game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = false;
        if (keycode == Keys.A) {
            game.startMovingPieceLeft();
            handled = true;
        }
        if (keycode == Keys.D) {
            game.startMovingPieceRight();
            handled = true;
        }
        if (keycode == Keys.S) {
            game.startMovingPieceDown();
            handled = true;
        }
        if (keycode == Keys.W) {
            game.hardDropPiece(game.activePiece);
            handled = true;
        }
        if (keycode == Keys.LEFT) {
            game.rotatePieceACW();
            handled = true;
        }
        if (keycode == Keys.RIGHT) {
            game.rotatePieceCW();
            handled = true;
        }
        return handled;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.ESCAPE) Gdx.app.exit();

        if (keycode == Keys.A) {
            game.stopMovingPieceLeft();
        }

        if (keycode == Keys.D) {
            game.stopMovingPieceRight();
        }

        if (keycode == Keys.S) {
            game.stopMovingPieceDown();
        }

        return true;
    }
}