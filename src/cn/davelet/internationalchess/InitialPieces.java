/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cn.davelet.internationalchess;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author DAVE
 */
public class InitialPieces {
    public InitialPieces(ImageView[] piece){
        /*
         * 初始化棋子数组 piece[0]到piece[15]是黑方，peice[16]到piece[31]是白方 从前向后分别是车 马 象 后 王;
         * 初始化兵升变备用棋子
         */
        for (int a = 1; a <= 32; a++) {
            piece[a - 1] = new ImageView(new Image(getClass().getResourceAsStream("JPG\\1" + a + ".jpg")));
        }
        for (int a = 32; a < 40; a++) {
            piece[a] = new ImageView(new Image(getClass().getResourceAsStream("JPG\\"+(a - 32) + ".jpg")));
            GridPane.setHalignment(piece[a], HPos.CENTER);
            GridPane.setValignment(piece[a], VPos.CENTER);
        }
    }

}
