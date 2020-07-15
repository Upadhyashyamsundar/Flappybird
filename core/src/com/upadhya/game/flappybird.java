package com.upadhya.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;


public class flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bottomtube;
	Texture gameover;
	Texture toptube;
	float birdsy=0;
	float velocity=0;
	int gamestate=0;
	float Gravity=2;
	float gap=400;
	float maxtubeoffset;
	Random rand;
	int score=0;
	int scoringtube=0;
	BitmapFont font;

	Music music;


	BitmapFont scoreshow;
	BitmapFont highscorefont;


	Preferences prefs;
	int highscore=0;


	Circle birdcircel;
	Rectangle[] toptuberectangle;
	Rectangle[] bottomtuberectangle;
	//ShapeRenderer shapeRenderer;

	float tubevelocity=4;
	int numberoftubes=4;
	float[] tubex=new float[numberoftubes];
	float tubeoffset[]=new float[numberoftubes];
	float tubedistance;

	Texture[] birds;
	int flipstate;

	/*Skin skin;
	ImageButton playbutton;
	Stage stage;*/




	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		bottomtube=new Texture("bottomtube.png");
		toptube=new Texture("toptube.png");
		gameover=new Texture("gameoverreal.png");

		music=Gdx.audio.newMusic(Gdx.files.internal("bgmusicf.mp3"));
		music.setLooping(true);
		music.setVolume(1f);
		music.play();

		prefs = Gdx.app.getPreferences("game preferences");


		font=new BitmapFont();
		scoreshow=new BitmapFont();
		scoreshow.setColor(Color.WHITE);
		scoreshow.getData().setScale(8);
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		highscorefont=new BitmapFont();
		highscorefont.setColor(Color.WHITE);
		highscorefont.getData().setScale(7);




		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

		maxtubeoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		rand=new Random((long)maxtubeoffset);
		tubedistance=Gdx.graphics.getWidth()*3/4;

		//shapeRenderer=new ShapeRenderer();
		birdcircel=new Circle();
		toptuberectangle=new Rectangle[numberoftubes];
		bottomtuberectangle=new Rectangle[numberoftubes];

		/*stage=new Stage();
		skin=new Skin(Gdx.files.internal("playbutton.png"));
		playbutton=new ImageButton(skin);
		stage.addActor(playbutton);*/




		startGame();


	}

	public void startGame(){
		birdsy=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

		for(int i=0;i<numberoftubes;i++){

			tubeoffset[i]=(rand.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);

			tubex[i]=Gdx.graphics.getWidth()/2-toptube.getWidth()/2+Gdx.graphics.getWidth()+i*tubedistance;

			toptuberectangle[i]=new Rectangle();
			bottomtuberectangle[i]=new Rectangle();


		}
	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		highscore();

		if(tubex[scoringtube]<Gdx.graphics.getWidth()/2) {

			score++;


			if (scoringtube < numberoftubes-1) {

				scoringtube++;
			} else {

				scoringtube = 0;
			}
		}

		if(gamestate==1)
		{


			if(Gdx.input.justTouched()){

				velocity=-25;
			}


			for(int i=0;i<numberoftubes;i++) {

				if (tubex[i] < -toptube.getWidth()) {

					tubex[i] += numberoftubes * tubedistance;
					tubeoffset[i]=(rand.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
				} else {

					tubex[i] = tubex[i] - tubevelocity;


				}
				batch.draw(toptube, tubex[i], (float) (Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 1.75));
				batch.draw(bottomtube, tubex[i], (float) (Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeoffset[i] / 1.75));

				toptuberectangle[i]=new Rectangle(tubex[i],(float) (Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 1.75),toptube.getWidth(),toptube.getHeight());
				bottomtuberectangle[i]=new Rectangle(tubex[i], (float) (Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeoffset[i] / 1.75),bottomtube.getWidth(),bottomtube.getHeight());
			}



			if (birdsy>0) {

				velocity=velocity+Gravity;
				birdsy -= velocity;
			}else{
				gamestate=2;
			}

		} else if(gamestate==0) {
			if (Gdx.input.justTouched()) {

				gamestate = 1;
			}
		}else if(gamestate==2){

			gameover();

		}
		if (flipstate == 0) {
			flipstate = 1;
		} else {
			flipstate = 0;
		}

		batch.draw(birds[flipstate], Gdx.graphics.getWidth() / 2 - birds[flipstate].getWidth() / 2, birdsy);
		font.draw(batch,String.valueOf(score),50,2000);


		batch.end();

		birdcircel.set(Gdx.graphics.getWidth()/2,birdsy+birds[flipstate].getHeight()/2,birds[flipstate].getWidth()/2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdcircel.x,birdcircel.y,birdcircel.radius);
		for(int i=0;i<numberoftubes;i++){

			//shapeRenderer.rect(tubex[i],(float) (Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 1.75),toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubex[i], (float) (Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeoffset[i] / 1.75),bottomtube.getWidth(),bottomtube.getHeight());

			if(Intersector.overlaps(birdcircel,toptuberectangle[i]) ||Intersector.overlaps(birdcircel,bottomtuberectangle[i])){

				gamestate=2;
			}


		}
		//shapeRenderer.end();



	}
	public void gameover(){

		batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
		scoreshow.draw(batch,"Your Score:"+String.valueOf(score),Gdx.graphics.getWidth()/2-350,Gdx.graphics.getHeight()/2-150);
		highscorefont.draw(batch,"Highscore:"+String.valueOf(highscore),480,2000);
		music.stop();
		//stage.draw();
		//stage.act();
		if(Gdx.input.justTouched()) {

			gamestate = 1;
			startGame();
			score=0;
			scoringtube=0;
			velocity=0;
			music.play();
		}
	}



	public void highscore(){

		if(score>highscore){

			prefs.putInteger("highscore",score);
			prefs.flush();

		}
		highscore=prefs.getInteger("highscore",highscore);

	}



}
