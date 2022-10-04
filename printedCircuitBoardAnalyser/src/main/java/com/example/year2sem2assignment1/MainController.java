package com.example.year2sem2assignment1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    public Label totalLabel;

    public MainController() {
    }

    @FXML
    public Slider noiseSlider;

    @FXML
    private Label welcomeText;

    @FXML
    public Button closeButton;

    @FXML
    ImageView imageView, imageViewBW, imageViewR;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private int total;
    private int width;
    private int height;


    public int[] thePixelArray;
    public int sliderValue;
    public WritableImage newImage;
    public WritableImage newImageBW;

    Color pickedColour = Color.rgb(228, 147, 40, 1);

    Hashtable<Integer, ArrayList<Integer>> hash;


    /**
     * This method retrieves the coordinates on the location of the mouse when clicked
     */
    public void imageHandle(MouseEvent e){
        double x = e.getX();
        double y = e.getY();

        imageView = (ImageView) e.getSource();
        Bounds bounds = imageView.getLayoutBounds();
        double xScale = bounds.getWidth() / imageView.getImage().getWidth();
        double yScale = bounds.getHeight() / imageView.getImage().getHeight();

        x /= xScale;
        y /= yScale;

        int xCord = (int) x;
        int yCord = (int) y;

        getPixelColour(xCord, yCord);
        pickedColour = getPixelColour((int) x, (int) y);
    }

    public void initialize() {


    }



    /**
     * This method is used to close the application
     *
     * @param event
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    /**
     * This method retrieves the colour of the pixel in specified coordinates
     *
     * @param x
     * @param y
     * @return thePixelColour
     */
    public Color getPixelColour(int x, int y) {
        PixelReader pixelReader = imageView.getImage().getPixelReader();
        Color thePixelColour = pixelReader.getColor(x, y);
        return thePixelColour;
    }


    /**
     * This method uses the filechooser to import an image to an image view
     *
     * @param event
     */
    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("openFile");
        Window ownerWindow = null;
        File selectedFile = fileChooser.showOpenDialog(ownerWindow);
        Image image = new Image(selectedFile.toURI().toString(), 400, 300, true, true);
        if (image != null) {
            imageView.setImage(image);
        }
    }




    /**
     * This changed the pixels of the selected colour to black, while making the others back
     *
     * @param actionEvent
     * @return newImage
     */
    public WritableImage changeToBW(ActionEvent actionEvent) {

        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();

         width = (int) image.getWidth();
         height = (int) image.getHeight();

        WritableImage newImage = new WritableImage(width, height);
         thePixelArray = new int[width * height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

               // pickedColour = pixelReader.getColor(col, row);
                Color thePixelCol = pixelReader.getColor(col, row);

                double red = pixelReader.getColor(col, row).getRed();
                double green = pixelReader.getColor(col, row).getGreen();
                double blue = pixelReader.getColor(col, row).getBlue();

                if (red > pickedColour.getRed() - .14 && (red < pickedColour.getRed() + .14)
                        && green > pickedColour.getGreen() - .14 && green < pickedColour.getGreen() + .14
                        && blue > pickedColour.getBlue() - .14 && blue < pickedColour.getBlue() + 14) {
                    newImage.getPixelWriter().setColor(col, row, Color.BLACK);

                    thePixelArray[(row * width) + col] = (row * width) + col;


                } else {
                    newImage.getPixelWriter().setColor(col, row, Color.WHITE);
                    thePixelArray[(row * width) + col] = -1;


                }
            }}

                    unionPixels();
            for(int id=0;id<thePixelArray.length;id++) {
                if (thePixelArray[id] >= 0)
                    System.out.println("The root of element " + id + " is " + find(thePixelArray, id)
                            + " (element value: " + thePixelArray[id] + ")");

            }
                    populateHashTable();
                    imageViewBW.setImage(newImage);
                    newImageBW = newImage;
                   // createRectangle();



        imageViewBW.setImage(newImage);
        return newImage;

    }

    /**
     * This method is an Iterative version of find with the negative value stored at root
     *
     * @param a
     * @param id
     * @return id
     */
    public static int find(int[] a, int id) {
       // while (a[id] >= 0) id = a[id];
       // return id;
        if(a[id]<0) return a[id];
        if(a[id]==id) return id;
        else  return find(a, a[id]);
    }

    /**
     * This method finds disjoint sets in an array
     *
     * @param a
     * @param p
     * @param q
     */
    public static void unionFind(int[] a, int p, int q) {
        a[find(a, q)] = find(a, p); //The root of q is made reference p
    }

    /**
     * This method utilised the union and find methods to unite disjoint sets in thePixelArray
     */
    public void unionPixels() {
        for (int id = 0; id < thePixelArray.length; id++) {
            //+1 for beside +width underneeth
               if (thePixelArray[id] != -1) {

            if ((id + 1) < thePixelArray.length && thePixelArray[id + 1] >= 0 && (id + 1) % width != 0) {  //makes sure it doesn't go over the array borders
                //   if (thePixelArray[id] != -1) {
               //      if (thePixelArray[id + 1] != -1) {
                unionFind(thePixelArray, id, id + 1);}//}

                if ((id + width) < thePixelArray.length && thePixelArray[id + width] >= 0 && (id + width) % width != 0) {  //makes sure it doesn't go over the image borders as well as under 0 so that pixels on the next line dont get joined up
                    //      if (thePixelArray[id] != -1) {
                    //          if (thePixelArray[id + width] != -1) {
                    unionFind(thePixelArray, id, id + width);//}
                }

        }}
    }
    // }
    //  }
    //   }
    //   }
    // }


    /**
     * This method generated a random colour
     *
     * @return (r, g, b)
     */
    public Color randomiseColour() {
        Random randomColour = new Random();
        int r = randomColour.nextInt(255);
        int g = randomColour.nextInt(255);
        int b = randomColour.nextInt(255);
        return Color.rgb(r, g, b);
    }


    /**
     * This method populates the hash table with the use of thePixelArray
     * This method was created with the help of Dean Lonergan
     */
    public void populateHashTable() {
        hash = new Hashtable<>();
        for (int id = 0; id < thePixelArray.length; id++) { //going through array

            if (thePixelArray[id] != -1) {
                int theRoot = find(thePixelArray, id);    //finding array id of root if id is not -1 (white pixel)
                if (hash.containsKey(theRoot)) {

                    ArrayList<Integer> elements;
                    elements = hash.get(theRoot);
                    elements.add(id);

                } else {
                    ArrayList<Integer> elements;
                    elements = new ArrayList<>();
                    elements.add(id);
                    hash.put(theRoot, elements);
                }
            }
        }
    }

    /**
     * This method colours the black pixel clusters in a random colour
     * This method was developed with the help of William Vasilev
     * @param actionEvent
     */
    public void randomColours(ActionEvent actionEvent) {
        int xVar = 0;
        int yVar = 0;
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int id = 0; id < thePixelArray.length; id++) {
            xVar = id % width;
            yVar = id / width;
            pixelWriter.setColor(xVar, yVar, Color.WHITE); //making background white
        }
        for (int theRoot : hash.keySet()) {
            Color randomC = randomiseColour();
            for (int theVal : hash.get(theRoot)) { //calling key elements theVal
                xVar = theVal % width; //finding column
                yVar = theVal / width; //finding width
                pixelWriter.setColor(xVar, yVar, randomC);
            }
        }
        imageViewBW.setImage(newImage);
    }


    /**
     * This method removed excess noise bhy the amount that the slider specifies
     * @return newImageBW
     */
    public WritableImage removeNoise() {
        PixelReader pixelReader = newImageBW.getPixelReader();
        WritableImage newImage = new WritableImage(width, height);
        newImage=newImageBW;
        sliderValue=(int)noiseSlider.getValue();
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for (int theRoot : hash.keySet()) { //going through hashtable to find elements they have key as root
            ArrayList<Integer> values = hash.get(theRoot); //use the key to get arraylist of elements
            if (values.size() < sliderValue) {
                for (int element : values) {
                    int col = element % width; //finding column
                    int row = element / width; //finding width
                    pixelWriter.setColor(col, row, Color.WHITE);

                }

            }

        }
        imageViewBW.setImage(newImage);
        rectSizeNum();
        return newImage;

    }




    /**
     * This method uses the sampled colour to colour in black pixels
     * @param actionEvent
     */
    public void sampleColours(ActionEvent actionEvent) {
        int x = 0;
        int y = 0;

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int i = 0; i < thePixelArray.length; i++) {
            x = i % width;
            y = i / width;
            pixelWriter.setColor(x, y, Color.WHITE);
        }
        for (int theRoot : hash.keySet()) {
            Color pickedC = pickedColour;
            for (int theVal : hash.get(theRoot)) {
                x = theVal % width;
                y = theVal / width;
                pixelWriter.setColor(x, y, pickedC);
            }
        }
        imageViewBW.setImage(writableImage);
    }




    /**
     * This method creates rectangles around selected components
     * This method was created with the help of Siobhan Roche and William Vasilev
     */
    public void rectSizeNum() {
        int rectCount = 0;
        int border[]=new int[hash.size()];
        total = 0;



      //This part sorts the size of different components
        int max = Integer.MAX_VALUE;
        for (int i = 0; i < border.length; i++) { //size sort
            int maxRoot = 0;
            int maxCurrentVal = 0;
            for (int k : hash.keySet()) {
                if (hash.get(k).size() >= maxCurrentVal && hash.get(k).size() < max) {
                    maxCurrentVal = hash.get(k).size();
                    maxRoot = k;
                }
            }
            border[i] = maxRoot;
            max = maxCurrentVal;

        }

          //This part gets the highest and lowest values at the edges, this is to get the rectangles locations.
        for (int theRoot : hash.keySet()) {
            int top = Integer.MAX_VALUE, bottom = -Integer.MAX_VALUE, left = Integer.MAX_VALUE, right = -Integer.MAX_VALUE;
            for (int i : hash.get(theRoot)) {

                if (i % width < left) left = i % width;
                if (i % width > right) right = i % width;
                if (i / width < top) top = i / width;
                if (i / width > bottom) bottom = i / width;
            }

            int size = hash.get(theRoot).size();

               //This part draws the rectangle
               if (size > sliderValue) {

                   total = total + 1;

            Rectangle rect = new Rectangle(left, top, right - left, bottom - top);
            rectCount = findIndex(border, theRoot);
            rect.setTranslateX(imageView.getLayoutX());
            rect.setTranslateY(imageView.getLayoutY());
            rect.setStroke(Color.BLUE);
            rect.setFill(Color.TRANSPARENT);
            Tooltip.install(rect, new Tooltip("Number: " + rectCount + "\nSize : " + size));
            ((Pane) imageView.getParent()).getChildren().remove(rect);
            ((Pane) imageView.getParent()).getChildren().add(rect);


            //this part adds the text for the reatcngle count.
            Text t = new Text(rect.getTranslateX() + left + 2, rect.getTranslateY() + bottom - 10, rectCount + "");
            t.setFont(new Font(10));
                   ((Pane) imageView.getParent()).getChildren().remove(t);
            ((Pane) imageView.getParent()).getChildren().add(t);
        }
        }

       totalLabel.setText(String.valueOf(total));
    }

    /**
     * This method finds the index number of each rectangle in order to put them in order
     * @param b
     * @param value
     * @return i+1
     */
    public int findIndex(int[] b, int value){
        for(int i = 0; i < b.length; i++){
            if(b[i] == value)
                return i + 1;
        }
        return -1;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    /*
    public void createRectangle() {
        hash = new Hashtable<>();
//        for(int i = 0;i < imageArray.length;i+.+){
//            if (imageArray[i]!=-111 && !allRoots.contains(find(imageArray,i))) allRoots.add(find(imageArray,i));
//        }
        for (int i = 0; i < thePixelArray.length; i++) {
            if (thePixelArray[i] != -111) {
                int root = find(thePixelArray, i);
                if (hash.containsKey(root)) {
                    ArrayList<Integer> elements = hash.get(root);
                    elements.add(i);
                } else {
                    ArrayList<Integer> elements = new ArrayList<>();
                    elements.add(i);
                    hash.put(root, elements);
                }

            }

        }
        createRectangle2();

    }
*/


      /*
        @Override
        public void initialize(URL url, ResourceBundle resources) {

            sliderValue = (int) noiseSlider.getValue();


            noiseSlider.valueProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldNumber, Number newNumber) {

                    sliderValue = (int) noiseSlider.getValue();

                }
            });
        }
    }
    */

    /*
        imageView.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();

            imageView = (ImageView) e.getSource();
            Bounds bounds = imageView.getLayoutBounds();
            double xScale = bounds.getWidth() / imageView.getImage().getWidth();
            double yScale = bounds.getHeight() / imageView.getImage().getHeight();

            x /= xScale;
            y /= yScale;

            int xCord = (int) x;
            int yCord = (int) y;

            getPixelColour(xCord, yCord);
            pickedColour = getPixelColour((int) x, (int) y);
                }
        );
        */

/*
        //The code under was created with the help of https://www.youtube.com/watch?v=cvcO4DvDVsA
        sliderValue = (int) noiseSlider.getValue();
        noiseSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldNumber, Number newNumber) {
                sliderValue = (int) noiseSlider.getValue();

            }
        });
        */

    //}
        /*    Bounds bounds = imageView.getLayoutBounds();
            double xScale = bounds.getWidth() / imageView.getImage().getWidth();
            double yScale = bounds.getHeight() / imageView.getImage().getHeight();

            left /= xScale;
            right /= yScale;
            top /= xScale;
            bottom /= yScale;*/
}