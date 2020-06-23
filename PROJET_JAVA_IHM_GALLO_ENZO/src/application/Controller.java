package application;





import java.net.URL;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import data.CsvFileReader;
public class Controller implements Initializable {
	
	private Modele model = new Modele(false, false, false, false, false, false );
	
	//RadioButton
	
	@FXML
	private RadioButton btnQuadri;
	
	@FXML
	private RadioButton btnHisto;
	
	ToggleGroup btnRadio;
	
	
	//Buttons
	@FXML
	private Button btnStart;
	
	@FXML
	private Button btnPause;
	
	@FXML
	private Button btnStop;
	
	@FXML
	private Button btnGraphique;
	
	
	//TextField
	
	@FXML
	private TextField textFieldLat;
	
	@FXML
	private TextField textFieldLong;
	
	@FXML
	private TextField textFieldAnnee;
	
	@FXML
	private TextField textFieldVitesse;
	
	//slider
	@FXML
	private Slider slider = new Slider(1880,2020,1880);
	
	//pane
	@FXML
	private Pane pane3D;
	
	@FXML
	private Pane paneGraphe;
	
	
	//graphique
	@FXML
	private LineChart<Integer,Float> graphe;
	
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
	

    /**
     * Constructeur
     */
	public Controller() {
		model = new Modele(false, false, false, false, false ,false);
	}
	
	
	
	//Gestion des boutons
	
	private void clickQ(MouseEvent event) {
		model.setbtnQuadri(true);
		model.setbtnHisto(false);
		model.setbtnGraphique(false);
	}
	
	private void clickH(MouseEvent event) {
		model.setbtnQuadri(false);
		model.setbtnHisto(true);
		model.setbtnGraphique(false);
	}
	
	private void clickStart(MouseEvent event) {
		model.setbtnStart(true);
		model.setbtnPause(false);
		model.setbtnStop(false);
		model.setbtnGraphique(false);
	}
	
	private void clickP(MouseEvent event) {
		model.setbtnStart(false);
		model.setbtnPause(true);
		model.setbtnStop(false);
		model.setbtnGraphique(false);
	}
	
	private void clickStop(MouseEvent event) {
		model.setbtnStart(false);
		model.setbtnPause(false);
		model.setbtnStop(true);
		model.setbtnGraphique(false);
	}
	
	private void clickG(MouseEvent event) {
		model.setbtnQuadri(false);
		model.setbtnHisto(false);
		model.setbtnStart(false);
		model.setbtnPause(false);
		model.setbtnStop(false);
		model.setbtnGraphique(true);
	}
	
	
	/**
	 * Méthode createLine donnée
	 * @param origin
	 * @param target
	 * @return
	 */
	public Cylinder createLine(Point3D origin, Point3D target) {
       Point3D yAxis = new Point3D(0, 1, 0);
       Point3D diff = target.subtract(origin);
       double height = diff.magnitude();

       Point3D mid = target.midpoint(origin);
       Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

       Point3D axisOfRotation = diff.crossProduct(yAxis);
       double angle = Math.acos(diff.normalize().dotProduct(yAxis));
       Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

       Cylinder line = new Cylinder(0.01f, height);

       line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

       return line;
	}

	
	/**
	 * Méthode permettant de convertir les coordonnées géographiques (latitude,longitude) en coordonnées 3D
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
	public static Point3D geoCoordTo3dCoord(float lat, float lon,float radius) {
		float lat_cor = lat + TEXTURE_LAT_OFFSET;
		float lon_cor = lon + TEXTURE_LON_OFFSET;
		return new Point3D(
               -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                       * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius,
               -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor))*radius,
               java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                       * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius);
	}
   
	
	/**
	 * Méthode permettant de convertir les coordonnées 3D en coordonnéees géographiques (latitude,longitude) 
	 * @param p
	 * @return
	 */
	public static Coordonnees coord3DToGeoCoord(Point3D p) {
		double lat = -java.lang.Math.asin(p.getY());
		double lon = -java.lang.Math.atan2(p.getX(), p.getZ());		
		lat = java.lang.Math.toDegrees(lat) - TEXTURE_LAT_OFFSET;
		lon = java.lang.Math.toDegrees(lon) - TEXTURE_LON_OFFSET;
		if (lon < -180) {
			lon = 360 + lon; 
		}
		return new Coordonnees((int)lat, (int)lon);
	}
 
   
	/**
	 * Méthode permettant d'afficher une zone sur le globe
	 * @param parent
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	public static void displayTown(Group parent,String name,float latitude,float longitude) {
		Sphere sphere= new Sphere(0.03);
		Point3D point=geoCoordTo3dCoord(latitude, longitude,1);
		PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.WHITE);
		greenMaterial.setSpecularColor(Color.WHITE);
		sphere.setMaterial(greenMaterial);
		sphere.setId(name);
		sphere.setTranslateX(point.getX());
		sphere.setTranslateY(point.getY());
		sphere.setTranslateZ(point.getZ());
		parent.getChildren().add(sphere);
	}
   
	
	
	/**
	 * Méthode AddQuadrilateral donnée
	 * @param parent
	 * @param topRight
	 * @param bottomRight
	 * @param bottomLeft
	 * @param topLeft
	 * @param material
	 */
	private void AddQuadrilateral(Group parent, Point3D topRight,Point3D bottomRight,Point3D bottomLeft,Point3D topLeft,PhongMaterial material) {
   	
		final TriangleMesh triangleMesh=new TriangleMesh();
		final float[] points= {
   			(float)topRight.getX(),(float)topRight.getY(),(float)topRight.getZ(),
   			(float)topLeft.getX(),(float)topLeft.getY(),(float)topLeft.getZ(),
   			(float)bottomLeft.getX(),(float)bottomLeft.getY(),(float)bottomLeft.getZ(),
   			(float)bottomRight.getX(),(float)bottomRight.getY(),(float)bottomRight.getZ()
		};
		final float[] texCoords= {
				1,1,
				1,0,
				0,1,
				0,0
		};
		final int[] faces= {
				0,1,1,0,2,2,
				0,1,2,2,3,3
		};
   	
   	
		triangleMesh.getPoints().setAll(points);
		triangleMesh.getTexCoords().setAll(texCoords);
		triangleMesh.getFaces().setAll(faces);
   	
		final MeshView meshView = new MeshView(triangleMesh);
		meshView.setMaterial(material);
		parent.getChildren().addAll(meshView);
   	
	}
	
   
	
	
	/**
	 * Méthode qui permet d'afficher le quadrillage des anomalies pour une année donnée
	 * @param contour
	 * @param année
	 */
	public void afficheQuadri(Group contour, int année) {

	   
	   if( (année >=1880)&&(année <= 2020)) {

		   final PhongMaterial rMaterial = new PhongMaterial();
		   rMaterial.setDiffuseColor(Color.RED);
		   rMaterial.setSpecularColor(Color.RED);
       
       
		   final PhongMaterial oMaterial = new PhongMaterial();
		   oMaterial.setDiffuseColor(Color.ORANGE);
		   oMaterial.setSpecularColor(Color.ORANGE);
       
		   final PhongMaterial yMaterial = new PhongMaterial();
		   yMaterial.setDiffuseColor(Color.YELLOW);
		   yMaterial.setSpecularColor(Color.YELLOW);
       
		   final PhongMaterial gMaterial = new PhongMaterial();
		   gMaterial.setDiffuseColor(Color.GREEN);
		   gMaterial.setSpecularColor(Color.GREEN);
       
		   final PhongMaterial cMaterial = new PhongMaterial();
		   cMaterial.setDiffuseColor(Color.CYAN);
		   cMaterial.setSpecularColor(Color.CYAN);
       
		   final PhongMaterial bMaterial = new PhongMaterial();
		   bMaterial.setDiffuseColor(Color.BLUE);
		   bMaterial.setSpecularColor(Color.BLUE);
       

       
		   PhongMaterial tMaterial = new PhongMaterial();
	   
       		
		   for (int i=0; i < Earth.listeZones.size();i++) {
       	 
			   float anomalie = Earth.getAno(Earth.listeZones.get(i).getLat(),Earth.listeZones.get(i).getLongi(),année);
			   int lat = Earth.listeZones.get(i).getLat();
			   int lon = Earth.listeZones.get(i).getLongi();
    	   
			   if(anomalie!=Float.NaN) {
       		
				   if( (anomalie <= 10)&&(anomalie > 5) ) {
       				
					   tMaterial = rMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2,lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat, lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
				   else if( (anomalie <= 5)&&(anomalie > 2) ) {
       				
					   tMaterial = oMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2, lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat, lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
				   else if( (anomalie <= 2)&&(anomalie > 0) ) {
       				
					   tMaterial = yMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2, lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat, lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
				   else if( (anomalie <= 0)&&(anomalie > -2) ) {
       				
					   tMaterial = gMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2, lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat,lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
				   else if( (anomalie <= -2)&&(anomalie > -5) ) {
       				
					   tMaterial = cMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2, lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat, lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
				   else if( (anomalie <= -5)&&(anomalie >= -10) ) {
   				
					   tMaterial = bMaterial;
					   Point3D topRight= geoCoordTo3dCoord(lat, lon,1.1f);
					   Point3D bottomRight= geoCoordTo3dCoord(lat-2, lon,1.1f);
					   Point3D bottomLeft= geoCoordTo3dCoord(lat-2, lon-2,1.1f);
					   Point3D topLeft= geoCoordTo3dCoord(lat, lon-2,1.1f);
        		
					   AddQuadrilateral(contour, topRight, bottomRight, bottomLeft, topLeft ,tMaterial);
				   }
       	  
			   }
       
		   }	 
	   	
	   	}
	   	else {System.out.println("Entrez une année entre 1880 et 2020");}
	}
   
	private void afficheHisto(Group contour, int année) {
	   
	   
		if( (année >=1880)&&(année <= 2020)) {
	   
			final PhongMaterial rMaterial = new PhongMaterial();
			rMaterial.setDiffuseColor(Color.RED);
			rMaterial.setSpecularColor(Color.RED);
       
       
			final PhongMaterial oMaterial = new PhongMaterial();
			oMaterial.setDiffuseColor(Color.ORANGE);
			oMaterial.setSpecularColor(Color.ORANGE);
       
			final PhongMaterial yMaterial = new PhongMaterial();
			yMaterial.setDiffuseColor(Color.YELLOW);
			yMaterial.setSpecularColor(Color.YELLOW);
       
			final PhongMaterial gMaterial = new PhongMaterial();
			gMaterial.setDiffuseColor(Color.GREEN);
			gMaterial.setSpecularColor(Color.GREEN);
       
			final PhongMaterial cMaterial = new PhongMaterial();
			cMaterial.setDiffuseColor(Color.CYAN);
			cMaterial.setSpecularColor(Color.CYAN);
       
			final PhongMaterial bMaterial = new PhongMaterial();
			bMaterial.setDiffuseColor(Color.BLUE);
			bMaterial.setSpecularColor(Color.BLUE);
       
			PhongMaterial tMaterial = new PhongMaterial();
       		
			
			for (int i=0; i < Earth.listeZones.size();i++) {
    	   
				float anomalie = Earth.getAno(Earth.listeZones.get(i).getLat(),Earth.listeZones.get(i).getLongi(),année);
				int lat = Earth.listeZones.get(i).getLat();
				int lon = Earth.listeZones.get(i).getLongi();    	   
       	 
				if(anomalie!=Float.NaN) {
       		
					if( (anomalie <= 10) &&(anomalie > 5) ) {
						tMaterial = rMaterial;
        		
						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
					else if( (anomalie <= 5) &&(anomalie > 2) ) {
						tMaterial = oMaterial;
	
						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
					else if( (anomalie <= 2) &&(anomalie > 0) ) {
						tMaterial = yMaterial;
	
						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
					else if( (anomalie <= 0) &&(anomalie > -2) ) {
						tMaterial = gMaterial;

						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
					else if( (anomalie <= -2) &&(anomalie > -5) ) {
						tMaterial = cMaterial;

						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
					else if( (anomalie <= -5) &&(anomalie >= -10) ) {
						tMaterial = bMaterial;

						Cylinder c = createLine(geoCoordTo3dCoord(lat,lon,1.0f), geoCoordTo3dCoord(lat,lon,(float)((anomalie+7)*0.15)));
						c.setMaterial(tMaterial);
						contour.getChildren().add(c);
					}
       		
       	  
				}
       
			} 
		}
		else {System.out.println("Entrez une année entre 1880 et 2020");}
	   
	}


	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
		//CsvFileReader.readCSV();		
		
		
		
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
        	URL modelUrl=this.getClass().getResource("Earth/earth.obj");
        	objImporter.read(modelUrl);
        }catch(ImportException e1) {
        	System.out.println(e1.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        root3D.getChildren().add(earth);
        

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
        
 
                
        
/////////////////////////////////////////////////////////////////////////////////////////////////////

        //Animation
        
        AnimationTimer anim = new AnimationTimer() {
	        final long startNanoTime = System.nanoTime();
			public void handle(long currentNanoTime) {
				
				root3D.getChildren().clear();
	        	root3D.getChildren().add(earth);
	        	root3D.getChildren().add(light);
	        	root3D.getChildren().add(ambientLight);
	        	int vitesse = Integer.parseInt(textFieldVitesse.getText());
	        	
	        	if(btnQuadri.isSelected()) {
	        		
	        		double t = (currentNanoTime - startNanoTime) / (1000000000.0/vitesse);
		        	if(1880+t <= 2020) {
		        		Group contour = new Group();
		        		textFieldAnnee.setText(Integer.toString((int)(1880 + t)));
		        		slider.setValue(1880+t);
			        	afficheQuadri(contour, (int)(1880 + t) );
			        	root3D.getChildren().add(contour);
			        	
		        	}
	        		
		        		
	        	}
	        		
	        	if (btnHisto.isSelected()) {
	        		
	        		double t = (currentNanoTime - startNanoTime) / (1000000000.0/vitesse);
	        		if(1880+t <= 2020) {
	        			Group contour = new Group();
		        		textFieldAnnee.setText(Integer.toString((int)(1880 + t)));
		        		slider.setValue(1880 + t);
		        		afficheHisto(contour, (int)(1880 + t) );
			        	root3D.getChildren().add(contour);
			        	
	        		}
	        		
	        	}
			}
      	};
        
        
		//eventHandler pour les radio boutons
		
		btnQuadri.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickQ(mouseEvent);
				root3D.getChildren().clear();
	        	root3D.getChildren().add(earth);
	        	root3D.getChildren().add(light);
	        	root3D.getChildren().add(ambientLight);
	        	
				if((slider.getValue()>=1880)&&(slider.getValue()<=2020)) {
					Group contour = new Group();
	        		afficheQuadri(contour, (int)slider.getValue() );
	        		root3D.getChildren().add(contour);
				}
				
				
			}
		});
		
		
		btnHisto.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickH(mouseEvent);
				root3D.getChildren().clear();
	        	root3D.getChildren().add(earth);
	        	root3D.getChildren().add(light);
	        	root3D.getChildren().add(ambientLight);
				
				if((slider.getValue()>=1880)&&(slider.getValue()<=2020)) {
					Group contour = new Group();
	        		afficheHisto(contour, (int)slider.getValue() );
	        		root3D.getChildren().add(contour);
				}
				
				
			}
		});
		
		
		//eventHandler pour les boutons
		
		
		btnStart.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickStart(mouseEvent);
			}
		});
		
		
		btnStart.setOnAction(e->{
			root3D.getChildren().clear();
        	root3D.getChildren().add(earth);
        	root3D.getChildren().add(light);
        	root3D.getChildren().add(ambientLight);
			anim.start();
			
			if(slider.getValue()>2020) {
				anim.stop();
			}
        	
        	}
        );
		
		
		
		btnPause.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickP(mouseEvent);
			}
		});
		
		btnPause.setOnAction(e->{
			
			anim.stop();
		});
		
		
		
		btnStop.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickStop(mouseEvent);
			}
		});
		
		btnStop.setOnAction(e->{
			root3D.getChildren().clear();
        	root3D.getChildren().addAll(earth);
        	root3D.getChildren().add(light);
        	root3D.getChildren().add(ambientLight);
        	textFieldAnnee.clear();
    		textFieldVitesse.clear();
			anim.stop();
		});
		
		
		btnGraphique.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				clickG(mouseEvent);
			}
		});
		
		btnGraphique.setOnAction(e->{
					
			Float[] anomalie = new Float[141];
					
			XYChart.Series s  = new XYChart.Series();
					
			anomalie = Earth.getAnoZones(Integer.parseInt(textFieldLat.getText()), Integer.parseInt(textFieldLat.getText()));
			
			for (int i=0; i<=140 ; i++) {
				s.getData().add(new XYChart.Data(i,anomalie[i]));
				
			}
			
			graphe.setLegendVisible(true);
			graphe.getData().clear();
			graphe.setCreateSymbols(false);
			graphe.getData().add(s);
			//paneGraphe.getChildren().add(graphe);
				
		});
		
		
		//eventhandler pour le pane
		
		pane3D.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				
				int lat,lon;
				
				PickResult pr = mouseEvent.getPickResult();
				Point3D p3D = pr.getIntersectedPoint();
				Coordonnees c = coord3DToGeoCoord(p3D);
				
				lat = ( (int) c.getLat()/4)*4;
				lon = ( (int) c.getLongi()/4)*4;
				
				if(c.getLat()<0) {
					lon -= 2;
				}
				else {
					lon += 2;
				}
				
				textFieldLat.setText(Integer.toString(lat));
				textFieldLong.setText(Integer.toString(lon));
				
				
			}
		});
	
		
		
		
		
		//eventhandler pour les textFields
		
		textFieldAnnee.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
		        	
					root3D.getChildren().clear();
		        	root3D.getChildren().add(earth);
		        	root3D.getChildren().add(light);
		        	root3D.getChildren().add(ambientLight);
					
					if (btnQuadri.isSelected()) {
		        		
		        		slider.setValue( Double.parseDouble(textFieldAnnee.getText()));
		        		
		        		Group contour = new Group();
		        		afficheQuadri(contour, Integer.parseInt(textFieldAnnee.getText()) );
		        		root3D.getChildren().add(contour);
		        		
		        	}
		        	
		        	if (btnHisto.isSelected()) {
		        		
		        		slider.setValue( Double.parseDouble(textFieldAnnee.getText()));
		        		
		        		Group contour = new Group();
		        		afficheHisto(contour,Integer.parseInt(textFieldAnnee.getText()));
		        		root3D.getChildren().add(contour);
		        	}
		        	
				}
			}
			
		});
        
        
        textFieldLat.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					
					root3D.getChildren().clear();
		        	root3D.getChildren().add(earth);
		        	root3D.getChildren().add(light);
		        	root3D.getChildren().add(ambientLight);
					
					Group parent=new Group();
					displayTown(parent,"",Integer.parseInt(textFieldLat.getText()),Integer.parseInt(textFieldLong.getText()));
					root3D.getChildren().add(parent);
				}
			}
			
		});
		
        textFieldLong.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					
					root3D.getChildren().clear();
		        	root3D.getChildren().add(earth);
		        	root3D.getChildren().add(light);
		        	root3D.getChildren().add(ambientLight);
					
					Group parent=new Group();
					displayTown(parent,"",Integer.parseInt(textFieldLat.getText()),Integer.parseInt(textFieldLong.getText()));
					root3D.getChildren().add(parent);
				}
			}
			
		});
		
        
        textFieldVitesse.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
		        	if (btnQuadri.isSelected()) {
		        		
		        		root3D.getChildren().clear();
			        	root3D.getChildren().addAll(earth);
			        	root3D.getChildren().add(light);
			        	root3D.getChildren().add(ambientLight);
			        	
		        		Group contour = new Group();
		        		afficheQuadri(contour, Integer.parseInt(textFieldAnnee.getText()) );
		        		root3D.getChildren().add(contour);
		        		
		        	}
		        	
		        	if (btnHisto.isSelected()) {
		        		
		        		root3D.getChildren().clear();
			        	root3D.getChildren().addAll(earth);
			        	root3D.getChildren().add(light);
			        	root3D.getChildren().add(ambientLight);
		        		
		        		Group contour = new Group();
		        		afficheHisto(contour,Integer.parseInt(textFieldAnnee.getText()));
		        		root3D.getChildren().add(contour);
		        	}
		        	
				}
			}
			
		});
		
		
		//eventhandler pour le slider
		
		
		slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				
				root3D.getChildren().clear();
	        	root3D.getChildren().add(earth);
	        	root3D.getChildren().add(light);
	        	root3D.getChildren().add(ambientLight);
				
	        	textFieldAnnee.setText(Integer.toString((int)slider.getValue()));
	        	
				if (btnQuadri.isSelected()) {
				
	    			textFieldAnnee.setText(Integer.toString((int)slider.getValue()));
					
					Group contour = new Group();
					afficheQuadri(contour, (int) slider.getValue());
					root3D.getChildren().add(contour);
					
				}
				else if (btnHisto.isSelected()) {
					
	    			textFieldAnnee.setText(Integer.toString((int)slider.getValue()));

					Group contour = new Group();
					afficheHisto(contour,(int) slider.getValue());
					root3D.getChildren().add(contour);
	        	}
	        	
			}
		});
		
		slider.valueProperty().addListener(e->{
        	
			root3D.getChildren().clear();
        	root3D.getChildren().add(earth);
        	root3D.getChildren().add(light);
        	root3D.getChildren().add(ambientLight);
        	
        	textFieldAnnee.setText(Integer.toString((int)slider.getValue()));
        	
        	if (btnQuadri.isSelected()) {
        		
        		textFieldAnnee.setText(Integer.toString((int)slider.getValue()));
        		
        		Group contour = new Group();
        		afficheQuadri(contour,Integer.parseInt(textFieldAnnee.getText()));
        		root3D.getChildren().add(contour);

        	}
        	
        	else if (btnHisto.isSelected()) {
    			
        		textFieldAnnee.setText(Integer.toString((int)slider.getValue()));

    			Group contour = new Group();
        		afficheHisto(root3D,Integer.parseInt(textFieldAnnee.getText()));
        		root3D.getChildren().add(contour);
        	}
        	
        });
        
        
		
		
        //////////////////////////////////////////////////////////////////////////////////////////////
		
		
			
		 // Create scene (width du pane : 457 | height du pane : 312)
         SubScene subscene = new SubScene(root3D, 457.0,312.0, true, SceneAntialiasing.BALANCED);
         subscene.setCamera(camera);
         subscene.setFill(Color.GREY);
         pane3D.getChildren().addAll(subscene);
         
        
		
	}
	
}