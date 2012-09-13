/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cn.davelet.internationalchess;

import javafx.scene.image.ImageView;

/**
 *
 * @author DAVE
 */
public class PieceFind {

    public int pieceFind(ImageView[] ima, ImageView key) {
        int a = 0;
        while (ima[a] != key) {
            a++;
        }
        return a;
    }
}
