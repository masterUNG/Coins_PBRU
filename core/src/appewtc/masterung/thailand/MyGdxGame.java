package appewtc.masterung.thailand;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {

	//Explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture,
			pigTexture, coinsTexture, rainTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont, scoreBitmapFont, showScoreBitmapFont;
	private int xCloudAnInt, yCloudAnInt = 600;
	private boolean cloudABoolean = true, finishABoolean = false;
	private Rectangle pigRectangle, coinsRectangle, rainRectangle;
	private Vector3 objVector3;
	private Sound pigSound, waterDropSound, coinsDropSound;
	private Array<Rectangle> coinsArray, rainArray;
	private long lastDropCoins, lastDropRain;
	private Iterator<Rectangle> coinsIterator, rainIterator; // ===> Java.util
	private int scoreAnInt = 0, falseAnInt = 0, finialScoreAnInt;
	private Music rainMusic, backgroundMusic;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//คือการกำหนดขนาดของจอที่ต้องการ
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1200, 800);

		//SetUP WallPaper
		wallpaperTexture = new Texture("background.png");

		//SetUP BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.RED);
		nameBitmapFont.setScale(4);

		//Setup Cloud
		cloudTexture = new Texture("cloud.png");

		//Setup Pig
		pigTexture = new Texture("pig.png");

		//Setup Rectangle Pig
		pigRectangle = new Rectangle();
		pigRectangle.x = 568;
		pigRectangle.y = 100;
		pigRectangle.width = 64;
		pigRectangle.height = 64;

		//Setup Pig Sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));

		//Setup Coins
		coinsTexture = new Texture("coins.png");

		//Create coinsArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();

		//Setup WaterDrop
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));

		//Setup Coins Drop
		coinsDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

		//Setup scoreBitMapFont
		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(Color.BLUE);
		scoreBitmapFont.setScale(4);

		//Setup rainTexture
		rainTexture = new Texture("droplet.png");

		//Create rainArray
		rainArray = new Array<Rectangle>();
		rainRandomDrop();

		//Setup rainMusic
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		//Setup backgroundMusic
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("bggame.mp3"));

		//Setup ShowScore
		showScoreBitmapFont = new BitmapFont();
		showScoreBitmapFont.setColor(230, 28, 223, 255);
		showScoreBitmapFont.setScale(5);

	}	// create เอาไว้กำหนดค่า

	private void rainRandomDrop() {

		rainRectangle = new Rectangle();
		rainRectangle.x = MathUtils.random(0, 1136);
		rainRectangle.y = 800;
		rainRectangle.width = 64;
		rainRectangle.height = 64;

		rainArray.add(rainRectangle);
		lastDropRain = TimeUtils.nanoTime();

	}

	private void coinsRandomDrop() {

		coinsRectangle = new Rectangle();
		coinsRectangle.x = MathUtils.random(0, 1136);
		coinsRectangle.y = 800;
		coinsRectangle.width = 64;
		coinsRectangle.height = 64;
		coinsArray.add(coinsRectangle);
		lastDropCoins = TimeUtils.nanoTime();
	}	// coinsRandomDrop

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Setup Screen
		objOrthographicCamera.update();
		batch.setProjectionMatrix(objOrthographicCamera.combined);



		//เอาไว้วาด Object
		batch.begin();

		//Drawable Wallpaper
		batch.draw(wallpaperTexture, 0, 0);

		//Drawable Cloud
		batch.draw(cloudTexture, xCloudAnInt, yCloudAnInt);

		//Drawable BitMapFont
		nameBitmapFont.draw(batch, "Coins PBRU", 50, 750);

		//Drawable Pig
		batch.draw(pigTexture, pigRectangle.x, pigRectangle.y);

		//Drawable Coins
		for (Rectangle forCoins : coinsArray) {
			batch.draw(coinsTexture, forCoins.x, forCoins.y);
		}

		//Drawable Score
		scoreBitmapFont.draw(batch, "Score = " + Integer.toString(scoreAnInt), 800, 750);

		//Drawable Rain
		for (Rectangle forRain : rainArray) {
			batch.draw(rainTexture, forRain.x, forRain.y);
		}

		if (finishABoolean) {

			batch.draw(wallpaperTexture, 0 , 0);
			showScoreBitmapFont.draw(batch, "Your Score ==> " + Integer.toString(finialScoreAnInt), 500, 750);

		}	//if




		batch.end();

		//Move Cloud
		moveCloud();

		//Active When Touch Screen
		activeTouchScreen();

		//Random Drop Coins
		randomDropCoins();

		//Random Drop Rain
		randomDropRain();

		//Play rainMusic
		rainMusic.play();

		//Play backGroundMusic
		backgroundMusic.play();

	}	// render ตัวนี่คือ loop

	private void randomDropRain() {

		if (TimeUtils.nanoTime() - lastDropRain > 1E9) {
			rainRandomDrop();
		}	// if

		rainIterator = rainArray.iterator();
		while (rainIterator.hasNext()) {

			Rectangle myRainRectangle = rainIterator.next();
			myRainRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			// When Rain drop into Floot
			if (myRainRectangle.y + 64 < 0) {
				waterDropSound.play();
				rainIterator.remove();
			} // if

			//When Rain Overlap Pig
			if (myRainRectangle.overlaps(pigRectangle)) {
				scoreAnInt -= 1;
				waterDropSound.play();
				rainIterator.remove();
			}    // if

		}    // while

	}	// randomDropRain

	private void randomDropCoins() {

		if (TimeUtils.nanoTime() - lastDropCoins > 1E9) {
			coinsRandomDrop();
		}

		coinsIterator = coinsArray.iterator();
		while (coinsIterator.hasNext()) {

			Rectangle myCoinsRectangle = coinsIterator.next();
			myCoinsRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			//When Coins into Floor
			if (myCoinsRectangle.y + 64 < 0) {
				falseAnInt += 1;
				waterDropSound.play();
				coinsIterator.remove();
				checkFalse();

			}	// if

			//When Coins OverLap Pig
			if (myCoinsRectangle.overlaps(pigRectangle)) {
				scoreAnInt += 1;
				coinsDropSound.play();
				coinsIterator.remove();
			}	// if

		}	// while Loop

	}	// randomDropCoins

	private void checkFalse() {
		if (falseAnInt > 20) {
			dispose();

			if (!finishABoolean) {
				finialScoreAnInt = scoreAnInt;
			}

			finishABoolean = true;

		}	// if
	}	//checkFalse

	@Override
	public void dispose() {
		super.dispose();

		backgroundMusic.dispose();
		rainMusic.dispose();
		pigSound.dispose();
		waterDropSound.dispose();
		coinsDropSound.dispose();


	}	// dispose

	private void activeTouchScreen() {

		if (Gdx.input.isTouched()) {

			//Sound Effect Pig
			pigSound.play();

			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			if (objVector3.x < Gdx.graphics.getWidth()/2) {
				if (pigRectangle.x < 0) {
					pigRectangle.x = 0;
				} else {
					pigRectangle.x -= 10;
				}

			} else {
				if (pigRectangle.x > 1136) {
					pigRectangle.x = 1136;
				} else {
					pigRectangle.x += 10;
				}

			}

		}	//if

	}	// activeTouchScreen

	private void moveCloud() {

		if (cloudABoolean) {
			if (xCloudAnInt < 937) {
				xCloudAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		} else {
			if (xCloudAnInt > 0) {
				xCloudAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		}

	}	// moveCloud

}	// Main Class
