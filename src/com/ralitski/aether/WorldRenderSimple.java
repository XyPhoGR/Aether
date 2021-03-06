package com.ralitski.aether;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.ralitski.util.Ticker;
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
	
	private ColorTransition colors;
	
	public WorldRenderSimple() {
		TexturedCenteredSquareRenderListCW.FULL.compile();
		try {
			GLImage img = new GLImage(new Image(ImageIO.read(getClass().getResourceAsStream("/res/circle.png"))));
			img.glPrepare();
			circle = new GLTexture(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		colors = new ColorTransition(new ColorsPastel(), Ticker.ticksPerSecond(0.1F));
	}

	@Override
	public void renderBackground(BoundingBox2d box, float rot) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(box.getMinX(), box.getMinY(), 0);
		GL11.glScalef(box.getWidth(), box.getHeight(), 1);
		nextBgColor().glColor();
		GLListHelper.getSquareListUncenteredCW().call();
		GL11.glPopMatrix();
	}
	
	private Color nextBgColor() {
		return colors.next();
	}

	@Override
	public void renderPlanet(Planet planet) {
		Body body = planet.getBody();
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
		
		float scale2 = scale + 1F;
		
		GL11.glScalef(scale2, scale2, 1);
		Color.WHITE.glColor();
		TexturedCenteredSquareRenderListCW.FULL.call();
		scale2 = 1F / scale2;
		GL11.glScalef(scale2, scale2, 1);
		
		GL11.glScalef(scale, scale, 1);
		body.getColor().glColor();
		TexturedCenteredSquareRenderListCW.FULL.call();
		GL11.glPopMatrix();
	}

	@Override
	public void renderPlayer(Player player, int index, int count) {
		Body body = player.getBody();
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

		GL11.glScalef(1F / scale, 1F / scale, 1);
		scale = scale * 0.6F;
		GL11.glScalef(scale, scale, 1);
		
		body.getColor().inverse().glColor();
		TexturedCenteredSquareRenderListCW.FULL.call();
		
		GL11.glPopMatrix();
	}

}
