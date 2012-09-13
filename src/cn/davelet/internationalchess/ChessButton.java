/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cn.davelet.internationalchess;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

/**
 *
 * @author DAVE
 */
public class ChessButton extends Button{
    public ChessButton(String name){
        this.setPrefWidth(100);
        this.setText(name);
        this.setFont(new Font("bold", 20));
    }

}
