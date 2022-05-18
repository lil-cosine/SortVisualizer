import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.AnimationTimer;

public class display extends Application
{
    private Stage primary;
    private Color currentColor;
    private Color bgcolor;
    private double currentSize;
    private Canvas canvas;
    public GraphicsContext pen;
    private HBox canvasBox;
    private int canvasWidth = 1200;
    private int canvasHeight = 600;
    public List<Integer> vals = new ArrayList<Integer>();
    public int idx;
    private int arraySize;
    private int boxWidth;

    @Override
    public void init()
    {
        //Establish global values
        this.arraySize = canvasWidth / 10;
        this.currentSize = 50;
        this.boxWidth = canvasWidth / arraySize;
        
        canvas = new Canvas(canvasWidth, canvasHeight);
        pen = canvas.getGraphicsContext2D();
        canvasBox = new HBox();
        canvasBox.getChildren().add(canvas);
        
        //Generate random integers and append them to the array
        Random random = new Random();
        for(int i = 0; i < arraySize; i++){
            vals.add(random.nextInt(canvasHeight - 50) + 1);
        }
    }


    @Override
    public void start(Stage primary)
    {
        this.primary = primary;
        BorderPane bp = new BorderPane();
        HBox topBox = new HBox();
        bp.setCenter(canvasBox);
        bp.setStyle("-fx-background-color: black");
        bp.setTop(topBox);
        primary.setScene(new Scene(bp));
        pen.setFill(Color.WHITE);
        topBox.getChildren().add(buildMenus());


        //Draws the unsorted array
        int xVal = 0;
        for(int i = vals.size() - 1; i > 0; i--){
            pen.fillRect(xVal, canvas.getHeight()- vals.get(i), boxWidth, vals.get(i) );
            xVal += 10;
        }
        


        primary.show();
    }
    
    private MenuBar buildMenus()
    {
        //Create menu items and add them to the menu bar
        MenuBar mbar = new MenuBar();
        Menu SortMenu = new Menu("Sorts");
        Menu ListMenu = new Menu("New Lists");
        mbar.getMenus().addAll(SortMenu, ListMenu);

        SortMenu.getItems().add(new SortMenuItem("Gnome"));
        SortMenu.getItems().add(new SortMenuItem("Bozo"));
        ListMenu.getItems().add(new NewListItem("New List"));
        
        return mbar;
    }
    
    @Override
    public void stop()
    {
    }

    /******************* Aliens ********************/
    class SortMenuItem extends MenuItem{

        public SortMenuItem(String name){
            super(name);
            
            setOnAction( e -> {
                //Change array size depending on sort
                //*This prevents the bozo sort from going for too long
                if(name == "Gnome"){
                    arraySize = canvasWidth / 10;
                }
                else if(name == "Bozo"){
                    arraySize = 5;
                }
                
                //Creates new random array
                boxWidth = canvasWidth / arraySize; 
                vals = new ArrayList<Integer>();
                Random random = new Random();
                for(int i = 0; i < (arraySize); i++){
                    vals.add(random.nextInt(canvasHeight - 50) + 1);
                };

                //Establish AnimationTimer
                AnimationTimer timer = new AnimationTimer(){
                    @Override
                    public void handle(long now)
                    {
                        //Draw the array, do one iteration of GnomeSort,
                        //and check if idx == the size of the array
                        if(name == "Gnome"){
                            DrawSort();
                            GnomeSort();
                            if(idx == vals.size()){
                                stop();
                                idx = 0;
                            }
                        }
                        
                        //Shuffle the array, draw the list, check if the array is in order
                        else if (name == "Bozo"){
                            Collections.shuffle(vals);
                            DrawSort();
                            
                            for(int i = 0; i < vals.size(); i++){
                                if(i == vals.size() - 1){
                                    stop();
                                }
                                else if(vals.get(i).compareTo(vals.get(i+1)) > 0){
                                    break;
                                } 
                            }
                            
                        }                      
                    }
                };
                
                //Begin the animation
                timer.start();
                
            });

        }

        //Performs one iteration of the GnomeSort
        public <T extends Comparable<T>> void GnomeSort(){
            if(idx == 0){
                idx += 1;
                DrawSort(idx);
            }
                    
            if(vals.get(idx).compareTo(vals.get(idx - 1)) >= 0){
                idx += 1;
                DrawSort(idx);
            }
                    
            else{
                int tmp = vals.get(idx - 1);
                vals.set(idx-1, vals.get(idx));
                vals.set(idx, tmp);
                idx = idx - 1;
                DrawSort(idx + 1, idx);  
            }
        
        }

        //Draws the array without making any color changes
        public void DrawSort(){
            int xVal = 0;
            pen.clearRect(0, 0, canvas.getWidth(), canvas.getHeight() );
            for(int i = 0; i < vals.size(); i++){
                pen.fillRect(xVal, canvas.getHeight()- vals.get(i), boxWidth, vals.get(i) );
                xVal += boxWidth;
            }
        }

        //Draws the array, making bar at the current index green
        public void DrawSort(int index){
            int xVal = 0;
            pen.clearRect(0, 0, canvas.getWidth(), canvas.getHeight() );
            for(int i = 0; i < vals.size(); i++){
                if(i == index){
                    pen.setFill(Color.GREEN);
                }
                else{
                    pen.setFill(Color.WHITE);
                }
                pen.fillRect(xVal, canvas.getHeight()- vals.get(i), boxWidth, vals.get(i) );
                xVal += boxWidth;
            }
        }
        
        //Draws the array, making the bar at the first index green
        //and the bar at the second index red
        public void DrawSort(int firstIndex, int secondIndex){
            int xVal = 0;
            
            pen.clearRect(0, 0, canvas.getWidth(), canvas.getHeight() );
            for(int i = 0; i < vals.size(); i++){
                if(i == firstIndex){
                    pen.setFill(Color.GREEN);
                }
                else if(secondIndex == i){
                    pen.setFill(Color.RED);
                }
                else{
                    pen.setFill(Color.WHITE);
                }
                pen.fillRect(xVal, canvas.getHeight()- vals.get(i), boxWidth, vals.get(i) );
                xVal += boxWidth;
            }
        }    
    }

    class NewListItem extends MenuItem{
        public NewListItem(String name){
            super(name);
            //Create new random array and draws it
            setOnAction( e -> {
                idx = 0;
                Random random = new Random();
                vals = new ArrayList<Integer>();
                for(int i = 0; i < (arraySize); i++){
                    vals.add(random.nextInt(canvasHeight - 50) + 1);
                }; 
                pen.clearRect(0, 0, canvas.getWidth(), canvas.getHeight() );
                int xVal = 0;
                for(int i = vals.size() - 1; i > 0; i--){
            
                    pen.fillRect(xVal, canvas.getHeight()- vals.get(i), boxWidth, vals.get(i) );
                    xVal += boxWidth;
                }

            });
        }
    }
}