/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cn.davelet.internationalchess;

import javafx.event.ActionEvent;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author DAVE
 */
public class PieceAcitons extends ActionEvent {

    public void blackPawnAction(ImageView[] piece, int i, int c, int r, int selected, GridPane grid, boolean control, boolean isThere[][], Text name) {

        if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
            System.out.println("当前选中的是白子。。。。" + selected);
            grid.getChildren().remove(piece[i]);
            System.out.println("黑子被移除");
            try {
                grid.add(piece[selected], c, r);
                System.out.println("bai zi bei tian jia yu ci" + selected);
            } catch (Exception e) {
                System.out.println("The White 已经成功消灭对方the black一子！" + e);
            } finally {
                System.out.println("well done!");
                selected = 40;
                control = false;
                InternationalChess.selected = selected;
                InternationalChess.name = name;
                InternationalChess.isThere = isThere;
                InternationalChess.grid = grid;
                InternationalChess.control = control;
                System.err.println("At now no piece has been elected and the BLACKs trun");
            }
        } else {//选中棋子
            name.setText("黑方:兵Pawn");//在解释区显示
            if (control == false) {
                selected = i;
                isThere[c][r] = false;
                InternationalChess.selected = selected;
                InternationalChess.name = name;
                InternationalChess.isThere = isThere;
                InternationalChess.grid = grid;
                InternationalChess.control = control;
                for (int a = 0; a < 16; a++) {
                    if (a != i) {
                        piece[a].setDisable(true);
                        piece[a].setEffect(new Bloom());
                    }
                }
                for (int a = 16; a < 32; a++) {
                    piece[a].setDisable(false);
                    piece[a].setEffect(null);
                }
                if (r == 1) {//如果兵没动过，可以走两步
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        }
    }

    public void whitePawnAction(ImageView[] piece, int i, int c, int r, int selected, GridPane grid, boolean control, boolean isThere[][], Text name) {

        if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
            grid.getChildren().remove(piece[i]);
            try {
                grid.add(piece[selected], c, r);
            } catch (Exception e) {
                System.out.println("The blck 已经成功消灭对方 the white 一子！");
            } finally {
                System.out.println("well done!");
                selected = 40;
                control = true;
                InternationalChess.selected = selected;
                InternationalChess.name = name;
                InternationalChess.isThere = isThere;
                InternationalChess.grid = grid;
                InternationalChess.control = control;
                System.err.println("NO elected and white turn");
            }
        } else {//选中棋子
            name.setText("白方:兵Pawn");//在解释区显示
            if (control == true) {
                System.out.println("现在由白方控制进攻");
                selected = i;
//                s = 100;
                System.out.println("现在被选择的是" + i);
                InternationalChess.selected = selected;
                InternationalChess.name = name;
                InternationalChess.isThere = isThere;
                InternationalChess.grid = grid;
                InternationalChess.control = control;
                for (int a = 16; a < 32; a++) {
                    if (a != i) {
                        piece[a].setDisable(true);
                        piece[a].setEffect(new Bloom());
                    }
                }
                for (int a = 0; a < 16; a++) {
                    piece[a].setDisable(false);
                    piece[a].setEffect(null);
                }
                if (r == 6) {//如果兵没动过，可以走两步
                    System.out.println("现在可以走2步");
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        }
    }
}
