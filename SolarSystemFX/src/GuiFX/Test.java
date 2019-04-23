package GuiFX;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.util.Duration;

import java.net.URL;

public class Test extends Application {
    CelestialBody[] planet = new CelestialBody[9];

    private static final float WIDTH = 1400;
    private static final float HEIGHT = 800;

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);
    private final DoubleProperty EarthAxis = new SimpleDoubleProperty(0);

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private final Sphere sphere = new Sphere(50);
    private final Sphere sphereearth = new Sphere(10);
    private final Sphere sphereMoon = new Sphere(10);
    private final Sphere sphereMoonGhost = new Sphere(1);
    private final Sphere sphereMercury = new Sphere(5);
    Group world = new Group();
    Group Moon = new Group();
    Group MoonGhost = new Group();



    @Override
    public void start(Stage primaryStage) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        world.getChildren().add(prepareSun());
        world.getChildren().add(prepareEarth());
        world.getChildren().add(prepareMercury());

        sphereMoonGhost.setTranslateX(20);
        Moon.getChildren().add(prepareMoon());
        MoonGhost.getChildren().add(sphereMoonGhost);

        Slider slider = prepareSlider();
        world.translateZProperty().bind(slider.valueProperty());
        Moon.translateZProperty().bind(slider.valueProperty());

        Sphere[] pl = new Sphere[planet.length];
        for(int i = 0; i < planet.length; i++){
            pl[i].setTranslateX(planet[i].xLoc);
            pl[i].setTranslateY(planet[i].yLoc);
            pl[i].setTranslateZ(planet[i].zLoc);

        }

        URL resource = getClass().getResource("travis.mp3");
        Media media = new Media(resource.toString());

        MediaPlayer player = new MediaPlayer(media);
        //player.play();

        Group root = new Group();
        root.getChildren().add(prepareImageView());
        root.getChildren().add(MoonGhost);
        root.getChildren().add(world);
        root.getChildren().add(Moon);
        root.getChildren().add(slider);

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        //scene.getStylesheets().add("flatterAdd.css");
        //initMouseControl(world, scene, primaryStage);

        primaryStage.setTitle("Galaxy");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation();
    
/*
    Thread thread =  new Thread() {
     
    	@Override
        public void run() {
        	MoonCoords();                                                                     
        }
    };    
    thread.start();
*/

    }

    private void prepareAnimation() {
        /*AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //rotation around own axis
                sphere.rotateProperty().set(sphere.getRotate() + 1);
                sphere2.rotateProperty().set(sphere2.getRotate() + 0.2);
                sphereMoon.rotateProperty().set(sphereMoon.getRotate() + 1);

                Rotate xRotate;
                Rotate zRotate;
                Rotate yRotate;
                world.getTransforms().addAll(
                        xRotate = new Rotate(0, Rotate.X_AXIS),
                        yRotate = new Rotate(0, Rotate.Y_AXIS),
                        zRotate = new Rotate(0, Rotate.Z_AXIS)

                );

                Rotate xRotateMoon;
                Rotate yRotateMoon;
                Moon.getTransforms().addAll(
                        xRotateMoon = new Rotate(0, Rotate.X_AXIS),
                        yRotateMoon = new Rotate(0, Rotate.Y_AXIS)
                );
                yRotateMoon.pivotYProperty().bind(EarthAxis);

                xRotate.angleProperty().bind(angleX);
                yRotate.angleProperty().bind(angleY);
                zRotate.angleProperty().bind(angleZ);
                xRotateMoon.angleProperty().bind(angleX);
                yRotateMoon.angleProperty().bind(angleY);

                //angle defines the way of rotation
                //angleX.set(1);
                angleZ.set(1);




            }
        };*/
        planet[0] = new CelestialBody("Sun", 0, 0, 0, 0, 0, 0, RealMasses.SUN_MASS);
        planet[1] = new CelestialBody("Mercury", RealDistance.mercuryXDistance, RealDistance.mercuryYDistance,
                RealDistance.mercuryZDistance, RealVelocities.mercuryXVel, RealVelocities.mercuryYVel,
                RealVelocities.mercuryZVel, RealMasses.MERCURY_MASS);
        planet[2] = new CelestialBody("Venus", RealDistance.venusXDistance, RealDistance.venusYDistance,
                RealDistance.venusZDistance, RealVelocities.venusXVel, RealVelocities.venusYVel,
                RealVelocities.venusZVel, RealMasses.VENUS_MASS);
        planet[3] = new CelestialBody("Earth", RealDistance.earthXDistance, RealDistance.earthYDistance,
                RealDistance.earthZDistance, RealVelocities.earthXVel, RealVelocities.earthYVel,
                RealVelocities.earthZVel, RealMasses.EARTH_MASS);
        planet[4] = new CelestialBody("Mars", RealDistance.marsXDistance, RealDistance.marsYDistance,
                RealDistance.marsZDistance, RealVelocities.marsXVel, RealVelocities.marsYVel, RealVelocities.marsZVel,
                RealMasses.MARS_MASS);
        planet[5] = new CelestialBody("Jupiter", RealDistance.jupiterXDistance, RealDistance.jupiterYDistance,
                RealDistance.jupiterZDistance, RealVelocities.jupiterXVel, RealVelocities.jupiterYVel,
                RealVelocities.jupiterZVel, RealMasses.JUPITER_MASS);
        planet[6] = new CelestialBody("Saturn", RealDistance.saturneXDistance, RealDistance.saturneYDistance,
                RealDistance.saturneZDistance, RealVelocities.saturnXVel, RealVelocities.saturnYVel,
                RealVelocities.saturnZVel, RealMasses.SATURNE_MASS);
        planet[7] = new CelestialBody("Uranus", RealDistance.uranusXDistance, RealDistance.uranusYDistance,
                RealDistance.uranusZDistance, RealVelocities.uranusXVel, RealVelocities.uranusYVel,
                RealVelocities.uranusZVel, RealMasses.URANUS_MASS);
        planet[8] = new CelestialBody("Neptune", RealDistance.neptuneXDistance, RealDistance.neptuneYDistance,
                RealDistance.neptuneZDistance, RealVelocities.neptuneXVel, RealVelocities.neptuneYVel,
                RealVelocities.neptuneZVel, RealMasses.NEPTUNE_MASS);

        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    for (int i = 0; i < planet.length; i++) {

                        planet[i].computeGravityStep(planet, 216000);
                        redraw();

                    }
                }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();


    }
    //Background
    private ImageView prepareImageView() {
        Image image = new Image(Test.class.getResourceAsStream("galaxy.jpeg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2 , -image.getHeight() / 2 , 800));
        return imageView;
    }

    //Slider for Zoom
    private Slider prepareSlider() {
        Slider slider = new Slider();
        slider.setMax(800);
        slider.setMin(-400);
        slider.setPrefWidth(300d);
        slider.setLayoutX(-150);
        slider.setLayoutY(200);
        slider.setShowTickLabels(true);
        slider.setTranslateZ(5);
        slider.setStyle("-fx-base: black");
        return slider;
    }

    private Node prepareSun() {
        //images
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/sun.jpeg")));

        //set Rotation
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

    private Node prepareEarth() {
        //images
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/earth/earth.normal.jpeg")));
        earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("images/earth/dark.jpeg")));
        earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("images/earth/shine.jpeg")));
        earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("images/earth/blue.jpeg")));

        sphereearth.setRotationAxis(Rotate.Y_AXIS);
        sphereearth.setMaterial(earthMaterial);
        //set Location
        sphereearth.setTranslateX(300);
        return sphereearth;
    }

    private Node prepareMoon() {
        //images
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/moon.jpeg")));

        sphereMoon.setRotationAxis(Rotate.Y_AXIS);
        sphereMoon.setMaterial(earthMaterial);
        sphereMoon.setTranslateX(280);
        return sphereMoon;
    }
    private Node prepareMercury(){
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image (getClass().getResourceAsStream("images/mercury.jpeg")));

        sphereMercury.setRotationAxis(Rotate.Y_AXIS);
        sphereMercury.setMaterial(mercuryMaterial);
        sphereMercury.setTranslateX(RealDistance.mercuryXDistance/100000);
        sphereMercury.setTranslateZ(RealDistance.mercuryZDistance/100000);
        //sphereMercury.setTranslateY(RealDistance.mercuryYDistance/100000);
        return sphereMercury;
    }
    private void redraw(){

        }
    }
 /* 
  private void MoonCoords(){
  	  while(true){
  	  	  sphereMoon.setTranslateX(sphere2.getTranslateX()+sphereMoonGhost.getTranslateX());
  	  	  sphereMoon.setTranslateY(sphere2.getTranslateY()+sphereMoonGhost.getTranslateY());
  	  	  sphereMoon.setTranslateZ(sphere2.getTranslateZ()+sphereMoonGhost.getTranslateZ());
  	  }
  }

  private void initMouseControl(Group group, Scene scene, Stage stage) {
    Rotate xRotate;
    Rotate yRotate;
    group.getTransforms().addAll(
        xRotate = new Rotate(0, Rotate.X_AXIS),
        yRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    xRotate.angleProperty().bind(angleX);
    yRotate.angleProperty().bind(angleY);

    scene.setOnMousePressed(event -> {
      anchorX = event.getSceneX();
      anchorY = event.getSceneY();
      anchorAngleX = angleX.get();
      anchorAngleY = angleY.get();
    });

    scene.setOnMouseDragged(event -> {
      angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
      angleY.set(anchorAngleY + anchorX - event.getSceneX());
    });

    stage.addEventHandler(ScrollEvent.SCROLL, event -> {
      double delta = event.getDeltaY();
      group.translateZProperty().set(group.getTranslateZ() + delta);
    });
  }
*/

}