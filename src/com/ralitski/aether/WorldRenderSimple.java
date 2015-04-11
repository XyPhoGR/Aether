package com.ralitski.aether;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.ralitski.util.math.geom.d2.BoundingBox2d;
import com.ralitski.util.math.geom.d2.Circle;
import com.ralitski.util.math.geom.d2.Point2d;
import com.ralitski.util.math.geom.d2.Shape2d;
import com.ralitski.util.render.img.Color;
import com.ralitski.util.render.img.GLImage;
import com.ralitski.util.render.img.GLTexture;
import com.ralitski.util.render.img.Image;
import com.ralitski.util.render.list.GLListHelper;
import com.ralitski.util.render.list.TexturedCenteredSquareRenderListCW;

public class WorldRenderSimple implements WorldRender {
	
	private GLTexture circle;
	
	public WorldRenderSimple() {
		TexturedCenteredSquareRenderListCW.FULL.compile();
		try {
			GLImage img = new GLImage(new Image(ImageIO.read(getClass().getResourceAsStream("/res/circle.png"))));
			img.glPrepare();
			circle = new GLTexture(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderBackground(BoundingBox2d box) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(box.getMinX(), box.getMinY(), 0);
		GL11.glScalef(box.getWidth(), box.getHeight(), 1);
		Color.GRAY.glColor();
		GLListHelper.getSquareListUncenteredCW().call();
		GL11.glPopMatrix();
	}

	@Override
	public void renderPlanet(Planet planet) {
		renderBody(planet.getBody());
	}

	@Override
	public void renderPlayer1(Player player) {
		renderBody(player.getBody());
	}

	@Override
	public void renderPlayer2(Player player) {
		renderBody(player.getBody());
	}
	
	private void renderBody(Body body) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		circle.glBind();
		GL11.glPushMatrix();
		Point2d pos = body.getShape().getPosition();
		GL11.glTranslatef(pos.getX(), pos.getY(), 0);
		
		float scale = 1;
		Shape2d shape = body.getShape();
		if(shape instanceof Circle) {
			scale = ((Circle)shape).getRadius();
		}
		
		GL11.glScalef(scale, scale, 1);
		body.getColor().glColor();
		TexturedCenteredSquareRenderListCW.FULL.call();
		GL11.glPopMatrix();
	}

}