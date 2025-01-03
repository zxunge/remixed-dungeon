package com.nyrds.platform.gl;

import com.badlogic.gdx.Gdx;
import com.nyrds.platform.gfx.BitmapData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Texture {

	public static final int NEAREST	= Gdx.gl20.GL_NEAREST;
	public static final int LINEAR	= Gdx.gl20.GL_LINEAR;

	public static final int REPEAT	= Gdx.gl20.GL_REPEAT;
	public static final int MIRROR	= Gdx.gl20.GL_MIRRORED_REPEAT;
	public static final int CLAMP	= Gdx.gl20.GL_CLAMP_TO_EDGE;
	
	protected int id;

	static private final int[] binded = new int[32];
	static private int active = 0;
	
	public Texture() {
		id = Gdx.gl20.glGenTexture();

		if(id==0) {
			throw new AssertionError();
		}

		//PUtil.slog("texture", "creating " + id);
		bind();
	}
	
	public static void activate( int index ) {
		active = index;
		Gdx.gl20.glActiveTexture( Gdx.gl20.GL_TEXTURE0 + index );
	}
	
	public void bind() {
		if( binded[active] != id ) {
			//PUtil.slog("texture", "binding " + id);
			Gdx.gl20.glBindTexture(Gdx.gl20.GL_TEXTURE_2D, id);
			Gl.glCheck();
			binded[active] = id;
		}
	}

	static public void unbind() {
		binded[active] = -1;
	}

	public void filter( int minMode, int maxMode ) {
		bind();
		Gdx.gl20.glTexParameterf( Gdx.gl20.GL_TEXTURE_2D, Gdx.gl20.GL_TEXTURE_MIN_FILTER, minMode );
		Gdx.gl20.glTexParameterf( Gdx.gl20.GL_TEXTURE_2D, Gdx.gl20.GL_TEXTURE_MAG_FILTER, maxMode );
		Gl.glCheck();
	}
	
	public void wrap( int s, int t ) {
		bind();
		Gdx.gl20.glTexParameterf( Gdx.gl20.GL_TEXTURE_2D, Gdx.gl20.GL_TEXTURE_WRAP_S, s );
		Gdx.gl20.glTexParameterf( Gdx.gl20.GL_TEXTURE_2D, Gdx.gl20.GL_TEXTURE_WRAP_T, t );
		Gl.glCheck();
	}
	
	public void delete() {
		Gdx.gl20.glDeleteTexture(id);
		//PUtil.slog("texture", "deleting " + id);
	}
	
	public void bitmap( BitmapData bitmap ) {
		bind();
		handMade(bitmap, false);
	}
	
	public void pixels( int w, int h, int[] pixels ) {
		bind();
		
		IntBuffer imageBuffer = ByteBuffer.
			allocateDirect( w * h * 4 ).
			order( ByteOrder.nativeOrder() ).
			asIntBuffer();
		imageBuffer.put( pixels );
		imageBuffer.position( 0 );

		Gdx.gl20.glTexImage2D(
			Gdx.gl20.GL_TEXTURE_2D, 
			0, 
			Gdx.gl20.GL_RGBA, 
			w, 
			h, 
			0, 
			Gdx.gl20.GL_RGBA, 
			Gdx.gl20.GL_UNSIGNED_BYTE, 
			imageBuffer );

		Gl.glCheck();
	}

	public void pixels( int w, int h, byte[] pixels ) {
		
		bind();
		
		ByteBuffer imageBuffer = ByteBuffer.
			allocateDirect( w * h ).
			order( ByteOrder.nativeOrder() );
		imageBuffer.put( pixels );
		imageBuffer.position( 0 );
		
		Gdx.gl20.glPixelStorei( Gdx.gl20.GL_UNPACK_ALIGNMENT, 1 );
		
		Gdx.gl20.glTexImage2D(
			Gdx.gl20.GL_TEXTURE_2D, 
			0, 
			Gdx.gl20.GL_ALPHA, 
			w, 
			h, 
			0, 
			Gdx.gl20.GL_ALPHA, 
			Gdx.gl20.GL_UNSIGNED_BYTE, 
			imageBuffer );

		Gl.glCheck();
	}
	

	public void handMade( BitmapData bitmap, boolean recode ) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		//PUtil.slog("texture", "handmade " + w + "x" + h);
		int[] pixels = new int[w * h]; 
		bitmap.getAllPixels(pixels);

		final int as = 0;
		final int rs = 8;
		final int gs = 16;
		final int bs = 24;

		if (recode) {
			for (int i=0; i < pixels.length; i++) {
				int color = pixels[i];
				int a = (color >> as) & 0xFF;
				int r = (color >> rs) & 0xFF;
				int g = (color >> gs) & 0xFF;
				int b = (color >> bs) & 0xFF;

				pixels[i] = (a << 24) | (r << 16) | (g << 8) | (b);
			}
		}

		pixels( w, h, pixels );
	}
}
