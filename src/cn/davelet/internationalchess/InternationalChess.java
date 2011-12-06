/*
 *
 */
package cn.davelet.internationalchess;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
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
    ImageView[] piece = new ImageView[40];//棋子数组,在68行初始化
    Rectangle[][] bgRect = new Rectangle[8][8];//棋盘矩形，在220行初始化
    //被选中图片的坐标(数组位置)
    static int selected = 40;
    //true表示白方可以移动棋子
    boolean control = true;
    //判断棋盘上是否有棋子,true为有棋子
    boolean isThere[][] = new boolean[8][8];
    //王车易位前已移动判定。数组分别代表白车、白王、白车，黑车、黑王、黑车
    boolean moved[] = new boolean[6];
    //特殊技能区域
    Accordion sSkill;
    TitledPane sUp;//升变窗格
    ToggleGroup group;

    /**
     * 待解决问题：
     * 图片居中（已实现）
     * 选中解释（已解决）
     * 棋子的移动（已解决）
     * 棋子的吃（已解决）
     * 交替走棋的严格（已实现）
     * 中心区域的标注
     * 图片的放大（已解决）
     * 摸棋无悔（已实现）
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
        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        /*
         * 初始化棋子数组 piece[0]到piece[15]是黑方，peice[16]到piece[31]是白方 从前向后分别是车 马 象 后
         * 王; 初始化兵升变备用棋子
         */
        for (int a = 1; a <= 32; a++) {
            piece[a - 1] = new ImageView(new Image(getClass().getResourceAsStream("1" + a + ".jpg")));
        }
        for (int a = 32; a < 40; a++) {
            piece[a] = new ImageView(new Image(getClass().getResourceAsStream((a - 32) + ".jpg")));
            GridPane.setHalignment(piece[a], HPos.CENTER);
            GridPane.setValignment(piece[a], VPos.CENTER);
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
        final Button giveup = new Button("放弃");
        giveup.setPrefWidth(100);
        giveup.setFont(new Font("bold", 20));
        //退出按钮
        Button exit = new Button("退出");
        exit.setFont(new Font(20));
        //Help
        final Button help = new Button("玩前必读");
        help.setStyle("-fx-font: 20 arial; -fx-base: #b6e000;");
        topButton.getChildren().addAll(start, pause, giveup, exit, help);

        //5个按钮的事件
        start.setOnAction(new EventHandler<ActionEvent>() {//开始

            public void handle(ActionEvent event) {
                grid.setEffect(null);//去除棋盘效果
                grid.setDisable(false);//棋盘可用
                control = true;//白方先行
                for (int m = 0; m < 6; m++) {//王车均未移动过
                    moved[m] = false;
                }
                grid.getChildren().removeAll(piece);//清理棋子
                for (int c = 0; c < 8; c++) {
                    for (int r = 0; r < 8; r++) {
                        isThere[c][r] = false;//清理棋盘
                    }
                }
                start.setText("重新开始");
                for (int a = 0; a < 40; a++) {
                    piece[a].setDisable(false);//棋子可用
                    piece[a].setEffect(null);//去除棋子效果
                }
                //为黑方布棋
                for (int column = 0; column < 8; column++) {
                    try {
                        grid.add(piece[column], column, 0);
                    } catch (Exception e) {
                    } finally {
                        isThere[column][0] = true;
                        GridPane.setValignment(piece[column], VPos.CENTER);
                        GridPane.setHalignment(piece[column], HPos.CENTER);
                    }
                }
                for (int column = 0; column < 8; column++) {
                    try {
                        grid.add(piece[8 + column], column, 1);
                    } catch (Exception e) {
                    } finally {
                        isThere[column][1] = true;
                        GridPane.setValignment(piece[8 + column], VPos.CENTER);
                        GridPane.setHalignment(piece[8 + column], HPos.CENTER);
                    }
                }
                //为白方布棋
                for (int column = 0; column < 8; column++) {
                    try {
                        grid.add(piece[16 + column], column, 6);
                    } catch (Exception e) {
                    } finally {
                        isThere[column][6] = true;
                        GridPane.setValignment(piece[16 + column], VPos.CENTER);
                        GridPane.setHalignment(piece[16 + column], HPos.CENTER);
                    }
                }
                for (int column = 0; column < 8; column++) {
                    try {
                        grid.add(piece[24 + column], column, 7);
                    } catch (Exception e) {
                    } finally {
                        isThere[column][7] = true;
                        GridPane.setValignment(piece[24 + column], VPos.CENTER);
                        GridPane.setHalignment(piece[24 + column], HPos.CENTER);
                    }
                }
                sSkill.setDisable(false);//特殊技能可用
            }
        });
        pause.setOnAction(new EventHandler<ActionEvent>() {//暂停

            public void handle(ActionEvent t) {
                if ("暂停".equals(pause.getText())) {
                    grid.setVisible(false);
                    sSkill.setDisable(true);
                    help.setDisable(true);
                    pause.setText("继续");
                } else {
                    grid.setVisible(true);
                    sSkill.setDisable(false);
                    help.setDisable(false);
                    pause.setText("暂停");
                }
            }
        });
        giveup.setOnAction(new EventHandler<ActionEvent>() {//结束,清空棋盘

            public void handle(ActionEvent t) {
                grid.setDisable(true);
                sSkill.setDisable(true);
                help.setDisable(true);
                grid.setEffect(new Bloom());
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        help.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                start.setDisable(true);
                pause.setDisable(true);
                giveup.setDisable(true);
                sSkill.setDisable(true);
                final Button tip = new Button();
                tip.setPrefHeight(grid.getHeight());
                tip.setPrefWidth(grid.getWidth());
                grid.add(tip, 0, 0, 8, 8);
                help.setDisable(true);
                String s = "玩前必读：\n"
                        + "1。点击<开始>按钮完成布棋；\n"
                        + "2。要实现<王车易位>，不要点击棋子，直接点击易位按钮（需要选择长或短）；\n"
                        + "3。要实现<吃过路兵>，需要先选择棋子，再点击按钮（需要选择左或右）；\n"
                        + "4。要实现<兵升变>，在兵走到对方底线后不要再点击棋子，直接点击要升变为的目标；\n"
                        + "5。不限制棋子移动，请自觉主动遵守规则；"
                        + "\n\n点击这里退出，开始游戏 ^_^o~ 努力！";
                tip.setText(s);
                tip.setFont(new Font("bold", 16));
                tip.setOnAction(new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent t) {
                        grid.getChildren().remove(tip);
                        help.setDisable(false);
                        start.setDisable(false);
                        pause.setDisable(false);
                        giveup.setDisable(false);
                        sSkill.setDisable(false);
                    }
                });
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
        name.setStyle("-fx-text-fill: #00FF00;");//设置字体颜色
        Text white = new Text("白方步数：");
        white.setFont(new Font(16));
        Text black = new Text("黑方步数：");
        black.setFont(new Font(16));
        ScrollPane sp1 = new ScrollPane();
        sp1.setMaxWidth(210);
        ScrollPane sp2 = new ScrollPane();
        sp2.setMaxWidth(210);
        Text recordWhite = new Text();
        recordWhite.setFill(Color.LIGHTBLUE);
        Text recordBlack = new Text();
        recordBlack.setFill(Color.LIGHTBLUE);
        sp1.setContent(recordWhite);
        sp2.setContent(recordBlack);
        //三个特殊按钮
        Text special = new Text("使用特殊技能:");
        special.setFont(new Font(16));
        //王车易位选择
        final Button left = new Button("长易位");
        left.setFont(new Font(20));
        left.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        final Button right = new Button("短易位");
        right.setFont(new Font(20));
        right.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        Button hitPassSolderL = new Button("吃过路兵：左");
        hitPassSolderL.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        hitPassSolderL.setPrefWidth(160);
        Button hitPassSolderR = new Button("吃过路兵：右");
        hitPassSolderR.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        hitPassSolderR.setPrefWidth(160);
        //将它们加入盒子
        VBox hitP = new VBox();
        VBox changG = new VBox();
        hitP.getChildren().addAll(hitPassSolderL, hitPassSolderR);
        changG.getChildren().addAll(left, right);
        //手风琴控件
        TitledPane KRchange = new TitledPane("王车易位", changG);
        KRchange.setFont(new Font(20));
        TitledPane hitS = new TitledPane("吃过路兵", hitP);
        hitS.setFont(new Font(20));
        sUp = new TitledPane("兵升变", upTo);
        sUp.setFont(new Font(20));
        sSkill = new Accordion();
        sSkill.setMaxWidth(210);
        sSkill.setPrefWidth(210);
        sSkill.getPanes().addAll(KRchange, hitS, sUp);
        sSkill.setDisable(true);
        //升变对象
        group = new ToggleGroup();
        final RadioButton 后 = new RadioButton("后");
        后.setFont(new Font("bold", 20));
        后.setToggleGroup(group);
        final RadioButton 车 = new RadioButton("车");
        车.setFont(new Font("bold", 20));
        车.setToggleGroup(group);
        final RadioButton 象 = new RadioButton("象");
        象.setFont(new Font("bold", 20));
        象.setToggleGroup(group);
        final RadioButton 马 = new RadioButton("马");
        马.setFont(new Font("bold", 20));
        马.setToggleGroup(group);
        upTo.getChildren().addAll(new Text("兵升变对象："), 后, 车, 象, 马);
        upTo.setStyle("-fx-font: 20 arial;");
        vbox.getChildren().addAll(namebox, black, sp2, white, sp1, new Separator(), special, sSkill);
        //初始化bgRect数组
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if ((column + row) % 2 == 0) {
                    bgRect[column][row] = new Rectangle(80, 80, Color.LIGHTYELLOW);
                } else {
                    bgRect[column][row] = new Rectangle(80, 80, Color.BLACK);
                }
            }
        }

//        使用GridPane作为棋盘，该窗格可以进行栈覆盖
        grid.setPadding(new Insets(0, 0, 0, 10));
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
        for (int a = 0; a < 40; a++) {
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
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[8]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 8;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 8) {
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
        });
        piece[9].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[9]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 9;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 9) {
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
        });
        piece[10].addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent t) {
                        ImageView source = (ImageView) t.getSource();
                        int c = GridPane.getColumnIndex(source);
                        int r = GridPane.getRowIndex(source);
                        if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                            grid.getChildren().remove(piece[10]);
                            try {
                                grid.add(piece[selected], c, r);
                            } catch (Exception e) {
                                System.out.println("已经成功消灭对方一子！");
                            } finally {
                                System.out.println("well done!");
                                selected = 40;
                                control = false;
                            }
                        } else {//选中棋子
                            name.setText("黑方:兵Pawn");//在解释区显示
                            if (control == false) {
                                selected = 10;
                                isThere[c][r] = false;
                                for (int a = 0; a < 16; a++) {
                                    if (a != 10) {
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
                });
        piece[11].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[11]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 11;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 11) {
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
        });
        piece[12].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[12]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 12;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 12) {
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
        });
        piece[13].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[13]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 13;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 13) {
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
        });
        piece[14].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[14]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 14;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 14) {
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
        });
        piece[15].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[15]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = false;
                    }
                } else {//选中棋子
                    name.setText("黑方:兵Pawn");//在解释区显示
                    if (control == false) {
                        selected = 15;
                        isThere[c][r] = false;
                        for (int a = 0; a < 15; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
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
        });
        //为白方步兵处理
        piece[16].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[16]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 16;
                        for (int a = 17; a < 32; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[17].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[17]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 17;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 17) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[18].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[18]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 18;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 18) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[19].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[19]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 19;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 19) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[20].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[20]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 20;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 20) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[21].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[21]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 21;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 21) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[22].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[22]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 22;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 22) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        piece[23].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[23]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        selected = 40;
                        control = true;
                    }
                } else {//选中棋子
                    name.setText("白方:兵Pawn");//在解释区显示
                    if (control == true) {
                        selected = 23;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 23) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                        if (r == 6) {//如果兵没动过，可以走两步
                        } else if (r > 0) {//否则只能走一步
                        } else if (r == 0) {//兵升变
                        }
                    }
                }
            }
        });
        //黑方车的处理
        piece[0].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[0]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:车Rook");//在解释区显示
                    if (control == false) {
                        selected = 0;
                        isThere[c][r] = false;
                        moved[3] = true;
                        for (int a = 0; a < 16; a++) {
                            if (a != 0) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[7].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[7]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:车Rook");//在解释区显示
                    if (control == false) {
                        selected = 7;
                        isThere[c][r] = false;
                        moved[5] = true;
                        for (int a = 0; a < 16; a++) {
                            if (a != 7) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方车的处理
        piece[24].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int r = GridPane.getRowIndex(source);
                int c = GridPane.getColumnIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[24]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:车Rook");//在解释区显示
                    if (control == true) {
                        selected = 24;
                        isThere[c][r] = false;
                        moved[0] = true;
                        for (int a = 16; a < 32; a++) {
                            if (a != 24) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[31].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[31]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:车Rook");//在解释区显示
                    if (control == true) {
                        selected = 31;
                        isThere[c][r] = false;
                        moved[2] = true;
                        for (int a = 16; a < 32; a++) {
                            if (a != 31) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //黑方马
        piece[1].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[1]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:马Knight");//在解释区显示
                    if (control == false) {
                        selected = 1;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 1) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[6].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[6]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:马Knight");//在解释区显示
                    if (control == false) {
                        selected = 6;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 6) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方马
        piece[25].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[25]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:马Knight");//在解释区显示
                    if (control == true) {
                        selected = 25;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 25) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[30].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[30]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:马Knight");//在解释区显示
                    if (control == true) {
                        selected = 30;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 30) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //黑方象
        piece[2].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[2]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:白象Bishop");//在解释区显示
                    if (control == false) {
                        selected = 2;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 2) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[5].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[5]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:黑象Bishop");//在解释区显示
                    if (control == false) {
                        selected = 5;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 5) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方象
        piece[26].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[26]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:黑象Bishop");//在解释区显示
                    if (control == true) {
                        selected = 26;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 26) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[29].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[29]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:白象Bishop");//在解释区显示
                    if (control == true) {
                        selected = 29;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 29) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //黑方后
        piece[3].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[3]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:后Queen");//在解释区显示
                    if (control == false) {
                        selected = 3;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            if (a != 3) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方后
        piece[27].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[27]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:后Queen");//在解释区显示
                    if (control == true) {
                        selected = 27;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            if (a != 27) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //黑方王
        piece[4].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected > 15 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[4]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        name.setText("白方胜利");
                        System.out.println("白方胜利");
                        grid.setEffect(new Bloom());
                        grid.setDisable(true);
                        selected = 40;
                    }
                } else {//选中棋子
                    name.setText("黑方:王King");//在解释区显示
                    if (control == false) {
                        selected = 4;
                        isThere[c][r] = false;
                        moved[4] = true;
                        for (int a = 0; a < 16; a++) {
                            if (a != 4) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方王
        piece[28].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[28]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        name.setText("黑方胜利");
                        System.out.println("黑方胜利");
                        grid.setEffect(new Bloom());
                        grid.setDisable(true);
                        selected = 40;
                    }
                } else {//选中棋子
                    name.setText("白方:王King");//在解释区显示
                    if (control == true) {
                        selected = 28;
                        isThere[c][r] = false;
                        moved[1] = true;
                        for (int a = 16; a < 32; a++) {
                            if (a != 28) {
                                piece[a].setDisable(true);
                                piece[a].setEffect(new Bloom());
                            }
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //白方车马象后
        piece[36].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[36]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:车Rook");//在解释区显示
                    if (control == true) {
                        selected = 36;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[37].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[37]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:马Knight");//在解释区显示
                    if (control == true) {
                        selected = 37;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[38].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[38]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:象Bishop");//在解释区显示
                    if (control == true) {
                        selected = 38;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[39].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 0 && selected < 16) || (selected > 31 && selected < 36)) {//对方来吃
                    grid.getChildren().remove(piece[39]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 7 && selected < 16 && r == 7) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = true;
                        }
                    }
                } else {//选中棋子
                    name.setText("白方:后Queen");//在解释区显示
                    if (control == true) {
                        selected = 39;
                        isThere[c][r] = false;
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        //黑方车马象后
        piece[32].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 16 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[32]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:车Rook");//在解释区显示
                    if (control == true) {
                        selected = 32;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[33].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 16 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[33]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:马Knight");//在解释区显示
                    if (control == true) {
                        selected = 33;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[34].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 16 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[34]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:象Bishop");//在解释区显示
                    if (control == true) {
                        selected = 34;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        piece[35].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                ImageView source = (ImageView) t.getSource();
                int c = GridPane.getColumnIndex(source);
                int r = GridPane.getRowIndex(source);
                if ((selected >= 16 && selected < 32) || (selected > 35 && selected < 40)) {//对方来吃
                    grid.getChildren().remove(piece[35]);
                    try {
                        grid.add(piece[selected], c, r);
                    } catch (Exception e) {
                        System.out.println("已经成功消灭对方一子！");
                    } finally {
                        System.out.println("well done!");
                        if (selected > 15 && selected < 24 && r == 0) {//必须升变
                            sSkill.getPanes().get(2).setExpanded(true);
                            group.selectToggle(null);
                        } else {
                            selected = 40;
                            control = false;
                        }
                    }
                } else {//选中棋子
                    name.setText("黑方:后Queen");//在解释区显示
                    if (control == true) {
                        selected = 35;
                        isThere[c][r] = false;
                        for (int a = 0; a < 16; a++) {
                            piece[a].setDisable(true);
                            piece[a].setEffect(new Bloom());
                        }
                        for (int a = 16; a < 32; a++) {
                            piece[a].setDisable(false);
                            piece[a].setEffect(null);
                        }
                    }
                }
            }
        });
        /*
         * 棋盘面板的单击事件，实现棋子的移动
         */
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                bgRect[column][row].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent t) {
                        if (selected < 40) {//必须有棋子被选中才能移动
                            Rectangle s = (Rectangle) t.getSource();
                            int c = GridPane.getColumnIndex(s);
                            int r = GridPane.getRowIndex(s);
                            if (isThere[c][r] == false) {
                                if (selected >= 8 && selected <= 23 && (r == 0 || r == 7)) {//若可以升变的棋子
                                    try {
                                        grid.add(piece[selected], c, r);
                                    } catch (Exception e) {
                                        System.out.println("正常移动。");
                                    }
                                } else {
                                    try {
                                        grid.add(piece[selected], c, r);
                                    } catch (Exception e) {
                                        System.out.println("正常移动。");
                                    } finally {
                                        //双方交替走棋
                                        if (selected < 32 && selected > 15) {
                                            control = false;
                                        } else if (selected < 16) {
                                            control = true;
                                        } else if (selected > 31 && selected <= 35) {
                                            control = true;
                                        } else if (selected > 35) {
                                            control = false;
                                        }
                                        selected = 40;
                                    }
                                }
                            }//如果有棋子，这里不执行，而是转向进行图片事件
                        }
                    }
                });
            }
        }
        //给特殊按钮增加事件
        left.setOnAction(new EventHandler<ActionEvent>() {//王车易位事件--长

            public void handle(ActionEvent t) {
                if (control == false) {//黑方易位
                    if (isThere[1][0] == false && isThere[2][0] == false && isThere[3][0] == false) {
                        if (moved[3] == false && moved[4] == false) {//可以易位
                            grid.getChildren().remove(piece[4]);
                            grid.getChildren().remove(piece[0]);;
                            grid.add(piece[4], 2, 0);
                            grid.add(piece[0], 3, 0);
                            control = true;
                            moved[3] = false;
                            moved[4] = false;
                            selected = 40;
                            for (int a = 16; a < 32; a++) {
                                piece[a].setDisable(false);
                                piece[a].setEffect(null);
                            }
                        }
                    }
                } else {
                    if (isThere[1][7] == false && isThere[2][7] == false && isThere[3][7] == false) {
                        if (moved[0] == false && moved[1] == false) {//可以易位
                            grid.getChildren().remove(piece[28]);
                            grid.getChildren().remove(piece[24]);;
                            grid.add(piece[28], 2, 7);
                            grid.add(piece[24], 3, 7);
                            control = false;
                            moved[0] = false;
                            moved[1] = false;
                            selected = 40;
                            for (int a = 0; a < 16; a++) {
                                piece[a].setDisable(false);
                                piece[a].setEffect(null);
                            }
                        }
                    }
                }
            }
        });
        right.setOnAction(new EventHandler<ActionEvent>() {//王车易位事件--短

            public void handle(ActionEvent t) {
                if (control == false) {//黑方易位
                    if (isThere[5][0] == false && isThere[6][0] == false) {
                        if (moved[4] == false && moved[5] == false) {//可以易位
                            grid.getChildren().remove(piece[4]);
                            grid.getChildren().remove(piece[7]);;
                            grid.add(piece[4], 6, 0);
                            grid.add(piece[7], 5, 0);
                            control = true;
                            moved[4] = false;
                            moved[5] = false;
                            selected = 40;
                            for (int a = 16; a < 32; a++) {
                                piece[a].setDisable(false);
                                piece[a].setEffect(null);
                            }
                        }
                    }
                } else {
                    if (isThere[5][7] == false && isThere[6][7] == false) {
                        if (moved[1] == false && moved[2] == false) {//可以易位
                            grid.getChildren().remove(piece[28]);
                            grid.getChildren().remove(piece[31]);;
                            grid.add(piece[28], 6, 7);
                            grid.add(piece[31], 5, 7);
                            control = false;
                            moved[1] = false;
                            moved[2] = false;
                            selected = 40;
                            for (int a = 0; a < 16; a++) {
                                piece[a].setDisable(false);
                                piece[a].setEffect(null);
                            }
                        }
                    }
                }
            }
        });
        hitPassSolderL.setOnAction(new EventHandler<ActionEvent>() {//吃左侧过路兵事件

            public void handle(ActionEvent t) {
                if (control == true) {//白方吃黑方
                    try {
                        int rPawn = GridPane.getRowIndex(piece[selected]);
                        if (selected >= 16 && selected < 24 && rPawn == 3) {
                            int cPawn = GridPane.getColumnIndex(piece[selected]);
                            for (int test = 8; test < 16; test++) {
                                int rHited = GridPane.getRowIndex(piece[test]);
                                int cHited = GridPane.getColumnIndex(piece[test]);
                                if (rPawn == rHited && cPawn == cHited + 1) {
                                    grid.getChildren().remove(piece[test]);
                                    grid.getChildren().remove(piece[selected]);
                                    grid.add(piece[selected], cHited, rHited - 1);
                                    selected = 40;
                                    control = false;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("未选择白兵");
                    }
                } else {//黑方吃白方
                    try {
                        int rPawn = GridPane.getRowIndex(piece[selected]);
                        if (selected >= 8 && selected < 16 && rPawn == 4) {
                            int cPawn = GridPane.getColumnIndex(piece[selected]);
                            for (int test = 16; test < 24; test++) {
                                int rHited = GridPane.getRowIndex(piece[test]);
                                int cHited = GridPane.getColumnIndex(piece[test]);
                                if (rPawn == rHited && cPawn == cHited + 1) {
                                    grid.getChildren().remove(piece[test]);
                                    grid.getChildren().remove(piece[selected]);
                                    grid.add(piece[selected], cHited, rHited + 1);
                                    selected = 40;
                                    control = true;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("未选择黑兵");
                    }
                }
            }
        });
        hitPassSolderR.setOnAction(new EventHandler<ActionEvent>() {//吃右过路兵

            public void handle(ActionEvent t) {
                if (control == true) {//白方吃黑方
                    try {
                        int rPawn = GridPane.getRowIndex(piece[selected]);
                        if (selected >= 16 && selected < 24 && rPawn == 3) {
                            int cPawn = GridPane.getColumnIndex(piece[selected]);
                            for (int test = 8; test < 16; test++) {
                                int rHited = GridPane.getRowIndex(piece[test]);
                                int cHited = GridPane.getColumnIndex(piece[test]);
                                if (rPawn == rHited && cPawn == cHited - 1) {
                                    grid.getChildren().remove(piece[test]);
                                    grid.getChildren().remove(piece[selected]);
                                    grid.add(piece[selected], cHited, rHited - 1);
                                    selected = 40;
                                    control = false;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("未选择白兵");
                    }
                } else {//黑方吃白方
                    try {
                        int rPawn = GridPane.getRowIndex(piece[selected]);
                        if (selected >= 8 && selected < 16 && rPawn == 4) {
                            int cPawn = GridPane.getColumnIndex(piece[selected]);
                            for (int test = 16; test < 24; test++) {
                                int rHited = GridPane.getRowIndex(piece[test]);
                                int cHited = GridPane.getColumnIndex(piece[test]);
                                if (rPawn == rHited && cPawn == cHited - 1) {
                                    grid.getChildren().remove(piece[test]);
                                    grid.getChildren().remove(piece[selected]);
                                    grid.add(piece[selected], cHited, rHited + 1);
                                    selected = 40;
                                    control = true;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("未选择黑兵");
                    }
                }
            }
        });
        //后车象马
        group.selectedToggleProperty().addListener(
                new ChangeListener<Toggle>() {

                    public void changed(ObservableValue<? extends Toggle> ov,
                            Toggle old_toggle, Toggle new_toggle) {
                        try {
                            int c = GridPane.getColumnIndex(piece[selected]);
                            if (control == true) {//白兵升变
                                grid.getChildren().remove(piece[selected]);
                                RadioButton rb = (RadioButton) new_toggle;
                                if (后 == rb) {//升变为后
                                    grid.add(piece[39], c, 0);
                                    control = false;
                                    selected = 40;
                                } else if (象 == rb) {
                                    grid.add(piece[38], c, 0);
                                    control = false;
                                    selected = 40;
                                } else if (马 == rb) {
                                    grid.add(piece[37], c, 0);
                                    control = false;
                                    selected = 40;
                                } else if (车 == rb) {
                                    grid.add(piece[36], c, 0);
                                    control = false;
                                    selected = 40;
                                }
                            } else {//黑兵升变
                                grid.getChildren().remove(piece[selected]);
                                RadioButton rb = (RadioButton) new_toggle;
                                if (后 == rb) {//升变为后
                                    grid.add(piece[35], c, 7);
                                    control = true;
                                    selected = 40;
                                } else if (象 == rb) {
                                    grid.add(piece[34], c, 7);
                                    control = true;
                                    selected = 40;
                                } else if (马 == rb) {
                                    grid.add(piece[33], c, 7);
                                    control = true;
                                    selected = 40;
                                } else if (车 == rb) {
                                    grid.add(piece[32], c, 7);
                                    control = true;
                                    selected = 40;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println("未选择棋子进行升变");
                        }
                    sSkill.getPanes().get(2).setExpanded(false);
                    }
                });
    }
}
