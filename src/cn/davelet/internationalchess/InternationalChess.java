/*
 *
 */
package cn.davelet.internationalchess;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author DAVE
 */
public class InternationalChess extends Application {

    GridPane grid = new GridPane();
    VBox upTo = new VBox();//升变对象区域
    Text name = new Text("名称区域");
    ImageView[] piece = new ImageView[32];//棋子数组,在68行初始化
    Rectangle[][] bgRect = new Rectangle[8][8];//棋盘矩形，在220行初始化
    //被选中图片的坐标(数组位置)
    int selected = 32;
//    int selectedRow = 8;
//    int selectedColumn = 8;

    /**
     * 待解决问题：
     * 图片居中
     * 选中解释（已解决）
     * 中心区域的标注
     * 图片的放大（已解决）
     * 时间的记录
     * 步骤的保存和读取
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("欢迎来到International Chess");
        primaryStage.setResizable(false);//禁止改变大小
        Group root = new Group();
        Scene scene = new Scene(root, 880, 725);
        scene.setFill(Color.LIGHTGRAY);
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        /*
         * 初始化棋子数组 piece[0]到piece[15]是黑方，peice[16]到piece[31]是白方 从前向后分别是车 马 象 后 王
         */
        for (int a = 1; a <= 32; a++) {
            piece[a - 1] = new ImageView(new Image(getClass().getResourceAsStream("1" + a + ".jpg")));
        }


        HBox topButton = new HBox();
        topButton.setSpacing(70);
        topButton.setPadding(new Insets(20));
        pane.setTop(topButton);//边框布局顶部是hbox
        //开始按钮
        final Button start = new Button();
        start.setText("开始");
        start.setFont(new Font("bold", 20));
        start.setPrefWidth(100);
        //暂停按钮
        final Button pause = new Button();
        pause.setPrefWidth(100);
        pause.setText("暂停");
        pause.setFont(new Font("bold", 20));
        //结束按钮
        Button giveup = new Button("放弃");
        giveup.setPrefWidth(100);
        giveup.setFont(new Font("bold", 20));
        //退出按钮
        Button exit = new Button("退出");
        exit.setFont(new Font(20));
        topButton.getChildren().addAll(start, pause, giveup, exit);

        //三个按钮的事件
        start.setOnAction(new EventHandler<ActionEvent>() {//开始

            public void handle(ActionEvent event) {
                start.setText("重新开始");
                //为黑方布棋
                for (int colunm = 0; colunm < 8; colunm++) {
                    grid.add(piece[colunm], colunm, 0);
                }
                for (int column = 0; column < 8; column++) {
                    grid.add(piece[8 + column], column, 1);
                }
                //为白方布棋
                for (int colunm = 0; colunm < 8; colunm++) {
                    grid.add(piece[16 + colunm], colunm, 6);
                }
                for (int column = 0; column < 8; column++) {
                    grid.add(piece[24 + column], column, 7);
                }
            }
        });
        pause.setOnAction(new EventHandler<ActionEvent>() {//暂停

            public void handle(ActionEvent t) {
                if ("暂停".equals(pause.getText())) {
                    grid.setVisible(false);
                    pause.setText("继续");
                } else {
                    grid.setVisible(true);
                    pause.setText("暂停");
                }
            }
        });
        giveup.setOnAction(new EventHandler<ActionEvent>() {//结束

            public void handle(ActionEvent t) {
                ActionHandler.giveup();
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                ActionHandler.exit();
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(6));
        pane.setRight(vbox);//边框布局右部
        HBox namebox = new HBox();
        namebox.setAlignment(Pos.CENTER);
        namebox.getChildren().add(name);
        name.setFont(new Font(28));
        name.setStyle("-fx-text-fill: #006464;");//设置字体颜色
        Text white = new Text("白方步数：");
        white.setFont(new Font(20));
        Text black = new Text("黑方步数：");
        black.setFont(new Font(20));
        ScrollPane sp1 = new ScrollPane();
        sp1.setFitToWidth(true);
        ScrollPane sp2 = new ScrollPane();
        sp2.setFitToWidth(true);
        TextArea recordWhite = new TextArea();
        recordWhite.setEditable(false);
        recordWhite.setPrefRowCount(15);
        recordWhite.setPrefWidth(160);
        TextArea recordBlack = new TextArea();
        recordBlack.setEditable(false);
        recordBlack.setPrefRowCount(15);
        recordBlack.setPrefWidth(160);
        sp1.setContent(recordWhite);
        sp2.setContent(recordBlack);
        //三个特殊按钮
        Text special = new Text("使用特殊技能");
        special.setFont(new Font(20));
        Button kingGharry = new Button("王车易位");
        kingGharry.setAlignment(Pos.CENTER);
        kingGharry.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        kingGharry.setPrefWidth(160);
        Button solderUp = new Button("兵升变");
        solderUp.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        solderUp.setPrefWidth(160);
        Button hitPassSolder = new Button("吃过路兵");
        hitPassSolder.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        hitPassSolder.setPrefWidth(160);
        //将它们加入盒子
        VBox skill = new VBox();
        skill.getChildren().addAll(special, kingGharry, hitPassSolder, solderUp);
        //升变对象
        upTo.setDisable(true);
        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("后");
        rb1.setFont(new Font("bold", 20));
        rb1.setToggleGroup(group);
        RadioButton rb2 = new RadioButton("车");
        rb2.setFont(new Font("bold", 20));
        rb2.setToggleGroup(group);
        RadioButton rb3 = new RadioButton("象");
        rb3.setFont(new Font("bold", 20));
        rb3.setToggleGroup(group);
        RadioButton rb4 = new RadioButton("马");
        rb4.setFont(new Font("bold", 20));
        rb4.setToggleGroup(group);
        upTo.getChildren().addAll(new Separator(), new Separator(), new Text("兵升变对象："), rb1, rb2, rb3, rb4);
        upTo.setStyle("-fx-font: 20 arial;");
        vbox.getChildren().addAll(namebox, white, sp1, black, sp2, skill, upTo);

        //初始化bgRect数组
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if ((column + row) % 2 == 0) {
                    bgRect[column][row] = new Rectangle(80, 80, Color.YELLOW);
                } else {
                    bgRect[column][row] = new Rectangle(80, 80, Color.GREEN);
                }
            }
        }

//        使用GridPane作为棋盘，该窗格可以进行栈覆盖
        grid.setPadding(new Insets(0, 0, 0, 10));
        //        the 3rd para of the rect sets the rectangle's color
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                grid.add(bgRect[column][row], column, row);
            }
        }
        pane.setCenter(grid);//边框布局中心是棋盘

        root.getChildren().add(pane);
        primaryStage.setScene(scene);
        primaryStage.show();


        /**
         * ********************************************************************************************************************
         * 以下是所有图片和棋盘的处理事件 111首先是对图片的放大和恢复 222然后是所有棋子的单击事件 333接着是棋盘面板的单击事件
         * 444特殊按钮的单击事件
         * *********************************************************************************************************************
         */

        /*
         * 为所有图片加添放大事件
         */
        for (int a = 0; a < 32; a++) {
            piece[a].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

                public void handle(MouseEvent t) {
                    ((ImageView) t.getSource()).setScaleX(1.25);
                    ((ImageView) t.getSource()).setScaleY(1.25);
                }
            });
            piece[a].addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

                public void handle(MouseEvent t) {
                    ((ImageView) t.getSource()).setScaleX(1);
                    ((ImageView) t.getSource()).setScaleY(1);
                }
            });
        }

        /*
         * 为所有图片添加单击事件
         *
         */
        //为黑方步兵处理
        piece[8].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 8;
                if (r == 1) {//如果兵没动过，可以走两步
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[9].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 9;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[10].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[11].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[12].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[13].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[14].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        piece[15].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                selected = 10;
                if (r == 1) {//如果兵没动过，可以走两步
//                        GridPane.clearConstraints((ImageView)t.getSource());
                } else if (r < 7) {//否则只能走一步
                } else if (r == 7) {//兵升变
                }
            }
        });
        //为白方步兵处理
        piece[16].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 16;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[17].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 17;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[18].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 18;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[19].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 19;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[20].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 20;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[21].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 21;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[22].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 22;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        piece[23].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                selected = 23;
                name.setText("白方:兵Pawn");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                if (r == 6) {//如果兵没动过，可以走两步
                } else if (r > 0) {//否则只能走一步
                } else if (r == 0) {//兵升变
                }
            }
        });
        //黑方车的处理
        piece[0].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:车Rook");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                selected = 0;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[7].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:车Rook");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                selected = 7;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //白方车的处理
        piece[24].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:车Rook");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                selected = 24;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[31].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:车Rook");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 31;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //黑方马
        piece[1].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:马Knight");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 1;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[6].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:马Knight");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 6;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //白方马
        piece[25].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:马Knight");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 25;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[30].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:马Knight");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 30;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //黑方象
        piece[2].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:白象Bishop");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 2;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[5].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:黑象Bishop");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 5;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //白方象
        piece[26].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:黑象Bishop");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 26;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        piece[29].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:白象Bishop");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 29;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //黑方后
        piece[3].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:后Queen");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 3;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //白方后
        piece[27].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:后Queen");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 27;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //黑方王
        piece[4].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("黑方:王King");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 4;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        //白方王
        piece[28].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                name.setText("白方:王King");//在解释区显示
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                selected = 28;
//                selectedColumn = c;
//                selectedRow = r;
            }
        });
        /*
         * 棋盘面板的单击事件
         */
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                bgRect[column][row].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent t) {
                        if (selected < 32//Column < 8 || selectedRow < 8
                                ) {
                            Rectangle s = (Rectangle) t.getSource();
                            int c = GridPane.getColumnIndex(s);
                            int r = GridPane.getRowIndex(s);
                            GridPane.clearConstraints(piece[selected]);
                            grid.add(piece[selected], c, r);
                        }
                    }
                });
            }
        }
        //给特殊按钮增加事件
        kingGharry.setOnAction(new EventHandler<ActionEvent>() {//王车易位事件

            public void handle(ActionEvent t) {
            }
        });
        solderUp.setOnAction(new EventHandler<ActionEvent>() {//兵升变事件

            public void handle(ActionEvent t) {
            }
        });
        hitPassSolder.setOnAction(new EventHandler<ActionEvent>() {//吃过路兵事件

            public void handle(ActionEvent t) {
            }
        });
    }
}
