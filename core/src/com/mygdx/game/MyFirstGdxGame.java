package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class MyFirstGdxGame extends ApplicationAdapter {
	enum Screen {
		SPLASH,
		TITLE,
		MAIN_GAME,
		GAME_OVER
	}
	Screen currentScreen;

	SpriteBatch batch;
	Texture texture;
	BitmapFont font;
	ShapeRenderer sr;

	// UI
	Skin skin;
	Stage UI_Stage;
	Table table;

	//
	Player player;
	ArrayList<Coin> coins;
	int coinCount;

	float splash_radius;

	// Audio
	Sound SFX_Select;
	Music SFX_Startup;
	Music BGM_Main;
	Music BGM_Title;
	Music BGM_Fanfare;
	float volume;

	int offset = 24;


	@Override
	public void create () {
		SetUp();
		SFX_Startup.play();
	}

	@Override
	public void render () {
		switch (currentScreen){
			case SPLASH:
				SplashScreen();
				break;
			case TITLE:
				TitleMenu();
				break;
			case MAIN_GAME:
				MainGameScreen();
				break;
			case GAME_OVER:
				GameOverScreen();
				break;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}

	public void resize (int width, int height) {
		UI_Stage.getViewport().update(width, height, true);
	}

	public void SetUp(){
		batch = new SpriteBatch();
		Pixmap pixmap = new Pixmap(128,128, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);

		//texture = createBgTexture(new Vector2(128,128));
		font = new BitmapFont();
		sr = new ShapeRenderer();
		player = new Player();
		splash_radius = 0;
		currentScreen = Screen.SPLASH;

		// UI Setup
		skin = new Skin(Gdx.files.internal("data/vhs/skin/vhs-ui.json"));
		UI_Stage = new Stage();
		table = new Table(skin);
		table.setFillParent(true);
		UI_Stage.addActor(table);
		Label Title = new Label("Title Screen", skin);
		Label Collect = new Label("Collect all the rings to win.", skin);
		Label Press = new Label("Press space to play.", skin);
		Button BTN_StartGame = new Button();
		BTN_StartGame.setSkin(skin);
		Title.setAlignment(1,0);
		Title.setFontScale(2);
		Collect.setAlignment(1,0);
		Press.setAlignment(1,0);

		Label volumeLabel = new Label("Volume: ", skin);
		Slider volumeSlider = new Slider(0,100,1,false, skin);
		volumeSlider.setValue(70);


		volumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Changed!");
				System.out.println("ChangeEvent: "+event);
				System.out.println("Actor: "+actor);

			}
		});
		table.row();
		table.add(Title).top().colspan(2).expandY();

		table.row();
		table.add(Collect).center().colspan(2).expandY();
		table.row();
		table.add(Press).center().colspan(2).expandY();

		table.row();
		table.add(volumeLabel);
		table.add(volumeSlider);
		Gdx.input.setInputProcessor(UI_Stage);

		// Audio Setup
		volume = volumeSlider.getValue()/100;
		SFX_Select = Gdx.audio.newSound(Gdx.files.internal("menu_select.wav"));
		SFX_Startup = Gdx.audio.newMusic(Gdx.files.internal("Computer_StartUp.wav"));
		BGM_Title = Gdx.audio.newMusic(Gdx.files.internal("Illusionary Joururi.mp3"));
		BGM_Main = Gdx.audio.newMusic(Gdx.files.internal("Heartless.mp3"));
		BGM_Fanfare = Gdx.audio.newMusic(Gdx.files.internal("02 Fanfares and Flowers.mp3"));
		SFX_Select.setVolume(1, volume);
		SFX_Startup.setVolume(volume);
		BGM_Title.setVolume(volume);
		BGM_Main.setVolume(volume);
		BGM_Fanfare.setVolume(volume);

		// Setup coin spawn points
		coins = new ArrayList<Coin>();
		coinCount = 15;

		// Setup input handler events
		InputHandlers();

	}

	public void InputHandlers(){

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown (int keyCode) {
				if(currentScreen == Screen.TITLE && keyCode == Input.Keys.SPACE) {
					currentScreen = Screen.MAIN_GAME;
					SFX_Select.play();
				}
				else if(currentScreen == Screen.MAIN_GAME && keyCode == Input.Keys.F) {
					CheckCoinPickUp();
				}
				else if(currentScreen == Screen.GAME_OVER && keyCode == Input.Keys.SPACE) {
					currentScreen = Screen.TITLE;
					SFX_Select.play();
					player.score = 0;
				}
				return true;
			}
		});
	}

	void CheckCoinPickUp(){
		// Removing elements greater than 10 using remove() method
		Iterator itr = coins.iterator();
		while (itr.hasNext())
		{
			Coin coin = (Coin)itr.next();
			float dist = Vector2.dst(player.Pos.x, player.Pos.y, coin.Pos.x, coin.Pos.y);
			boolean isOverlapping = (dist < coin.radius+8);
			System.out.println(coin + ": " + dist);
			System.out.println("Is overlapping: " + isOverlapping);
			if (isOverlapping) {
				player.score++;
				itr.remove();

				SFX_Select.play();
			}
		}
	}

	public void SplashScreen(){
		if(!SFX_Startup.isPlaying())
			currentScreen = Screen.TITLE;
		Gdx.gl.glClearColor(.05f, .05f, .05f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		Vector3 color =  new Vector3(0, .25f, 0);
		splash_radius += 2.5f;
		int x = (Gdx.graphics.getWidth())/2;
		int y = (Gdx.graphics.getHeight())/2;
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(color.x, color.y, color.z, 1);
		sr.circle(x,y, splash_radius);
		sr.end();
		batch.draw(texture, (Gdx.graphics.getWidth())/2-texture.getWidth()/2, (Gdx.graphics.getHeight())/2-texture.getHeight()/2);
		batch.end();
	}

	public void TitleMenu(){
		if(BGM_Main.isPlaying())
			BGM_Main.stop();
		if(BGM_Fanfare.isPlaying())
			BGM_Fanfare.stop();
		BGM_Title.play();
		BGM_Title.setLooping(true);

		Gdx.gl.glClearColor(0, .25f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		/**
		batch.begin();
		font.draw(batch, "Title Screen!", 25f, Gdx.graphics.getHeight() - offset);
		font.draw(batch, "Click the circle to win.", 25f, Gdx.graphics.getHeight() - offset*2);
		font.draw(batch, "Press space to play.", 25f, Gdx.graphics.getHeight() - offset*3);
		batch.end();
		 */
		batch.begin();
		if(coins.size() < 1)
			SpawnCoins();
		batch.end();

		// UI Batch
		batch.begin();
		UI_Stage.act(Gdx.graphics.getDeltaTime());
		UI_Stage.draw();
		// volumeSlider.draw(batch, 1);
		batch.end();
	}

	public void MainGameScreen(){
		if(BGM_Title.isPlaying())
			BGM_Title.stop();
		BGM_Main.play();
		BGM_Main.setLooping(true);
		Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "Score: " + player.score, 25f, Gdx.graphics.getHeight() - 28f);
		font.draw(batch, "Press 'F' to pick up coins.", 25f, Gdx.graphics.getHeight() - 48f);
		batch.end();
		batch.begin();



		for (Coin coin : coins) {
			coin.PlaceCoin();
		}
		if(coins.size() < 1)
			currentScreen = Screen.GAME_OVER;
		batch.end();

		player.Move();

	}

	public void GameOverScreen(){
		if(BGM_Main.isPlaying())
			BGM_Main.stop();
		BGM_Fanfare.play();
		Gdx.gl.glClearColor(.25f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		font.draw(batch, "Your Score: " + player.score, 25f, Gdx.graphics.getHeight() - 28f);
		font.draw(batch, "Press space to restart.", 25f, Gdx.graphics.getHeight() - 125f);
		batch.end();
	}

	public void DarwCircle(float x, float y, float radius, Vector3 color) {
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(color.x, color.y, color.z, 1);
		sr.circle(x, y, radius);
		sr.end();
	}

	public Texture createBgTexture(Vector2 size) {
		Pixmap pixmap = new Pixmap((int)size.x, (int)size.y, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		//pixmap.drawCircle((int)size.x/2, (int)size.y/2, 16);
		pixmap.drawLine(0,0,(int)size.x, (int)size.y);
		pixmap.fill();
		Texture texture = new Texture(pixmap); // must be manually disposed
		return texture;
	}

	void SpawnCoins(){
		coins = new ArrayList<>();
		for (int i = 0; i < coinCount; i++) {
			float rndX = random.nextInt(Gdx.graphics.getWidth()-10);
			float rndY = random.nextInt(Gdx.graphics.getHeight()-10);
			Coin coin = new Coin();
			coin.Pos = new Vector2(rndX, rndY);
			coin.radius = 10;
			coins.add(coin);
		}

	}
}


/**
public class TitleScreen extends ScreenAdapter{

}
 */